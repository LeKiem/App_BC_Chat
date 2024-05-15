package hunre.it.app_bc_chat.activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.activies.Admin.HomeAdminActivity;
import hunre.it.app_bc_chat.databinding.ActivitySplashBinding;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class Splash extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(() -> {
            if (!preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                startActivity(new Intent(getApplicationContext(), SingInActivity.class));
                finish();
            } else {
                kiemTra();
            }
        }, 1000);
    }

    private void kiemTra() {
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        if (userId != null) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            // Lấy giá trị của trường taiKhoan từ tài liệu người dùng
                            String taiKhoan = documentSnapshot.getString("taiKhoan");
                            if (taiKhoan != null && taiKhoan.equals("Admin")) {
                                // Người dùng là quản trị viên, chuyển hướng đến HomeAdminActivity
                                startActivity(new Intent(getApplicationContext(), HomeAdminActivity.class));
                            } else {
                                // Người dùng không phải là quản trị viên, chuyển hướng đến MainActivity
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            finish(); // Kết thúc Activity hiện tại sau khi chuyển hướng
                        } else {
                            showToast("Unable to sign in");
                        }
                    });
        } else {
            // Nếu không có ID người dùng, chuyển hướng đến hoạt động đăng nhập
            startActivity(new Intent(getApplicationContext(), SingInActivity.class));
            finish();
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
