 package hunre.it.app_bc_chat.activies;

 import android.os.Bundle;
 import android.view.Window;
 import android.view.WindowManager;

 import androidx.appcompat.app.AppCompatActivity;

 import com.google.firebase.database.FirebaseDatabase;

// import hunre.it.app_bc.databinding.ActivityCartBinding;

 public class BaseActivity1 extends AppCompatActivity {

//     ActivityCartBinding binding;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database=FirebaseDatabase.getInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}