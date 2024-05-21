package hunre.it.app_bc_chat.activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import hunre.it.app_bc_chat.Fragment.Admin.QLSanPham;
import hunre.it.app_bc_chat.Fragment.Admin.QlTinTuc;
import hunre.it.app_bc_chat.Fragment.Admin.ThongTinQuan;
import hunre.it.app_bc_chat.Fragment.KnccFragment;
import hunre.it.app_bc_chat.Fragment.KnpcFragment;
import hunre.it.app_bc_chat.Fragment.KntnFragment;
import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.adapters.Admin.AdapterViewPager2;
import hunre.it.app_bc_chat.databinding.ActivityKnactivityBinding;

public class KNActivity extends  BaseActivity1 {

    ActivityKnactivityBinding binding;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKnactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setupViewPager();
        bottomNavigate();
        loadFragments();
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

//        binding.viewpage2.setAdapter(adapter);
//        binding.tabLayout.setupWithViewPager(binding.viewpage2);
    }
    private void loadFragments() {

        // nạp fragment vào viewpager tại activity hiện hành
        fragments.add(new KnccFragment());
        fragments.add(new KnpcFragment());
        fragments.add(new KntnFragment());
        AdapterViewPager2 adapterViewPager2 = new AdapterViewPager2(this, fragments);
        binding.container.setAdapter(adapterViewPager2);
        // nút button navigation sẽ thay đổi theo trang
        binding.container.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.menu.setItemSelected(R.id.kncc, true);
                        break;
                    case 1:
                        binding.menu.setItemSelected(R.id.knpc, true);
                        break;
                    case 2:
                        binding.menu.setItemSelected(R.id.kntn, true);
                        break;
                }
                super.onPageSelected(position);
            }
        });
        // nạp viewpager2 vào bottom navigation
        binding.menu.setOnItemSelectedListener(i -> {
            switch (i) {
                case R.id.kncc:
                    binding.container.setCurrentItem(0);
                    break;
                case R.id.knpc:
                    binding.container.setCurrentItem(1);
                    break;
                case R.id.kntn:
                    binding.container.setCurrentItem(2);
                    break;
            }
        });

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