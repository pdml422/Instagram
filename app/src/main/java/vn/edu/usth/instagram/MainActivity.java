package vn.edu.usth.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import vn.edu.usth.instagram.Fragment.HomeFragment;
import vn.edu.usth.instagram.Fragment.NotificationFragment;
import vn.edu.usth.instagram.Fragment.ProfileFragment;
import vn.edu.usth.instagram.Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {


    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_home) {
                    selectorFragment = new HomeFragment();
                } else if (itemId == R.id.nav_search) {
                    selectorFragment = new SearchFragment();
                } else if (itemId == R.id.nav_add) {
                    selectorFragment = null;
                    startActivity(new Intent(MainActivity.this, PostActivity.class));
                } else if (itemId == R.id.nav_heart) {
                    selectorFragment = new NotificationFragment();
                } else if (itemId == R.id.nav_profile) {
                    selectorFragment = new ProfileFragment();
                }

                if (selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                }

                return true;

            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
        }

    }
}