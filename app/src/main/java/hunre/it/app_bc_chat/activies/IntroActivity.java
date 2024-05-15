package hunre.it.app_bc_chat.activies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import hunre.it.app_bc_chat.R;
import hunre.it.app_bc_chat.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {

    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigate();
    }

    private void bottomNavigate() {
        binding.Loginbtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, SingInActivity.class)));
        binding.Registerbtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, SignUpActivity.class)));
    }
}