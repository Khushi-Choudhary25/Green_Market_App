package khushi.example.greenmarket.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import khushi.example.greenmarket.R;
import khushi.example.greenmarket.ui.model.AddressModel;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private ArrayList<AddressModel> list;
    private int selectedPosition = 0;
    Context context;

    public AddressAdapter(ArrayList<AddressModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public AddressModel getSelectedAddress() {
        if (list != null && !list.isEmpty() && selectedPosition >= 0 && selectedPosition < list.size()) {
            return list.get(selectedPosition);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressModel model = list.get(position);
        holder.name.setText(model.getName());
        holder.address.setText(model.getFullAddress());
        holder.phone.setText(model.getPhone());

        holder.radio.setChecked(position == selectedPosition);
        holder.radio.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, phone;
        RadioButton radio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            address = itemView.findViewById(R.id.txtAddress);
            phone = itemView.findViewById(R.id.txtPhone);
            radio = itemView.findViewById(R.id.radioSelect);
        }
    }
}
