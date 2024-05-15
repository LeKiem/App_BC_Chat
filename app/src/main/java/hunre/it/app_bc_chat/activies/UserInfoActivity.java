package hunre.it.app_bc_chat.activies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.activies.Admin.HomeAdminActivity;
import hunre.it.app_bc_chat.activies.User.InfoActivity;
import hunre.it.app_bc_chat.databinding.ActivityUserInfoBinding;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class UserInfoActivity extends BaseActivity1 {
    ActivityUserInfoBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigate();
    }
    private void bottomNavigate() {
        binding.tintucBtn.setOnClickListener(v -> startActivity(new Intent( UserInfoActivity.this, TintucActivity.class)));
        binding.homeBtn.setOnClickListener(v -> startActivity(new Intent( UserInfoActivity.this, MainActivity.class)));
        binding.KNBtn.setOnClickListener(v -> startActivity(new Intent(UserInfoActivity.this, KNActivity.class)));
        binding.BtnUser.setOnClickListener(v -> startActivity(new Intent(UserInfoActivity.this, UserInfoActivity.class)));
        binding.btnChat.setOnClickListener(v -> startActivity(new Intent(UserInfoActivity.this, ChatMainActivity.class)));

        binding.btnCart.setOnClickListener(v -> startActivity(new Intent(UserInfoActivity.this, CartActivity.class)));
        binding.btnInfo.setOnClickListener(v -> startActivity(new Intent(UserInfoActivity.this, HomeAdminActivity.class)));
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnLogout.setOnClickListener(v -> signOut());
    }

    private void confirmExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle("Xác nhận đăng xuất");
        builder.setMessage("Bạn có muốn thoát khỏi chương trình không?");

        builder.setPositiveButton("Có", (dialogInterface, i) -> signOut());

        builder.setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void  signOut() {
        showToast("Đang đăng xuất...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String , Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SingInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Đăng xuất thất bại"));
    }
    private  void showToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}