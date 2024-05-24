package hunre.it.app_bc_chat.activies.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import hunre.it.app_bc_chat.Fragment.Admin.FragmentTrungGian;
import hunre.it.app_bc_chat.Fragment.Admin.QlTinTuc;
import hunre.it.app_bc_chat.Fragment.Admin.ThongTinQuan;
import hunre.it.app_bc_chat.Fragment.Admin.QLSanPham;
import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.adapters.Admin.AdapterViewPager2;
import hunre.it.app_bc_chat.databinding.ActivityHomeAdminBinding;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class HomeAdminActivity extends AppCompatActivity {


    private ActivityHomeAdminBinding binding;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeAdminBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        loadFragments();
        binding.container.setCurrentItem(0);
        binding.menu.setItemSelected(R.id.qlttb, true);
    }

    private void loadFragments() {

        // nạp fragment vào viewpager tại activity hiện hành
        fragments.add(new QLSanPham());
        fragments.add(new QlTinTuc());
        fragments.add(new ThongTinQuan());
        AdapterViewPager2 adapterViewPager2 = new AdapterViewPager2(this, fragments);
        binding.container.setAdapter(adapterViewPager2);
        // nút button navigation sẽ thay đổi theo trang
        binding.container.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.menu.setItemSelected(R.id.qlttb, true);
                        break;
                    case 1:
                        binding.menu.setItemSelected(R.id.qltt, true);
                        break;
                    case 2:
                        binding.menu.setItemSelected(R.id.qluser, true);
                        break;
                }
                super.onPageSelected(position);
            }
        });
        // nạp viewpager2 vào bottom navigation
        binding.menu.setOnItemSelectedListener(i -> {
            switch (i) {
                case R.id.qlttb:
                    binding.container.setCurrentItem(0);
                    break;
                case R.id.qltt:
                    binding.container.setCurrentItem(1);
                    break;
                case R.id.qluser:
                    binding.container.setCurrentItem(2);
                    break;
            }
        });

    }
//    private void loadUser() {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
//        reference
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        // lấy dl từ firebase để hiển thị lên view
//                        String tenQuan = "" + snapshot.child("tenQuan").getValue();
//                        String sdt = "" + snapshot.child("sdt").getValue();
//                        String tinhTP = "" + snapshot.child("tinhTP").getValue();
//                        String quanHuyen = "" + snapshot.child("quanHuyen").getValue();
//                        String diaChi = "" + snapshot.child("diaChi").getValue();
//                        String avatar = "" + snapshot.child("avatar").getValue();
//                        String diaChiTongHop = tinhTP + ", " + quanHuyen + ", " + diaChi;
//                        binding.textName.setText(tenQuan + " | " + sdt);
//                        binding.diaChi.setText(diaChiTongHop);
//                        try {
//                            Picasso.get().load(avatar).fit().centerCrop()
//                                    .placeholder(R.drawable.shopivhd)
//                                    .error(R.drawable.shopivhd)
//                                    .into(binding.avatar);
//                        } catch (Exception e) {
//                            binding.avatar.setImageResource(R.drawable.shopivhd);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//    }

    private  void loadUserDetails(){
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.sdt.setText(preferenceManager.getString(Constants.SDT));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
    private  void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private  void  updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }
    private  void showToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}