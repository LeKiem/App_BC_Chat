package hunre.it.app_bc_chat.activies;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import  hunre.it.app_bc_chat.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;


import hunre.it.app_bc_chat.databinding.ActivitySignUpBinding;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private  void setListeners() {
        binding.textSignIn.setOnClickListener( v ->onBackPressed());
        binding.btnSignUp.setOnClickListener(v -> {
            if(isValidSignUpDetails()){
                signUp();
            }
        });

        binding.layoutImage.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }
    private  void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private  void signUp() {
        loading( true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME , binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL , binding.inputEmail.getText().toString());
        user.put(Constants.SDT , binding.sDT.getText().toString());
        user.put(Constants.Tinh , binding.inputPassword.getText().toString());
        user.put(Constants.Huyen , binding.inputPassword.getText().toString());
        user.put(Constants.DiaChi , binding.inputPassword.getText().toString());
        user.put(Constants.KEY_PASSWORD , binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE , encodedImage);
        user.put(Constants.TaiKhoan, "KhachHang");
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolen(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                })
                .addOnFailureListener(e -> {
                    loading(false);
                    showToast(e.getMessage());
                });
        reference
                .setValue(user)
                .addOnSuccessListener(unused -> {
                    loading(false);
                    Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    loading(false);
                    new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                            .setTitle("Lưu thông tin")
                            .setMessage("Không thể lưu thông tin, lý do: " + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                });


    }

//    private  void signUp() {
//        loading( true);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String, Object> user = new HashMap<>();
//        user.put(Constants.KEY_NAME , binding.inputName.getText().toString());
//        user.put(Constants.KEY_EMAIL , binding.inputEmail.getText().toString());
//        user.put(Constants.KEY_PASSWORD , binding.inputPassword.getText().toString());
////        user.put(Constants.SDT)
//        user.put(Constants.KEY_IMAGE , encodedImage);
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .add(user)
//                .addOnSuccessListener(documentReference -> {
//                    loading(false);
//                    preferenceManager.putBoolen(Constants.KEY_IS_SIGNED_IN, true);
//                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
//                    preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
//                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//
//                })
//                .addOnFailureListener(e -> {
//                    loading(false);
//                    showToast(e.getMessage());
//                });
//    }
    private  String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private  final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage  = encodeImage(bitmap);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails(){

        if(encodedImage == null){
            showToast("Chọn ảnh của bạn");
            return  false;
        } else if (binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Vui lòng nhập tên của bạn : ");
            return  false;
        }  else if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Vui lòng nhập email của bạn: ");
            return  false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Chọn ảnh của bạn: ");
            return false;

        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập mật khẩu");
            return  false;
        } else if (binding.inputConfirmPassword.getText().toString().isEmpty()) {
            showToast("Vui lòng nhập lại mật khẩu của bạn");
            return  false;

        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Mật khẩu hoặc mật khẩu xác nhận chưa chính xác vui lòng nhập lại ");
            return  false;
        }else {
            return true;
        }

    }

    private  void  loading (Boolean isLoading){
        if(isLoading){
            binding.btnSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnSignUp.setVisibility(View.VISIBLE);
        }
    }
}