package khushi.example.greenmarket.ui.subcategories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.home.CategoryActivity;

public class SubCategoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<SubCategoryModel> list;
    String parentCategoryName;


    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModel> list, String parentCategoryName) {
        this.context = context;
        this.list = list;
        this.parentCategoryName = parentCategoryName;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_subcategory, parent, false);
        }

        ImageView imageView = view.findViewById(R.id.subcategoryImage);
        TextView textView = view.findViewById(R.id.subcategoryName);

        SubCategoryModel subcategory = list.get(i);

        imageView.setImageResource(subcategory.getImageResId());
        textView.setText(subcategory.getName());


        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra("category_name", parentCategoryName);

            intent.putExtra("subcategory_name", subcategory.getName());

            context.startActivity(intent);
        });

        return view;
    }
}
