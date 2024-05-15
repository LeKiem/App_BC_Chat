package hunre.it.app_bc_chat.activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import hunre.it.app_bc_chat.Domain.ItemsDomain;
import hunre.it.app_bc_chat.Domain.SliderItems;
import hunre.it.app_bc_chat.Fragment.DescriptionFragment;
import hunre.it.app_bc_chat.Fragment.ReviewFragment;
import hunre.it.app_bc_chat.Helper.ManagmentCart;
import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.adapters.SizeAdapter;
import hunre.it.app_bc_chat.adapters.SliderAdapter;
import hunre.it.app_bc_chat.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity1 {

    ActivityDetailBinding binding;

    private ItemsDomain object;
    private  int numberOrder =1;
    private ManagmentCart managmentCart;
    private Handler slideHandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart= new ManagmentCart(this);
        getBundles();
        initBanners();
//        initSize();
        setupViewPager();
    }

    private void initSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S");
        list.add("M");
        list.add("L");
        list.add("XL");
        list.add("XXl");

        binding.recylerSize.setAdapter(new SizeAdapter(list));
        binding.recylerSize.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }
    private void initBanners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for (int i = 0; i < object.getPicUrl().size(); i++){
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }

        binding.viewpagerSlider.setAdapter(new SliderAdapter(sliderItems, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }
    private void getBundles() {
        object=(ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText(object.getPrice() +"00đ");
        binding.ratingBar.setRating((float) object.getRating());
        binding.ratingTxt.setText(object.getRating()+"Sao");

        binding.addToCartBtn.setOnClickListener(v -> {
            object.setNumberinCart(numberOrder);
            managmentCart.insertItem(object);
        });
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private  void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
//        KnpcFragment tab3 = new KnpcFragment();
//        SoldFragment tab4 = new SoldFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();

        bundle1.putString("description", object.getDescription());

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);


        adapter.addFrag(tab1, "Mô tả sản phầm");
        adapter.addFrag(tab2, "Đánh giá ");


        binding.viewpage.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpage);
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