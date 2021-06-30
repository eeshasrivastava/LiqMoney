package com.starlord.runnigatm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.starlord.runnigatm.models.Users;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView profileName;
    ImageView profileImage, liquidMoney, hardMoney;
    FirebaseFirestore db;
    private Users user;
    String TAG = "Main Activity";

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        liquidMoney = findViewById(R.id.liquid_money);
        hardMoney = findViewById(R.id.hard_money);
        drawerLayout = findViewById(R.id.activity_main);
        navigationView = findViewById(R.id.nv);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

////////For changing the color of status bar ////////////////////////////////////////////////////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue_dark));
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


///////////Accessing data from FireStore database ///////////////////////////////////////////////////////////////////
        DocumentReference docRef = db.collection("users")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            user = documentSnapshot.toObject(Users.class);
            assert user != null;
            profileName.setText("Hello, " + user.getFirstName() + "!");
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////////


        profileImage.setOnClickListener(v -> openDrawer());

        liquidMoney.setOnClickListener(v -> {
            //TODO
        });

        hardMoney.setOnClickListener(v -> {
            //TODO
        });


////////Navigation drawer on click listener ////////////////////////////////////////////////////////////////
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            switch (id) {
                case R.id.action_profile:
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    break;
                case R.id.action_chats:
                    intent = new Intent(MainActivity.this, ChatActivity.class);
                    break;
                case R.id.action_settings:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    break;
                case R.id.action_transaction:
                    intent = new Intent(this, TransactionsActivity.class);
                    break;
                case R.id.action_help:
                    intent = new Intent(this, HelpActivity.class);
                    break;
                default:
                    return true;
            }

            closeDrawer();
            startActivity(intent);

            return true;
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}