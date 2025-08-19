package khushi.example.greenmarket.ui.adapters;
import khushi.example.greenmarket.ui.subcategories.GridItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import khushi.example.greenmarket.R;

public class DryFruitAdapter extends RecyclerView.Adapter<DryFruitAdapter.ViewHolder> {

    Context context;
    String[] names;
    int[] images;

    public DryFruitAdapter(Context context, String[] names, int[] images) {
        this.context = context;
        this.names = names;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dryFruitName.setText(names[position]);
        holder.dryFruitImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dryFruitName;
        ImageView dryFruitImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dryFruitName = itemView.findViewById(R.id.name);
            dryFruitImage = itemView.findViewById(R.id.image);
        }
    }
}