package hunre.it.app_bc_chat.activies.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import hunre.it.app_bc_chat.activies.BaseActivity1;
import hunre.it.app_bc_chat.activies.KNActivity;
import hunre.it.app_bc_chat.activies.MainActivity;
import hunre.it.app_bc_chat.activies.TintucActivity;
import hunre.it.app_bc_chat.activies.UserInfoActivity;
import hunre.it.app_bc_chat.databinding.ActivityInfoBinding;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        db = FirebaseFirestore.getInstance();
        loadUserDetails();
        setupButtonClicks();
    }
    private void setupButtonClicks() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở UserInfoActivity để cập nhật thông tin người dùng
                Intent intent = new Intent(InfoActivity.this, CapNhatTtUser.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload user details when returning from UserInfoActivity
        loadUserDetails();
    }

    private void loadUserDetails() {
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        db.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString(Constants.KEY_NAME);
                            String email = documentSnapshot.getString(Constants.KEY_EMAIL);
                            String diaChi = documentSnapshot.getString(Constants.DiaChi);
                            String sdt = documentSnapshot.getString(Constants.SDT);

                            binding.titleName.setText(name);
                            binding.titleEmail.setText(email);
                            binding.titleDc.setText(diaChi);
                            binding.titleSdt.setText(sdt);
                        } else {
                            // Xử lý trường hợp không tìm thấy thông tin người dùng
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi khi truy xuất dữ liệu không thành công
                    }
                });
    }
}