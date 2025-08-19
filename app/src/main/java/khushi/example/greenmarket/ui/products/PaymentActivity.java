package khushi.example.greenmarket.ui.products;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import khushi.example.greenmarket.R;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "PaymentActivity";

    private double totalAmount = 0;
    private String userPhone = "9999999999";

    private String name, address, contact;
    private Button btnOnlinePayment, btnCOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_f3OX883Bn4Ba2u");

        int quantity = getIntent().getIntExtra("quantity", 1);
        double pricePerUnit = getIntent().getDoubleExtra("pricePerUnit", 0);
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        contact = getIntent().getStringExtra("contact");

        totalAmount = quantity * pricePerUnit;

        btnOnlinePayment = findViewById(R.id.btnOnlinePayment);
        btnCOD = findViewById(R.id.btnCOD);

        btnOnlinePayment.setOnClickListener(view -> fetchUserPhoneAndStartPayment(checkout));
        btnCOD.setOnClickListener(view -> handleCOD());
    }

    private void fetchUserPhoneAndStartPayment(Checkout checkout) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .child("phone")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userPhone = snapshot.getValue(String.class);
                        }
                        startPayment(checkout);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Failed to fetch phone", error.toException());
                        startPayment(checkout);
                    }
                });
    }

    private void startPayment(Checkout checkout) {
        final AppCompatActivity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Green Market - " + name);
            options.put("description", "Order Payment");
            options.put("currency", "INR");

            int amountInPaise = (int) Math.round(totalAmount * 100);
            options.put("amount", amountInPaise);

            JSONObject prefill = new JSONObject();
            prefill.put("contact", contact);
            prefill.put("email", "customer@example.com");

            options.put("prefill", prefill);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e(TAG, "Razorpay Error", e);
            Toast.makeText(activity, "Payment error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCOD() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderId = FirebaseDatabase.getInstance().getReference().push().getKey();

        if (orderId == null) {
            Toast.makeText(this, "Failed to place order. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("name", name);
        orderData.put("address", address);
        orderData.put("contact", contact);
        orderData.put("totalAmount", totalAmount);
        orderData.put("paymentMode", "Cash on Delivery");
        orderData.put("orderStatus", "Pending");
        orderData.put("timestamp", System.currentTimeMillis());

        FirebaseDatabase.getInstance().getReference()
                .child("orders")
                .child(userId)
                .child(orderId)
                .setValue(orderData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Order placed successfully with COD!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save order: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_LONG).show();
        finish();
    }
}
