package khushi.example.greenmarket;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText inputEmailPhone;
    private Button btnReset;
    private TextView textBackToLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();
        inputEmailPhone = findViewById(R.id.inputEmailPhone);
        btnReset = findViewById(R.id.btnReset);
        textBackToLogin = findViewById(R.id.textBackToLogin);

        btnReset.setOnClickListener(v -> {
            String val = inputEmailPhone.getText().toString().trim();
            if (TextUtils.isEmpty(val)) {
                Toast.makeText(this, "Please enter email or phone", Toast.LENGTH_SHORT).show();
                return;
            }

            if (val.contains("@")) {

                auth.sendPasswordResetEmail(val)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(this, "Error: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            } else {

                Toast.makeText(this, "Phone reset via OTP not implemented yet", Toast.LENGTH_SHORT).show();

            }
        });

        textBackToLogin.setOnClickListener(v -> finish());
    }
}
