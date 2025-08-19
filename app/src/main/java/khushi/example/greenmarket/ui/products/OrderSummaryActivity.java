package khushi.example.greenmarket.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import khushi.example.greenmarket.R;

public class OrderSummaryActivity extends AppCompatActivity {

    TextView summaryText;
    Button placeOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        summaryText = findViewById(R.id.summaryText);
        placeOrderBtn = findViewById(R.id.placeOrderBtn);


        int quantity = getIntent().getIntExtra("quantity", 1);
        double pricePerUnit = getIntent().getDoubleExtra("pricePerUnit", 0);
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0);
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String contact = getIntent().getStringExtra("contact");

        String summary = "Name: " + name + "\n"
                + "Address: " + address + "\n"
                + "Contact: " + contact + "\n"
                + "Quantity: " + quantity + " kg\n"
                + "Price per unit: ₹" + pricePerUnit + "\n"
                + "Total Amount: ₹" + totalAmount;

        summaryText.setText(summary);

        placeOrderBtn.setOnClickListener(v -> {

            Intent intent = new Intent(OrderSummaryActivity.this, PaymentActivity.class);
            intent.putExtra("quantity", quantity);
            intent.putExtra("pricePerUnit", pricePerUnit);
            intent.putExtra("totalAmount", totalAmount);
            intent.putExtra("name", name);
            intent.putExtra("address", address);
            intent.putExtra("contact", contact);

            startActivity(intent);
        });
    }
}
