package hunre.it.app_bc_chat.service;

import android.annotation.SuppressLint;
import android.widget.Filter;



import java.util.ArrayList;

import hunre.it.app_bc_chat.adapters.Admin.AdapterSanPham;
import hunre.it.app_bc_chat.models.SanPham;

public class TimKiem extends Filter {

    private final AdapterSanPham adapter;
    private final ArrayList<SanPham> sanPhams;

    public TimKiem(AdapterSanPham adapter, ArrayList<SanPham> filterList) {
        this.adapter = adapter;
        this.sanPhams = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){

            constraint = constraint.toString().toUpperCase();
            ArrayList<SanPham> result = new ArrayList<>();
            for (int i=0; i<sanPhams.size(); i++){
                if (sanPhams.get(i).getTitle().toUpperCase().contains(constraint)){
//                    if (sanPhams.get(i).getCoGiamGia().equals("true")) {
//
//                    }
                    result.add(sanPhams.get(i));
                }
            }
            results.count = result.size();
            results.values = result;
        }
        else {
            results.count = sanPhams.size();
            results.values = sanPhams;
        }
        return results;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.sanPhams = (ArrayList<SanPham>) results.values;
        adapter.notifyDataSetChanged();
    }
}
