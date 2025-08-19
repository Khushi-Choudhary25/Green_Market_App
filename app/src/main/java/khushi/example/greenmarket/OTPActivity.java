package khushi.example.greenmarket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private Button buttonVerify;
    private TextView textViewResend, textViewSubtitle;
    private FrameLayout loadingOverlay;
    private ProgressBar progressBar;

    private String verificationId;
    private String phoneNumber;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30000;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);

        buttonVerify   = findViewById(R.id.buttonVerify);
        textViewResend = findViewById(R.id.textViewResend);
        textViewSubtitle = findViewById(R.id.textViewSubtitle);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        progressBar    = findViewById(R.id.progressBar);

        verificationId = getIntent().getStringExtra("verificationId");
        phoneNumber    = getIntent().getStringExtra("phoneNumber");
        resendToken    = getIntent().getParcelableExtra("resendToken");

        if (phoneNumber != null) {
            textViewSubtitle.setText(
                    "Enter the security code we sent to " + phoneNumber);
        }

        setupOtpInputs();

        buttonVerify.setOnClickListener(v -> {
            String code = getOtpFromBoxes();
            if (!TextUtils.isEmpty(code) && code.length() == 6) {
                loadingOverlay.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                buttonVerify.setEnabled(false);
                verifyPhoneNumberWithCode(verificationId, code);
            } else {
                Toast.makeText(OTPActivity.this,
                        "Please enter valid OTP", Toast.LENGTH_SHORT).show();
            }
        });

        textViewResend.setOnClickListener(v -> resendVerificationCode());
        startResendTimer();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override public void onVerificationCompleted(
                    @NonNull PhoneAuthCredential credential) { /* auto */ }

            @Override public void onVerificationFailed(
                    @NonNull FirebaseException e) {
                Toast.makeText(OTPActivity.this,
                        "Verification Failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override public void onCodeSent(
                    @NonNull String newVerificationId,
                    @NonNull PhoneAuthProvider.ForceResendingToken newToken) {
                verificationId = newVerificationId;
                resendToken    = newToken;
                Toast.makeText(OTPActivity.this,
                        "OTP Resent Successfully", Toast.LENGTH_SHORT).show();
                startResendTimer();
            }
        };
    }

    private void setupOtpInputs() {
        EditText[] editTexts = {otp1, otp2, otp3, otp4, otp5, otp6};
        for (int i = 0; i < editTexts.length; i++) {
            int finalI = i;
            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(
                        CharSequence s, int a, int b, int c) {}
                @Override public void onTextChanged(
                        CharSequence s, int a, int b, int c) {
                    if (!s.toString().isEmpty()
                            && finalI < editTexts.length - 1) {
                        editTexts[finalI + 1].requestFocus();
                    }
                }
                @Override public void afterTextChanged(Editable e) {}
            });
            editTexts[i].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_DEL
                        && editTexts[finalI].getText().toString().isEmpty()
                        && finalI > 0) {
                    editTexts[finalI - 1].requestFocus();
                }
                return false;
            });
        }
    }

    private String getOtpFromBoxes() {
        return otp1.getText().toString().trim()
                + otp2.getText().toString().trim()
                + otp3.getText().toString().trim()
                + otp4.getText().toString().trim()
                + otp5.getText().toString().trim()
                + otp6.getText().toString().trim();
    }

    private void verifyPhoneNumberWithCode(String vid, String code) {
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(vid, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(
            PhoneAuthCredential credential) {
        FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    loadingOverlay.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        DatabaseReference ref = FirebaseDatabase
                                .getInstance().getReference("Users");

                        Query q = ref.orderByChild("phoneNumber")
                                .equalTo(phoneNumber);
                        q.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override public void onDataChange(
                                            @NonNull DataSnapshot snapshot) {
                                        Intent i;
                                        if (snapshot.exists()
                                                && snapshot.getChildrenCount() > 0) {

                                            i = new Intent(
                                                    OTPActivity.this,
                                                    MainActivity.class);
                                        } else {

                                            i = new Intent(
                                                    OTPActivity.this,
                                                    SignupActivity.class);
                                        }
                                        i.putExtra("phoneNumber", phoneNumber);
                                        startActivity(i);
                                        finish();
                                    }
                                    @Override public void onCancelled(
                                            @NonNull DatabaseError error) {
                                        Toast.makeText(OTPActivity.this,
                                                "DB error: " + error.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(OTPActivity.this,
                                "Verification Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resendVerificationCode() {
        PhoneAuthOptions options = PhoneAuthOptions
                .newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(resendToken)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startResendTimer() {
        countDownTimer = new CountDownTimer(
                timeLeftInMillis, 1000) {
            @Override public void onTick(long millis) {
                timeLeftInMillis = millis;
                textViewResend.setVisibility(View.GONE);
            }
            @Override public void onFinish() {
                textViewResend.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}
