package khushi.example.greenmarket.ui.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import khushi.example.greenmarket.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import example.greenmarket.ui.adapters.CategoryAdapter;
import khushi.example.greenmarket.ui.model.CategoryModel;

public class CategoryFragment extends Fragment {

    private RecyclerView recyclerCategory;

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerCategory = view.findViewById(R.id.recyclerCategory);
        recyclerCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));

        List<CategoryModel> categoryList = new ArrayList<>();
        categoryList.add(new CategoryModel("Cereals", R.drawable.cereals));
        categoryList.add(new CategoryModel("Vegetables", R.drawable.vegetables));
        categoryList.add(new CategoryModel("Fruits", R.drawable.fruits));
        categoryList.add(new CategoryModel("Pulses", R.drawable.pulses));
        categoryList.add(new CategoryModel("Spices", R.drawable.spices));
        categoryList.add(new CategoryModel("Dry Fruits", R.drawable.dryfruits));

        CategoryAdapter adapter = new CategoryAdapter(getContext(), categoryList);
        recyclerCategory.setAdapter(adapter);

        return view;
    }
}
