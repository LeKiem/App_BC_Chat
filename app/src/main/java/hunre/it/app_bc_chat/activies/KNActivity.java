package hunre.it.app_bc_chat.activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import hunre.it.app_bc_chat.Fragment.KnccFragment;
import hunre.it.app_bc_chat.Fragment.KnpcFragment;
import hunre.it.app_bc_chat.Fragment.KntnFragment;
import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.databinding.ActivityKnactivityBinding;

public class KNActivity extends  BaseActivity1 {

    ActivityKnactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKnactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPager();
        bottomNavigate();
    }

    private void bottomNavigate() {
        binding.tintucBtn.setOnClickListener(v -> startActivity(new Intent( KNActivity.this, TintucActivity.class)));
        binding.KNBtn.setOnClickListener(v -> startActivity(new Intent(KNActivity.this, KNActivity.class)));
        binding.BtnUser.setOnClickListener(v -> startActivity(new Intent(KNActivity.this, UserInfoActivity.class)));
        binding.homeBtn.setOnClickListener(v -> startActivity(new Intent(KNActivity.this, MainActivity.class)));
        binding.btnBack.setOnClickListener(v -> onBackPressed());
    }

    private  void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        KnpcFragment tab1 = new KnpcFragment();
        KnccFragment tab2 = new KnccFragment();
        KntnFragment tab3 = new KntnFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();


        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);

        adapter.addFrag(tab1, "KNPC");
        adapter.addFrag(tab2, "KNCC");
        adapter.addFrag(tab3, "KCTN");

        binding.viewpage2.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpage2);
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private  final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        private void  addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public  CharSequence getPageTitle (int position){
            return  mFragmentTitleList.get(position);
        }
    }
}