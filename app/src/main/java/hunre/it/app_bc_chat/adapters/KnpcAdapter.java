package hunre.it.app_bc_chat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import hunre.it.app_bc_chat.Domain.KnpcDomain;
import hunre.it.app_bc_chat.databinding.ViewholderKnpcBinding;


public class KnpcAdapter extends RecyclerView.Adapter<KnpcAdapter.Viewholder> {
    ArrayList<KnpcDomain> items;
    Context context;

    public KnpcAdapter(ArrayList<KnpcDomain> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderKnpcBinding binding = ViewholderKnpcBinding.inflate(LayoutInflater.from(context), parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.binding.nameTxt.setText(items.get(position).getTitle());
        holder.binding.desTxt.setText(items.get(position).getDescription());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends  RecyclerView.ViewHolder{
        ViewholderKnpcBinding binding;
        public Viewholder(ViewholderKnpcBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
