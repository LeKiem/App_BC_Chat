package hunre.it.app_bc_chat.adapters.User;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import hunre.it.app_bc_chat.Domain.TinTucDomain;
import hunre.it.app_bc_chat.activies.User.DetailTinTucActivity;
import hunre.it.app_bc_chat.databinding.ViewholderPopListLikeBinding;
import hunre.it.app_bc_chat.databinding.ViewholderPopListNewBinding;

public class AdapterTinTucPage1 extends RecyclerView.Adapter<AdapterTinTucPage1.Viewholder> {
    private final ArrayList<TinTucDomain> items;
    private Context context;

    public AdapterTinTucPage1(ArrayList<TinTucDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public AdapterTinTucPage1.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
//        ViewholderPopListLikeBinding binding = ViewholderPopListLikeBinding.inflate(
//                LayoutInflater.from(context), parent, false
//        );
        ViewholderPopListNewBinding binding = ViewholderPopListNewBinding.
                inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTinTucPage1.Viewholder holder, int position) {
        TinTucDomain item = items.get(position);
        holder.binding.desTxt.setText(item.getTenTT());
        holder.binding.tenTxt.setText(item.getMoTa());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions=requestOptions.transform(new CenterCrop());
        Glide.with(context)
                .load(items.get(position).getHinhAnh())
                .apply(requestOptions)
                .into(holder.binding.pic);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTinTucActivity.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public  class Viewholder extends RecyclerView.ViewHolder {
         ViewholderPopListNewBinding binding;

        public Viewholder(ViewholderPopListNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
