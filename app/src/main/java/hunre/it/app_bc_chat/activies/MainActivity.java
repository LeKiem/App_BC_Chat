package hunre.it.app_bc_chat.activies;

import android.content.Context;
import android.content.Intent;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import hunre.it.app_bc_chat.Domain.ItemsDomain;
import hunre.it.app_bc_chat.Domain.LikesDomain;
import hunre.it.app_bc_chat.Domain.SliderItems;
import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.adapters.LikeAdapter;
import hunre.it.app_bc_chat.adapters.PopularAdapter;
import hunre.it.app_bc_chat.adapters.RecentConversationsAdapter;
import hunre.it.app_bc_chat.adapters.SliderAdapter;
import hunre.it.app_bc_chat.databinding.ActivityMainBinding;
import hunre.it.app_bc_chat.databinding.ActivitySingInBinding;
import hunre.it.app_bc_chat.network.ApiClient;
import hunre.it.app_bc_chat.network.ApiService;
import hunre.it.app_bc_chat.utilities.Constants;
import hunre.it.app_bc_chat.utilities.PreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity1 {
    private PreferenceManager preferenceManager;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        initBanner();
//        initCategory();
        initPopular();
        initLike();
        bottomNavigate();
//        loadUserDetails();
        getToken();

    }


    private  void loadUserDetails(){
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
    private  void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private  void  updateToken(String token){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }
    private void initLike() {
        DatabaseReference myRef = database.getReference("Likes");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<LikesDomain> likes =  new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        likes.add(issue.getValue(LikesDomain.class));
                    }

                    if (!likes.isEmpty()) {
                        binding.recyclerViewOfficial.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                        binding.recyclerViewOfficial.setAdapter(new LikeAdapter(likes));
                    }
                    binding.progressBarOfficial.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void bottomNavigate() {
        binding.tintucBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TintucActivity.class)));
        binding.KNBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, KNActivity.class)));
        binding.BtnUser.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserInfoActivity.class)));
        binding.btnNoti.setOnClickListener(v -> {
            sendNoti();
        });
    }
    private void sendTopic(){
//        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().subscribeToTopic("noti")
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed noti";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
//                        Log.d(Tag, msg);
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }
    private void sendNoti(){

        try {
            JSONObject data  = new JSONObject();
            data.put(Constants.KEY_USER_ID, "id");
            data.put(Constants.KEY_NAME, "Name");
//                data.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());

            JSONObject body = new JSONObject();
            body.put("to", "/topics/noti");
            body.put(Constants.REMOTE_MSG_DATA, data);


            sendNotificationToAllUsers(body.toString());

        } catch (Exception e){
            showToast(e.getMessage());
        }

//        binding.inputMessage.setText(null);
    }
    public void sendNotificationToAllUsers(String messageBody) {


        ApiClient.getClient()
                .create(ApiService.class)
                .sendMessage(Constants.getRemoteMsgHeaders(), messageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.isSuccessful()) {
                            handleSuccessResponse(response);
                        } else {
                            handleErrorResponse(response);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        showToast(t.getMessage());
                    }
                });


    }
    private void handleSuccessResponse(Response<String> response) {
        try {
            if (response.body() != null) {
                JSONObject responseJson = new JSONObject(response.body());
                int failure = responseJson.getInt("failure");
                if (failure == 1) {
                    JSONArray results = responseJson.getJSONArray("results");
                    JSONObject error = (JSONObject) results.get(0);
                    showToast(error.getString("error"));
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showToast("Notification sent successfully");
    }
    private void handleErrorResponse(Response<String> response) {
        System.out.println("1:"+response);
        showToast("Error: " + response.code());
    }
    private  void showToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));
                    }

                    if (!items.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

//    private void initCategory() {
//        DatabaseReference myRef = database.getReference("Category");
//        binding.progressBarOfficial.setVisibility(View.VISIBLE);
//        ArrayList<CategoryDomain> items = new ArrayList<>();
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        items.add(issue.getValue(CategoryDomain.class));
//                    }
//                    if (!items.isEmpty()) {
//                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this,
//                                LinearLayoutManager.HORIZONTAL, false));
//                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));
//                    }
//                    binding.progressBarOfficial.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewpagerSlider.setAdapter(new SliderAdapter(items, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }

}