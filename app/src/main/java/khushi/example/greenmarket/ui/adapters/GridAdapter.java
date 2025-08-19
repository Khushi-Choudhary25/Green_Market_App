package khushi.example.greenmarket.ui.adapters;

import khushi.example.greenmarket.ui.subcategories.GridItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import khushi.example.greenmarket.R;

public class GridAdapter extends BaseAdapter {

    Context context;
    List<GridItem> items;

    public GridAdapter(Context context, List<GridItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);

        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.name);

        GridItem item = items.get(position);
        imageView.setImageResource(item.getImageResId());
        textView.setText(item.getName());

        return view;
    }
}
