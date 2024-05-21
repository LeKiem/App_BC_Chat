package hunre.it.app_bc_chat.activies.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hunre.it.app_bc_chat.Domain.SliderItems;
import hunre.it.app_bc_chat.Domain.TinTucDomain;
import hunre.it.app_bc_chat.Fragment.DescriptionFragment;
import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.databinding.ActivityDetailTinTucBinding;
import hunre.it.app_bc_chat.adapters.SliderAdapter;

public class DetailTinTucActivity extends AppCompatActivity {
    ActivityDetailTinTucBinding binding;
    private TinTucDomain object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTinTucBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getBundles();
        initBanners();
        setupViewPager();
    }

    private void getBundles() {
        object = (TinTucDomain) getIntent().getSerializableExtra("object");
        if (object != null) {
            binding.tenTTTxt.setText(object.getTenTT());
            binding.btnBack.setOnClickListener(v -> finish());
        }
    }

    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getHinhAnh().length(); i++) {
            sliderItems.add(new SliderItems(object.getHinhAnh()));
        }

        binding.viewpagerSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        DescriptionFragment tab1 = new DescriptionFragment();
        Bundle bundle1 = new Bundle();
        if (object != null) {
            bundle1.putString("moTa", object.getMoTa());
        }
        tab1.setArguments(bundle1);
        adapter.addFrag(tab1, "Thông tin bài viết");

        binding.viewpage.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpage);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
