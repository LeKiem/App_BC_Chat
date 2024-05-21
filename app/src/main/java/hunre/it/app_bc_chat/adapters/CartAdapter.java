package hunre.it.app_bc_chat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


import hunre.it.app_bc_chat.Domain.ItemsDomain;
import hunre.it.app_bc_chat.Domain.SanPhamUser;
import hunre.it.app_bc_chat.Helper.ChangeNumberItemsListener;
import hunre.it.app_bc_chat.Helper.ManagmentCart;
import hunre.it.app_bc_chat.databinding.ViewholderCartBinding;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    private ArrayList<SanPhamUser> listItemSelected;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private ManagmentCart managmentCart;

    public CartAdapter(ArrayList<SanPhamUser> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.managmentCart = new ManagmentCart(context);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        SanPhamUser item = listItemSelected.get(position);

        holder.binding.titleTxt.setText(item.getTitle());

        // Convert giaGoc from String to double
        double giaGoc = 0;
        try {
            giaGoc = Double.parseDouble(item.getGiaGoc());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        holder.binding.feeEachitem.setText(String.format("%.0f.000đ", giaGoc));
        holder.binding.totalEachItem.setText(String.format("%.0f.000đ", giaGoc * item.getNumberinCart()));
        holder.binding.numberTxt.setText(String.valueOf(item.getNumberinCart()));

        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop());

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl())
                .apply(requestOptions)
                .into(holder.binding.pic);

        holder.binding.plusCarBtn.setOnClickListener(v -> managmentCart.plusItem(listItemSelected, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.changed();
        }));

        holder.binding.minusBnt.setOnClickListener(v -> managmentCart.minusItem(listItemSelected, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.changed();
        }));
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;

        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
