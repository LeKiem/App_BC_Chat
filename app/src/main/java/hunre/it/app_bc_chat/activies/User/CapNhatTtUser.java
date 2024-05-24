package hunre.it.app_bc_chat.activies.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import hunre.it.app_bc_chat.databinding.ActivityCapNhatTtUserBinding;
import hunre.it.app_bc_chat.databinding.ActivityUserInfoBinding;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class CapNhatTtUser extends AppCompatActivity {

    private ActivityCapNhatTtUserBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCapNhatTtUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        db = FirebaseFirestore.getInstance();

        // Hiển thị thông tin người dùng hiện tại nếu có
        loadUserDetails();

        // Xử lý sự kiện khi nhấn nút Save
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
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

                            binding.editTextName.setText(name);
                            binding.editTextEmail.setText(email);
                            binding.editTextDiaChi.setText(diaChi);
                            binding.editTextSdt.setText(sdt);
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
//    private void displayCurrentUserInfo() {
//        // Hiển thị thông tin người dùng hiện tại nếu có
//        String name = preferenceManager.getString(Constants.KEY_NAME);
//        String email = preferenceManager.getString(Constants.KEY_EMAIL);
//        String diaChi = preferenceManager.getString(Constants.DiaChi);
//        String sdt = preferenceManager.getString(Constants.SDT);
//
//        binding.editTextName.setText(name);
//        binding.editTextEmail.setText(email);
//        binding.editTextDiaChi.setText(diaChi);
//        binding.editTextSdt.setText(sdt);
//    }

    private void updateUserInfo() {
        String name = binding.editTextName.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String diaChi = binding.editTextDiaChi.getText().toString().trim();
        String sdt = binding.editTextSdt.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name)) {
            binding.editTextName.setError("Vui lòng điền tên");
            binding.editTextName.requestFocus();
            return;
        }

        // Lưu thông tin người dùng mới vào Firestore Database
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        DocumentReference userRef = db.collection(Constants.KEY_COLLECTION_USERS).document(userId);

        userRef.update(
                        Constants.KEY_NAME, name,
                        Constants.KEY_EMAIL, email,
                        Constants.DiaChi, diaChi,
                        Constants.SDT, sdt)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Cập nhật thông tin thành công
                            Toast.makeText(CapNhatTtUser.this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                            // Cập nhật thông tin trong SharedPreferences
                            preferenceManager.putString(Constants.KEY_NAME, name);
                            preferenceManager.putString(Constants.KEY_EMAIL, email);
                            preferenceManager.putString(Constants.DiaChi, diaChi);
                            preferenceManager.putString(Constants.SDT, sdt);
                            // Đóng UserInfoActivity và quay lại InfoActivity
                            finish();
                        } else {
                            // Xử lý khi cập nhật thông tin thất bại
                            Toast.makeText(CapNhatTtUser.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
