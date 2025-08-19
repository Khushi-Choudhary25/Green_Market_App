package khushi.example.greenmarket.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.model.Product;

public class OrderFormActivity extends AppCompatActivity {

    TextView productInfoText;
    EditText quantityInput, nameInput, addressInput, contactInput;
    Button placeOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);

        productInfoText = findViewById(R.id.productInfoText);
        quantityInput = findViewById(R.id.quantityInput);
        nameInput = findViewById(R.id.nameInput);
        addressInput = findViewById(R.id.addressInput);
        contactInput = findViewById(R.id.contactInput);
        placeOrderBtn = findViewById(R.id.placeOrderBtn);

        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            productInfoText.setText("Order for: " + product.getDescription());
        }

        placeOrderBtn.setOnClickListener(v -> {
            String qty = quantityInput.getText().toString().trim();
            String name = nameInput.getText().toString().trim();
            String address = addressInput.getText().toString().trim();
            String contact = contactInput.getText().toString().trim();

            if (qty.isEmpty()) {
                quantityInput.setError("Please enter quantity");
                return;
            }
            if (name.isEmpty()) {
                nameInput.setError("Please enter name");
                return;
            }
            if (address.isEmpty()) {
                addressInput.setError("Please enter address");
                return;
            }
            if (contact.isEmpty()) {
                contactInput.setError("Please enter contact number");
                return;
            }


            if (!contact.matches("\\d{10}")) {
                contactInput.setError("Enter valid 10-digit mobile number");
                return;
            }

            int qtyInt = Integer.parseInt(qty);
            double pricePerUnit = Double.parseDouble(product.getBasePrice());
            double totalAmount = qtyInt * pricePerUnit;

            Toast.makeText(this, "Order for " + qtyInt + "kg, total ₹" + totalAmount, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(OrderFormActivity.this, OrderSummaryActivity.class);
            intent.putExtra("quantity", qtyInt);
            intent.putExtra("pricePerUnit", pricePerUnit);
            intent.putExtra("totalAmount", totalAmount);
            intent.putExtra("productDescription", product.getDescription());
            intent.putExtra("name", name);
            intent.putExtra("address", address);
            intent.putExtra("contact", contact);
            startActivity(intent);
        });
    }
}
