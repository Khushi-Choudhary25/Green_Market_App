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

public class VegetablesSubcategoryActivity extends AppCompatActivity {

    GridView gridView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetables_subcategory);

        backButton = findViewById(R.id.backButton);
        gridView = findViewById(R.id.gridView);


        backButton.setOnClickListener(v -> onBackPressed());

        List<GridItem> items = new ArrayList<>();
        items.add(new GridItem("Aloo", R.drawable.aloo));
        items.add(new GridItem("Pyaaz", R.drawable.pyaaz));
        items.add(new GridItem("Tamatar", R.drawable.tamatar));
        items.add(new GridItem("Bhindi", R.drawable.bhindi));
        items.add(new GridItem("Baingan", R.drawable.baingan));
        items.add(new GridItem("Gobi", R.drawable.gobi));
        items.add(new GridItem("Patta Gobi", R.drawable.patta_gobi));
        items.add(new GridItem("Matar", R.drawable.matar));
        items.add(new GridItem("Lauki", R.drawable.lauki));
        items.add(new GridItem("Tinda", R.drawable.tinda));
        items.add(new GridItem("Karela", R.drawable.karela));
        items.add(new GridItem("Parwal", R.drawable.parwal));
        items.add(new GridItem("Torai", R.drawable.torai));
        items.add(new GridItem("Shimla Mirch", R.drawable.shimla_mirch));
        items.add(new GridItem("Hari Mirch", R.drawable.hari_mirch));
        items.add(new GridItem("Kaddu", R.drawable.kaddu));
        items.add(new GridItem("Palak", R.drawable.palak));
        items.add(new GridItem("Methi", R.drawable.methi));
        items.add(new GridItem("Sarso ka Saag", R.drawable.sarso_ka_saag));
        items.add(new GridItem("Shalgam", R.drawable.shalgam));

        GridAdapter adapter = new GridAdapter(this, items);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, id) -> {
            GridItem selectedItem = (GridItem) adapterView.getItemAtPosition(position);

            if (selectedItem != null && selectedItem.getName() != null) {
                String subcategory = selectedItem.getName().trim();
                String category = "Vegetables";

                Log.d("SubcategoryClick", "Sending subcategory: " + subcategory);
                Log.d("SubcategoryClick", "Sending category: " + category);

                Intent intent = new Intent(VegetablesSubcategoryActivity.this, StaticProductsActivity.class);
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
