package khushi.example.greenmarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPhoneOrEmail;
    private ImageButton buttonContinue;
    private RelativeLayout loadingOverlay;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextPhoneOrEmail = findViewById(R.id.editTextPhoneOrEmail);
        buttonContinue = findViewById(R.id.buttonContinue);
        loadingOverlay = findViewById(R.id.loadingOverlay);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                hideLoading();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideLoading();
                Toast.makeText(LoginActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                hideLoading();
                Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                intent.putExtra("verificationId", verificationId);
                intent.putExtra("phoneNumber", editTextPhoneOrEmail.getText().toString().trim().substring(3));
                startActivity(intent);
            }
        };

        buttonContinue.setOnClickListener(v -> {
            String input = editTextPhoneOrEmail.getText().toString().trim();

            if (TextUtils.isEmpty(input) || input.length() < 13) {
                Toast.makeText(this, "Please enter your 10 digit mobile number after +91", Toast.LENGTH_SHORT).show();
            } else if (isValidPhone(input)) {
                startPhoneNumberVerification(input.substring(3));
            } else {
                Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPhone(String input) {

        if (!input.startsWith("+91")) return false;
        String phone = input.substring(3);
        return phone.matches("^[0-9]{10}$");
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        showLoading();


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    hideLoading();
                    if (task.isSuccessful()) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading() {
        loadingOverlay.setVisibility(RelativeLayout.VISIBLE);
    }

    private void hideLoading() {
        loadingOverlay.setVisibility(RelativeLayout.GONE);
    }
}
