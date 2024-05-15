package hunre.it.app_bc_chat.activies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import hunre.it.app_bc_chat.activies.Admin.HomeAdminActivity;
import hunre.it.app_bc_chat.databinding.ActivitySingInBinding;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class SingInActivity extends AppCompatActivity {

    private ActivitySingInBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivitySingInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }
    private  void setListeners() {
        binding.textCreateNewAccount.setOnClickListener( v ->
                startActivity( new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.btnSignIn.setOnClickListener( v -> {
            if(isValidSignInDetails()){
                signIn();
            }
        });
    }
//    private  void signIn(){
//        loading( true);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
//                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
//                .get()
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful() && task.getResult() != null
//                            && task.getResult().getDocumentChanges().size() > 0
//                    ){
//                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//                        preferenceManager.putBoolen(Constants.KEY_IS_SIGNED_IN, true);
//                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
//                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
//                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    } else {
//                        loading(false);
//                        showToast("Unable to sign in");
//                    }
//                });
//
//    }
private void signIn() {
    loading(true);
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    database.collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
            .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null
                        && !task.getResult().getDocuments().isEmpty()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    // Lấy giá trị của trường isAdmin từ tài liệu người dùng
                    String isAdmin = documentSnapshot.getString("taiKhoan");
                    if (isAdmin != null && isAdmin.equals("Admin")) {
                        // Người dùng là quản trị viên, chuyển hướng đến HomeAdminActivity
                        preferenceManager.putBoolen(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));

                        startActivity(new Intent(getApplicationContext(), HomeAdminActivity.class));
                    } else {
                        // Người dùng không phải là quản trị viên, chuyển hướng đến MainActivity
                        preferenceManager.putBoolen(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    finish(); // Kết thúc Activity hiện tại sau khi chuyển hướng
                } else {
                    loading(false);
                    showToast("Unable to sign in");
                }
            });
}

    private  void  loading (Boolean isLoading){
        if(isLoading){
            binding.btnSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnSignIn.setVisibility(View.VISIBLE);
        }
    }
    private  void showToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private  Boolean isValidSignInDetails(){
        if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Nhập email của bạn");
            return  false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Nhập mật emai của bạn");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Nhập mật khẩu của bạn");
            return  false;
        }else {
            return true;
        }
    }


}