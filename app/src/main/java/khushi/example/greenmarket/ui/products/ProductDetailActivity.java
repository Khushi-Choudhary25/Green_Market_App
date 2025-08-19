package khushi.example.greenmarket.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.List;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.adapters.ImageAdapter;
import khushi.example.greenmarket.ui.model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    ViewPager2 imageSlider;
    WormDotsIndicator dotsIndicator;

    TextView name, price, quantity, category, farmingType, subcategory, address, expectedDelivery;
    ImageAdapter imageAdapter;

    Button orderNowBtn;
    Product product;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imageSlider = findViewById(R.id.imageSlider);
        dotsIndicator = findViewById(R.id.dotsIndicator);

        name = findViewById(R.id.nameText);
        price = findViewById(R.id.priceText);
        quantity = findViewById(R.id.quantityText);
        category = findViewById(R.id.categoryText);
        farmingType = findViewById(R.id.farmingTypeText);
        subcategory = findViewById(R.id.subcategoryText);
        address = findViewById(R.id.addressText);
        expectedDelivery = findViewById(R.id.deliveryText);
        orderNowBtn = findViewById(R.id.orderNowButton);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            List<String> imageUrls = product.getImageUrls();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                imageAdapter = new ImageAdapter(imageUrls);
                imageSlider.setAdapter(imageAdapter);
                dotsIndicator.setViewPager2(imageSlider);
            }

            name.setText(product.getDescription());
            price.setText("₹" + product.getBasePrice() + " / " + product.getUnit());
            quantity.setText(product.getQuantity() + " " + product.getUnit());
            category.setText(product.getCategory());
            farmingType.setText(product.getFarmingType());
            subcategory.setText(product.getSubcategory());
            address.setText(product.getAddress());
            expectedDelivery.setText(product.getExpectedDelivery() + " days");
        }

        orderNowBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, OrderFormActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
    }
}
