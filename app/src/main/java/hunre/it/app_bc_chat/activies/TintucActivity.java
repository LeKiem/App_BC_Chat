package hunre.it.app_bc_chat.activies;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hunre.it.app_bc_chat.Domain.TinTucDomain;
import hunre.it.app_bc_chat.adapters.User.AdapterTinTucPage1;
import hunre.it.app_bc_chat.databinding.ActivityTintucBinding;

public class TintucActivity extends BaseActivity1 {

    ActivityTintucBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTintucBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initLike();
        bottomNavigate();
    }

    private void bottomNavigate() {
        binding.tintucBtn.setOnClickListener(v -> startActivity(new Intent( TintucActivity.this, TintucActivity.class)));
        binding.homeBtn.setOnClickListener(v -> startActivity(new Intent( TintucActivity.this, MainActivity.class)));
        binding.KNBtn.setOnClickListener(v -> startActivity(new Intent(TintucActivity.this, KNActivity.class)));
        binding.BtnUser.setOnClickListener(v -> startActivity(new Intent(TintucActivity.this, UserInfoActivity.class)));
        binding.btnBack.setOnClickListener(v -> onBackPressed());
    }
    private void initLike() {
        DatabaseReference myRef = database.getReference("TinTuc");
        binding.progressBarNew.setVisibility(View.VISIBLE);
        ArrayList<TinTucDomain> news =  new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        news.add(issue.getValue(TinTucDomain.class));
                    }

                    if (!news.isEmpty()) {
                        binding.recyclerViewNew.setLayoutManager(new GridLayoutManager(TintucActivity.this,1));
                        binding.recyclerViewNew.setAdapter(new AdapterTinTucPage1(news));

                    }
                    binding.progressBarNew.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}