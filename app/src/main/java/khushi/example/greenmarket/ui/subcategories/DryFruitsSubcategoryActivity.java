package khushi.example.greenmarket.ui.subcategories;
import khushi.example.greenmarket.ui.adapters.GridAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.products.StaticProductsActivity;

public class DryFruitsSubcategoryActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dry_fruits_subcategory);

        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);


        backButton.setOnClickListener(v -> onBackPressed());

        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Badam", R.drawable.badam));
        items.add(new GridItem("Akhrot", R.drawable.akhrot));
        items.add(new GridItem("Pista", R.drawable.pista));
        items.add(new GridItem("Kaju", R.drawable.kaju));
        items.add(new GridItem("Chilgoza", R.drawable.chilgoza));
        items.add(new GridItem("Makhana", R.drawable.makhana));
        items.add(new GridItem("Kishmish", R.drawable.kishmish));
        items.add(new GridItem("Munakka", R.drawable.munakka));
        items.add(new GridItem("Anjeer", R.drawable.anjeer));
        items.add(new GridItem("Sukha Nariyal", R.drawable.sukha_nariyal));
        items.add(new GridItem("Khajoor", R.drawable.khajoor));

        GridAdapter adapter = new GridAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            GridItem selectedItem = (GridItem) adapterView.getItemAtPosition(position);

            if (selectedItem != null && selectedItem.getName() != null) {
                String subcategory = selectedItem.getName().trim();
                String category = "Dry Fruits";

                Log.d("SubcategoryClick", "Sending subcategory: " + subcategory);
                Log.d("SubcategoryClick", "Sending category: " + category);

                Intent intent = new Intent(DryFruitsSubcategoryActivity.this, StaticProductsActivity.class);
                intent.putExtra("subcategory", subcategory);
                intent.putExtra("category", category);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Selected subcategory is null", Toast.LENGTH_SHORT).show();
                Log.e("SubcategoryClick", "selectedItem or name is null");
            }
        });
    }
}
