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

public class FruitsSubcategoryActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits_subcategory);

        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);


        backButton.setOnClickListener(v -> onBackPressed());

        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Seb", R.drawable.seb));
        items.add(new GridItem("Kela", R.drawable.kela));
        items.add(new GridItem("Aam", R.drawable.aam));
        items.add(new GridItem("Santra", R.drawable.santra));
        items.add(new GridItem("Papita", R.drawable.papita));
        items.add(new GridItem("Anaar", R.drawable.anaar));
        items.add(new GridItem("Angoor", R.drawable.angoor));
        items.add(new GridItem("Nimbu", R.drawable.nimbu));
        items.add(new GridItem("Tarbooj", R.drawable.tarbooj));
        items.add(new GridItem("Kharbooja", R.drawable.kharbooja));
        items.add(new GridItem("Amrood", R.drawable.amrood));
        items.add(new GridItem("Litchi", R.drawable.litchi));
        items.add(new GridItem("Jamun", R.drawable.jamun));
        items.add(new GridItem("Nariyal", R.drawable.nariyal));
        items.add(new GridItem("Bel", R.drawable.bel));

        GridAdapter adapter = new GridAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            GridItem selectedItem = (GridItem) adapterView.getItemAtPosition(position);

            if (selectedItem != null && selectedItem.getName() != null) {
                String subcategory = selectedItem.getName().trim();
                String category = "Fruits";

                Log.d("SubcategoryClick", "Sending subcategory: " + subcategory);
                Log.d("SubcategoryClick", "Sending category: " + category);

                Intent intent = new Intent(FruitsSubcategoryActivity.this, StaticProductsActivity.class);
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
