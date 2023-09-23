package vn.edu.usth.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.google.firebase.auth.FirebaseAuth;

import vn.edu.usth.instagram.LoginActivity;
import vn.edu.usth.instagram.MainActivity;
import vn.edu.usth.instagram.R;
import vn.edu.usth.instagram.RegisterActivity;

public class StartActivity extends AppCompatActivity {

    private ImageView iconImage;
    private LinearLayout linearLayout;
    private Button login;
    private Button sign_up;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        linearLayout = findViewById(R.id.linear_layout);
        sign_up = findViewById(R.id.sign_up);
        login = findViewById(R.id.login);


        sign_up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(StartActivity.this , RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this , LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//            startActivity(new Intent(StartActivity.this, MainActivity.class));
//            finish();
//        }
    }
}
