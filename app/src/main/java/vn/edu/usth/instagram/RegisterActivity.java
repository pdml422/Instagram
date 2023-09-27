package vn.edu.usth.instagram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import vn.edu.usth.instagram.Model.User;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout username;
    private TextInputLayout name;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button register;
    private TextView loginUser;
    private Button login_button;
    private Button uncheck;
    private String imageUrl;
    private TextView usernameStatus;


    private static int usernameFlag = 0;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.sign_up);
        loginUser = findViewById(R.id.login_user);
        login_button = findViewById(R.id.login_button);
        uncheck = findViewById(R.id.check_username);
        usernameStatus = findViewById(R.id.availability_username);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getEditText().getText().toString();
                String txtName = name.getEditText().getText().toString();
                String txtEmail = email.getEditText().getText().toString();
                String txtPassword = password.getEditText().getText().toString();

                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername, txtName, txtEmail, txtPassword);
                }
            }
        });

        uncheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUsernameAvailability();
            }
        });
    }


    private void checkUsernameAvailability(){

        final String txt_username = username.getEditText().getText().toString();

        usernameStatus.setVisibility(View.VISIBLE);
        DatabaseReference mUsersRed = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersRed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (txt_username.equals(user.getUsername())){
                        usernameFlag = 1;
                        usernameStatus.setText("Unavailable! Username alredy taken! Try something else.");
                        return;
                    } else if(txt_username.equals("")){
                        usernameFlag = 1;
                        usernameStatus.setText("Unavailable! Please enter an username.");
                        return;
                    }
                }
                usernameStatus.setText("Available!");
                usernameFlag = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void registerUser(final String username , final String name , String email , String password) {
        pd.setMessage("Please Wait!");
        pd.show();
        if (usernameFlag == 0) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userid = firebaseUser.getUid();
                        imageUrl = "default";

                        mRootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imagerul", imageUrl);
                        hashMap.put("id", userid);
                        hashMap.put("username", username.toLowerCase());
                        hashMap.put("name", name);

                        mRootRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    usernameStatus.setText("Available!");
                                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "You can't register with this email and password!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            pd.dismiss();
            Toast.makeText(this, "Please change the username!", Toast.LENGTH_SHORT).show();
        }
    }

}