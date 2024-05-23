package hunre.it.app_bc_chat.activies.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import hunre.it.app_bc_chat.Domain.SanPhamUser;
import hunre.it.app_bc_chat.Helper.ManagmentCart;
import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.activies.BaseActivity1;
import hunre.it.app_bc_chat.adapters.CartAdapter;
import hunre.it.app_bc_chat.databinding.ActivityCartBinding;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class CartActivity extends BaseActivity1 {
    private ActivityCartBinding binding;
    private ArrayList<SanPhamUser> gioHangArrayList = new ArrayList<>();
    private ManagmentCart managmentCart;
    private PreferenceManager preferenceManager;
    private double tax;
    private double tongHoaDon;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String diaChi, tenToi, sdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        preferenceManager = new PreferenceManager(this); // Initialize preferenceManager
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        diaChi = prefs.getString("diaChi", "null");
        tenToi = prefs.getString("tenToi", "null");
        sdt = prefs.getString("sdtToi", "null");

        initCartList();
        calculatorCart();
        setValiable();

        binding.btnCheckout.setOnClickListener(v -> datHang());
    }

    private void initCartList() {
        gioHangArrayList = managmentCart.getListCart();
        if (gioHangArrayList.isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.srcollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.srcollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(gioHangArrayList, this, () -> calculatorCart()));
    }

    private void setValiable() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        double totalFee = managmentCart.getTotalFee();
        tax = Math.round((totalFee * percentTax) * 100.0) / 100.0;
        tongHoaDon = Math.round((totalFee + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(totalFee * 100) / 100;

        binding.totlaFeeTxt.setText(itemTotal + "00đ");
        binding.taxTxt.setText(tax + "000đ");
        binding.deliveryTxt.setText(delivery + "00đ");
        binding.totalTxt.setText(tongHoaDon + "00đ");
    }

    private void datHang() {
        progressDialog.setMessage("Đang lập hóa đơn");
        progressDialog.show();

        final String timeStamp = "" + System.currentTimeMillis();
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        String tienHoaDon = String.valueOf(tongHoaDon).replace("đ", "");

        // Prepare order data
        HashMap<String, String> orderData = new HashMap<>();
        orderData.put("maHd", timeStamp);
        orderData.put("ngayDat", timeStamp);
        orderData.put("tongHd", tienHoaDon);
        orderData.put("uid_khachHang", userId);
        orderData.put("sdtKhachHang", sdt);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonHang");
        reference.child(timeStamp).setValue(orderData).addOnSuccessListener(unused -> {
            // Add each product in the cart to the order
            for (SanPhamUser gioHang : gioHangArrayList) {
                String maSp = gioHang.getMaSp();
                String tongGiaTienSP = String.valueOf(gioHang.getTongGiaTienSP());
                String tenSp = gioHang.getTitle();
                String soLuong = String.valueOf(gioHang.getNumberinCart());
                String anhSanPham = gioHang.getPicUrl();

                HashMap<String, String> productData = new HashMap<>();
                productData.put("maSp", maSp);
                productData.put("anhSanPham", anhSanPham);
                productData.put("tenSp", tenSp);
                productData.put("tongGiaTienSP", tongGiaTienSP);
                productData.put("soLuong", soLuong);
                productData.put("uid_khachHang", firebaseAuth.getUid());

                reference.child(timeStamp).child("SanPham").child(maSp).setValue(productData);
            }

            // Show success dialog and clear cart
            new AlertDialog.Builder(CartActivity.this, R.style.CustomAlertDialog)
                    .setMessage("Đặt món ăn thành công, bạn hãy kiểm tra hóa đơn nhé")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        managmentCart.clearCart(); // Clear the cart after successful order
                        progressDialog.dismiss();
                        onBackPressed();
                    })
                    .show();
        }).addOnFailureListener(e -> {
            // Show failure dialog
            progressDialog.dismiss();
            new AlertDialog.Builder(CartActivity.this, R.style.CustomAlertDialog)
                    .setMessage("Lập hóa đơn thất bại, lỗi: " + e.getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}
