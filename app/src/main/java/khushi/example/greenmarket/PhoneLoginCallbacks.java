package khushi.example.greenmarket;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneLoginCallbacks extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

    private final Activity activity;

    public PhoneLoginCallbacks(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(activity, "Phone Login Successful", Toast.LENGTH_SHORT).show();
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "Phone Login Failed: " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onVerificationFailed(@NonNull FirebaseException e) {
        Toast.makeText(activity, "Verification Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
        Toast.makeText(activity, "OTP Sent! Please wait for verification.", Toast.LENGTH_SHORT).show();
    }
}
