package hunre.it.app_bc_chat.activies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import hunre.it.app_bc_chat.Helper.ManagmentCart;
import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.adapters.CartAdapter;
import hunre.it.app_bc_chat.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity1 {
    ActivityCartBinding binding;

    private double tax;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);

        calculatorCart();
        setValiable();
        initCartList();
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.srcollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.srcollViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setValiable() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totlaFeeTxt.setText(itemTotal + "00đ");
        binding.taxTxt.setText( tax  + "000đ");
        binding.deliveryTxt.setText( delivery  + "00đ");
        binding.totalTxt.setText(total  + "00đ");
    }
}