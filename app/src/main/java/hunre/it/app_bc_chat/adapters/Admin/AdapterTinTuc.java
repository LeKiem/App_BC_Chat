package hunre.it.app_bc_chat.adapters.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.activies.Admin.CapNhatTT;
import hunre.it.app_bc_chat.models.TinTuc;
import hunre.it.app_bc_chat.service.TimKiem;
import hunre.it.app_bc_chat.service.TimKiemTT;

public class AdapterTinTuc  extends RecyclerView.Adapter<AdapterTinTuc.HolderTinTucs> implements Filterable {

    private Context context;
    public ArrayList<TinTuc> tinTucs, kqTimKiem;
    private TimKiemTT timKiem;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private AlertDialog dialog;
//    private TrangChuKH trangChuKH;
    public String phiGiaoHang;

    public AdapterTinTuc(Context context, ArrayList<TinTuc> tinTucs) {
        this.context = context;
        this.tinTucs = tinTucs;
        this.kqTimKiem = tinTucs;
    }

    @NonNull
    @Override
    public HolderTinTucs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tin_tuc, parent, false);
        return new HolderTinTucs(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HolderTinTucs holder, int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        final TinTuc modelTinTuc = tinTucs.get(position);
        // get
        String tenTT = modelTinTuc.getTenTT();
        String hinhAnh = modelTinTuc.getHinhAnh();
        String moTa = modelTinTuc.getMoTa();
        String id = modelTinTuc.getMaTt();
        // set
        holder.tenTT.setText(tenTT);
        Picasso.get().load(hinhAnh).fit().centerCrop()
                .placeholder(R.drawable.item_tt_2)
                .error(R.drawable.item_tt_2)
                .into(holder.hinhAnh);
//        holder.moTa.setText(moTa);

        holder.itemView.setOnClickListener(view -> {
            dialogTinTuc(id, modelTinTuc);
        });

    }
    private void dialogTinTuc (String id, TinTuc tinTuc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        // dialog tin tuc
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_tt_tin_tuc, null);
        ImageView hinhAnh = view.findViewById(R.id.hinhAnh);
        TextView tenTT = view.findViewById(R.id.tenTt);
        TextView moTa = view.findViewById(R.id.moTa);
        ImageButton capNhat = view.findViewById(R.id.capNhat);
        ImageButton xoa = view.findViewById(R.id.xoa);
        builder.setView(view);
        // Không cho phép khách hàng được thao tác lên món ăn
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String taiKhoan = "" + snapshot.child("taiKhoan").getValue();
                        if (taiKhoan.equals("")) {
                            xoa.setVisibility(View.GONE);
                            capNhat.setVisibility(View.GONE);
                        } else {
                            // xóa sản phẩm theo id
                            xoa.setVisibility(View.VISIBLE);
                            xoa.setOnClickListener(view1 -> xoa(id));
                            // cập nhật sản phẩm, chuyển sang activity khác, tôi không thể để adapter này chứa quá nhiều code được :D
                            capNhat.setVisibility(View.VISIBLE);
                            capNhat.setOnClickListener(view1 -> {
                                Intent intent = new Intent(context, CapNhatTT.class);
                                intent.putExtra("maTt", id);
                                context.startActivity(intent);
                                dialog.dismiss();
                            });
//                            themVaoGio.setVisibility(View.GONE);
//                            linear02.setVisibility(View.GONE);

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TinTuc");
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get từ csdl
                String uid = "" + snapshot.child("uid").getValue();
                //get từ model
                String uid02 = tinTuc.getUid();
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("TaiKhoan");
                reference1.child(uid02).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // lấy dl từ bảng users
                        String tenQuan = "" + snapshot.child("tenQuan").getValue();
                        String anhDaiDien = "" + snapshot.child("avatar").getValue();
                        phiGiaoHang = "" + snapshot.child("phiGiao").getValue();
                        // chia sẻ dữ liệu cho toàn bộ activity và fragment
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("tenQuan", tenQuan);
                        editor.putString("phiGiao", phiGiaoHang);
                        editor.apply();
                        // set data
//                        tenTT.setText(tenQuan);
//                        try {
//                            Picasso.get().load(anhDaiDien).fit().centerCrop()
//                                    .placeholder(R.drawable.shopivhd)
//                                    .error(R.drawable.shopivhd)
//                                    .into(avatar);
//                        } catch (Exception e) {
//                            avatar.setImageResource(R.drawable.shopivhd);
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String maTT = tinTuc.getMaTt();
        String anhTt = tinTuc.getHinhAnh();
        String ten = tinTuc.getTenTT();
        String moTaTt = tinTuc.getMoTa();

        Picasso.get().load(anhTt).fit().centerCrop()
                .placeholder(R.drawable.item_tt_2)
                .error(R.drawable.item_tt_2)
                .into(hinhAnh);
        tenTT.setText(ten);
        if(tenTT == null){
            System.out.println("Errrrrr");
        }
        else {
//            System.out.println("KOOO LOOOOi");
        }
        moTa.setText(moTaTt);

        dialog = builder.create();
        dialog.show();
    }
    private void xoa(String id) {
        // hiển thị dialog xác nhận xóa đã
        new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setTitle("Xóa tin tức")
                .setMessage("Bạn có muốn xóa tin tức này vĩnh viễn?")
                .setPositiveButton("Xóa", (dialogInterface, i) -> {
                    progressDialog.setMessage("Đang xóa...");
                    progressDialog.show();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TinTuc");
                    reference.child(id).removeValue().addOnSuccessListener(unused -> {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                                .setMessage("Xóa thành công")
                                .setPositiveButton("OK", (dialogInterface1, i1) -> dialog.dismiss()).show();
                    }).addOnFailureListener(e -> {
                        new AlertDialog.Builder(context, R.style.CustomAlertDialog)
                                .setMessage("Xóa thất bại, lỗi: " + e.getMessage())
                                .setPositiveButton("OK", null).show();
                    });
                    // không xóa
                }).setNegativeButton("Không", null).show();

    }
    @Override
    public int getItemCount() {
        return  tinTucs.size();
    }
    @Override
    public Filter getFilter() {
        if (timKiem == null) {
            timKiem = new TimKiemTT(AdapterTinTuc.this, kqTimKiem);
        }
        return timKiem;
    }

    public class HolderTinTucs extends RecyclerView.ViewHolder{
        private final TextView  tenTT, moTa;
        private final ImageView hinhAnh;
        public HolderTinTucs(@NonNull View itemView) {
            super(itemView);
            tenTT = itemView.findViewById(R.id.tenTt);
            moTa = itemView.findViewById(R.id.moTa);
            hinhAnh = itemView.findViewById(R.id.hinhAnh);
        }
    }
}
