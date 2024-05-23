package hunre.it.app_bc_chat.activies.User;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hunre.it.app_bc_chat.adapters.User.OrderAdapter;
import hunre.it.app_bc_chat.databinding.ActivityOrderHistoryBinding;
import hunre.it.app_bc_chat.models.Order;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;

public class OrderHistoryActivity extends AppCompatActivity {

    private ActivityOrderHistoryBinding binding;
    private PreferenceManager preferenceManager;
    private List<Order> orderList;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(this);
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList);

        binding.orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.orderRecyclerView.setAdapter(orderAdapter);

        loadOrders();
    }

    private void loadOrders() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonHang");
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        reference.orderByChild("uid_khachHang").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Order order = ds.getValue(Order.class);
                            orderList.add(order);
                        }
                        // Gán dữ liệu mới cho adapter mỗi khi có thay đổi
                        orderAdapter.notifyDataSetChanged();

                        if (orderList.isEmpty()) {
                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            binding.orderRecyclerView.setVisibility(View.GONE);
                        } else {
                            binding.emptyTxt.setVisibility(View.GONE);
                            binding.orderRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OrderHistoryActivity.this, "Failed to load orders: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
