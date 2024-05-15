package hunre.it.app_bc_chat.service;

import android.annotation.SuppressLint;
import android.widget.Filter;

import java.util.ArrayList;

import hunre.it.app_bc_chat.adapters.Admin.AdapterSanPham;
import hunre.it.app_bc_chat.adapters.Admin.AdapterTinTuc;
import hunre.it.app_bc_chat.models.SanPham;
import hunre.it.app_bc_chat.models.TinTuc;

public class TimKiemTT extends Filter {

    private final AdapterTinTuc adapter;
    private final ArrayList<TinTuc> tinTucs;

    public TimKiemTT(AdapterTinTuc adapter, ArrayList<TinTuc> filterList) {
        this.adapter = adapter;
        this.tinTucs = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){

            constraint = constraint.toString().toUpperCase();
            ArrayList<TinTuc> result = new ArrayList<>();
            for (int i=0; i<tinTucs.size(); i++){
                if (tinTucs.get(i).getTenTT().toUpperCase().contains(constraint)){
//                    if (sanPhams.get(i).getCoGiamGia().equals("true")) {
//
//                    }
                    result.add(tinTucs.get(i));
                }
            }
            results.count = result.size();
            results.values = result;
        }
        else {
            results.count = tinTucs.size();
            results.values = tinTucs;
        }
        return results;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.tinTucs = (ArrayList<TinTuc>) results.values;
        adapter.notifyDataSetChanged();
    }
}
