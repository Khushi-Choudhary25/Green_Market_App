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

public class PlantsSubcategoryActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_subcategory);

        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);


        backButton.setOnClickListener(v -> onBackPressed());

        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Gulab", R.drawable.gulab));
        items.add(new GridItem("Surajmukhi", R.drawable.surajmukhi));
        items.add(new GridItem("Giloy", R.drawable.giloy));
        items.add(new GridItem("Neem", R.drawable.neem));
        items.add(new GridItem("Aloe Vera", R.drawable.aloe_vera));
        items.add(new GridItem("Bamboo", R.drawable.bamboo));
        items.add(new GridItem("Ashwagandha", R.drawable.ashwagandha));
        items.add(new GridItem("Lemongrass", R.drawable.lemongrass));
        items.add(new GridItem("Curry Patta", R.drawable.curry_patta));
        items.add(new GridItem("Papita Paudha", R.drawable.papita_paudha));
        items.add(new GridItem("Amrood", R.drawable.amrood_podha));
        items.add(new GridItem("Aam", R.drawable.aam_podha));
        items.add(new GridItem("Madhukamini", R.drawable.madhukamini));
        items.add(new GridItem("kela", R.drawable.kela_podha));
        items.add(new GridItem("Eucalyptus", R.drawable.eucalyptus));
        items.add(new GridItem("Poplar", R.drawable.poplar));



        GridAdapter adapter = new GridAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            GridItem selectedItem = (GridItem) adapterView.getItemAtPosition(position);

            if (selectedItem != null && selectedItem.getName() != null) {
                String subcategory = selectedItem.getName().trim();
                String category = "Plants";

                Log.d("SubcategoryClick", "Sending subcategory: " + subcategory);
                Log.d("SubcategoryClick", "Sending category: " + category);

                Intent intent = new Intent(PlantsSubcategoryActivity.this, StaticProductsActivity.class);
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
