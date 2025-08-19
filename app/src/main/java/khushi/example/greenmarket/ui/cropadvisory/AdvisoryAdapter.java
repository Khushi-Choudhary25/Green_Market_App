package khushi.example.greenmarket.ui.cropadvisory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import khushi.example.greenmarket.R;

public class AdvisoryAdapter extends RecyclerView.Adapter<AdvisoryAdapter.AdviceViewHolder> {

    private List<String> adviceList;

    public AdvisoryAdapter(List<String> adviceList) {
        this.adviceList = adviceList;
    }

    @NonNull
    @Override
    public AdviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_advice, parent, false);
        return new AdviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdviceViewHolder holder, int position) {
        String advice = adviceList.get(position);
        holder.adviceText.setText((position + 1) + ". " + advice);
    }

    @Override
    public int getItemCount() {
        return adviceList.size();
    }

    static class AdviceViewHolder extends RecyclerView.ViewHolder {
        TextView adviceText;

        AdviceViewHolder(@NonNull View itemView) {
            super(itemView);
            adviceText = itemView.findViewById(R.id.adviceText);
        }
    }
}
