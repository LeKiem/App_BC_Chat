package hunre.it.app_bc_chat.activies;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import hunre.it.app_bc_chat.Domain.SanPhamUser;
import hunre.it.app_bc_chat.Domain.SliderItems;
import hunre.it.app_bc_chat.Fragment.DescriptionFragment;
import hunre.it.app_bc_chat.Fragment.ReviewFragment;
import hunre.it.app_bc_chat.Helper.ManagmentCart;
import hunre.it.app_bc_chat.activies.User.CartActivity;
import hunre.it.app_bc_chat.adapters.SliderAdapter;
import hunre.it.app_bc_chat.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    private SanPhamUser object;
    private  int numberOrder =1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        getBundles();
        setupViewPager();
        initBanners();
    }
        private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().length(); i++){
            sliderItems.add(new SliderItems(object.getPicUrl()));
        }

        binding.viewpagerSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }
    private void getBundles() {
        object = (SanPhamUser) getIntent().getSerializableExtra("object");
        if (object != null) {
            binding.titleTxt.setText(object.getTitle());
            binding.priceTxt.setText(object.getGiaGoc() + "00đ");

            binding.btnBack.setOnClickListener(v -> finish());
            binding.btnCart.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                startActivity(intent);
            });
            binding.addToCartBtn.setOnClickListener(v -> {
                object.setNumberinCart(numberOrder);
                managmentCart.insertItem(object);
            });
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();

        Bundle bundle1 = new Bundle();
        if (object != null) {
            bundle1.putString("description", object.getDescription());
        }
        tab1.setArguments(bundle1);

        adapter.addFrag(tab1, "Mô tả sản phẩm");
        adapter.addFrag(tab2, "Đánh giá");

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
