package hunre.it.app_bc_chat.adapters.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import hunre.it.app_bc_chat.Domain.TinTucDomain;
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

        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop());

        Glide.with(context)
                .load(item.getHinhAnh())
                .apply(requestOptions)
                .into(holder.binding.pic);

//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, DetailActivity.class);
//            intent.putExtra("object", String.valueOf(item));
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        final ViewholderPopListNewBinding binding;

        public Viewholder(ViewholderPopListNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
