package khushi.example.greenmarket.ui.subcategories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.adapters.GridAdapter;
import khushi.example.greenmarket.ui.subcategories.GridItem;
import khushi.example.greenmarket.ui.products.StaticProductsActivity;

public class CerealsSubcategoryActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cereals_subcategory);

        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);

        backButton.setOnClickListener(v -> onBackPressed());

        List<GridItem> items = getCerealSubcategories();

        GridAdapter adapter = new GridAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            GridItem selectedItem = (GridItem) adapterView.getItemAtPosition(position);

            if (selectedItem != null && selectedItem.getName() != null) {
                String subcategory = selectedItem.getName().trim();
                String category = "Cereals";

                Log.d("SubcategoryClick", "Selected: " + category + " > " + subcategory);

                Intent intent = new Intent(CerealsSubcategoryActivity.this, StaticProductsActivity.class);
                intent.putExtra("category", category);
                intent.putExtra("subcategory", subcategory);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid subcategory selected.", Toast.LENGTH_SHORT).show();
                Log.e("SubcategoryClick", "selectedItem or name is null");
            }
        });
    }

    private List<GridItem> getCerealSubcategories() {
        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Gehu", R.drawable.gehu));
        items.add(new GridItem("Chawal", R.drawable.chawal));
        items.add(new GridItem("Makka", R.drawable.makka));
        items.add(new GridItem("Bajra", R.drawable.bajra));
        items.add(new GridItem("Jowar", R.drawable.jowar));
        items.add(new GridItem("Jau", R.drawable.jau));
        items.add(new GridItem("Ragi", R.drawable.ragi));
        items.add(new GridItem("Sama", R.drawable.sama));
        items.add(new GridItem("Kodo", R.drawable.kodo));
        items.add(new GridItem("Kangni", R.drawable.kangni));
        return items;
    }
}
