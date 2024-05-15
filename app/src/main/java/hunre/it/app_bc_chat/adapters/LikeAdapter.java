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

import hunre.it.app_bc_chat.Domain.LikesDomain;
import hunre.it.app_bc_chat.databinding.ViewholderPopListLikeBinding;


public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.Viewholder> {
    ArrayList<LikesDomain> items;
    Context context;


    public LikeAdapter(ArrayList<LikesDomain> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderPopListLikeBinding binding = ViewholderPopListLikeBinding.
                inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.binding.title.setText(items.get(position).getTitle());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions=requestOptions.transform(new CenterCrop());

        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends  RecyclerView.ViewHolder{
        ViewholderPopListLikeBinding binding;

        public Viewholder(ViewholderPopListLikeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
