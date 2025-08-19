package khushi.example.greenmarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    Spinner spinnerUserType, spinnerState;
    EditText etFullName, etVillage, etPincode;
    Button btnSignup;

    DatabaseReference databaseReference;
    String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        spinnerUserType = findViewById(R.id.spinnerUserType);
        spinnerState    = findViewById(R.id.spinnerState);
        etFullName      = findViewById(R.id.etFullName);
        etVillage       = findViewById(R.id.etVillage);
        etPincode       = findViewById(R.id.etPincode);
        btnSignup       = findViewById(R.id.btnSignup);


        phoneNumber = getIntent().getStringExtra("phoneNumber");
        databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(phoneNumber);

        ArrayAdapter<CharSequence> userTypeAdapter =
                ArrayAdapter.createFromResource(
                        this, R.array.user_types,
                        android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(userTypeAdapter);

        ArrayAdapter<CharSequence> stateAdapter =
                ArrayAdapter.createFromResource(
                        this, R.array.indian_states,
                        android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(stateAdapter);

        btnSignup.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String village  = etVillage.getText().toString().trim();
            String pincode  = etPincode.getText().toString().trim();

            if (fullName.isEmpty() || village.isEmpty() || pincode.isEmpty()) {
                Toast.makeText(
                        SignupActivity.this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
            } else {
                saveUserData();
            }
        });
    }

    private void saveUserData() {
        String userType = spinnerUserType.getSelectedItem().toString();
        String fullName = etFullName.getText().toString().trim();
        String state    = spinnerState.getSelectedItem().toString();
        String village  = etVillage.getText().toString().trim();
        String pincode  = etPincode.getText().toString().trim();

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("userType",    userType);
        userMap.put("fullName",    fullName);
        userMap.put("state",       state);
        userMap.put("village",     village);
        userMap.put("pincode",     pincode);
        userMap.put("phoneNumber", phoneNumber);

        databaseReference.setValue(userMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(
                            SignupActivity.this,
                            "Signup Successful!",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(
                            SignupActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(
                        SignupActivity.this,
                        "Signup Failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
    }
}
