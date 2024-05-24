package hunre.it.app_bc_chat.activies.Admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.activies.ChatMainActivity;
import hunre.it.app_bc_chat.databinding.ActivityCapNhatSpBinding;
import hunre.it.app_bc_chat.databinding.ActivityCapNhatTtBinding;
import hunre.it.app_bc_chat.databinding.ActivityTintucBinding;

public class CapNhatTT extends AppCompatActivity {
    private ActivityCapNhatTtBinding binding;

    private String maTt;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private double gia_goc, gia_giam, tiLePhanTram;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCapNhatTtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        maTt = getIntent().getStringExtra("maTt");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this, R.style.CustomAlertDialog);
        progressDialog.setCanceledOnTouchOutside(false);
        loadSanPham(maTt);
        binding.capNhat.setOnClickListener(view1 -> checkData());
        binding.hinhAnh.setOnClickListener(view1 -> option());
    }

    private void loadSanPham(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TinTuc");
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String hinhAnh = "" + snapshot.child("hinhAnh").getValue();
                String tenTt = "" + snapshot.child("tenTT").getValue();
                String moTa = "" + snapshot.child("moTa").getValue();
                Picasso.get().load(hinhAnh).fit().centerCrop()
                        .placeholder(R.drawable.item_tt_2)
                        .error(R.drawable.item_tt_2)
                        .into(binding.hinhAnh);
                binding.tenTt.setText(tenTt);
                binding.moTa.setText(moTa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void capNhat(String id) {
        // cập nhật khi món ăn đã có sẵn ảnh.
        if (image_uri == null) {
            progressDialog.setMessage("Đang cập nhật...");
            progressDialog.show();
            final String timestamp = ""+System.currentTimeMillis();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("tenTT", ""+tenTt);
            hashMap.put("moTa", ""+moTa);
            hashMap.put("timestamp", ""+timestamp);
            hashMap.put("uid", ""+firebaseAuth.getUid());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TinTuc");
            reference.child(id).updateChildren(hashMap).addOnSuccessListener(unused -> {
                progressDialog.dismiss();
                new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                        .setMessage("Cập nhật thành công")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            onBackPressed();
                            super.finish();
                        }).show();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                        .setMessage("Cập nhật thất bại, lỗi: " + e.getMessage())
                        .setPositiveButton("OK", null).show();
            });
        } else capNhatHinhAnh(id);

    }
    private void capNhatHinhAnh(String id) {
        progressDialog.setMessage("Đang cập nhật...");
        progressDialog.show();
        final String timestamp = ""+System.currentTimeMillis();
        String path = "hinh_anh_tin_tuc/" + "" + id;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(path);
        storageReference.putFile(image_uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
            while (!task.isSuccessful());
            Uri downloadImageUri = task.getResult();
            if (task.isSuccessful()) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("tenTT", ""+tenTt);
                hashMap.put("moTa", ""+moTa);
                hashMap.put("hinhAnh", ""+downloadImageUri);
                hashMap.put("timestamp", ""+timestamp);
                hashMap.put("uid", ""+firebaseAuth.getUid());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TinTuc");
                reference.child(id)
                        .updateChildren(hashMap)
                        .addOnSuccessListener(unused -> {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(getApplicationContext(), R.style.CustomAlertDialog)
                                    .setMessage("Cập nhật thành công")
                                    .setPositiveButton("ok", (dialogInterface, i) -> {
                                        onBackPressed();
                                        super.finish();
                                    }).show();
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(getApplicationContext(), R.style.CustomAlertDialog)
                                    .setMessage("Cập nhật thất bại, lỗi: " + e.getMessage())
                                    .setPositiveButton("OK", null).show();
                        });

            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            new AlertDialog.Builder(getApplicationContext(), R.style.CustomAlertDialog)
                    .setMessage("Cập nhật thất bại, lỗi: " + e.getMessage())
                    .setPositiveButton("OK", null).show();
        });
    }
//    private void confirmExit() {
//        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ChatMainActivity.this);
//        builder.setTitle("Xác nhận đăng xuất");
//        builder.setMessage("Bạn có muốn thoát khỏi chương trình không?");
//        builder.setPositiveButton("Có", (dialogInterface, i) -> signOut());
//        builder.setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss());
//
//        androidx.appcompat.app.AlertDialog dialog = builder.create();
//        dialog.show();
//    }
    private String tenTt, moTa;

    private void checkData() {
        tenTt = binding.tenTt.getText().toString().trim();
        moTa = binding.moTa.getText().toString().trim();
        if (tenTt.isEmpty()) {
            binding.tenTt.setError("Bạn không được bỏ trống tên của tin tuc");
            return;
        }
        if (moTa.isEmpty()) {
            binding.moTa.setError("Hãy viết noi dung của tin tức");
            return;
        }

        capNhat(maTt);
    }


    // chọn hình từ camera hoặc thư viện
    private void option() {
        String[] luaChon = {"Camera", "Thư viện"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh từ").setItems(luaChon, (dialog, which) -> {
            if (which == 0) {
                if (checkCameraPermission()) {
                    pickFromCamera();
                } else requestCameraPermission();
            } else {
                if (checkStoragePermission()) {
                    pickFromGallery();
                } else requestStoragePermission();
            }
        }).show();
    }
    // kiểm tra người dùng có cấp quyền truy cập camera hay chưa
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    // nếu chưa cấp quyền thì gửi yêu cầu
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }
    // chụp hình
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");
        image_uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }
    // kiểm tra người dùng có cấp quyền truy cập bộ nhớ hay chưa
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }
    // nếu chưa cấp quyền thì gửi yêu cầu
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    // lấy hình từ thư viện
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = Objects.requireNonNull(data).getData();
                binding.hinhAnh.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                binding.hinhAnh.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}