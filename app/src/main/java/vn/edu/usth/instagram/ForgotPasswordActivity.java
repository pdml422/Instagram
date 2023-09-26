package vn.edu.usth.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnReset;
    TextView btnBack;
    TextInputLayout edtEmail;
    String strEmail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnBack = findViewById(R.id.btnBack);
        btnReset = findViewById(R.id.btnReset);
        edtEmail = findViewById(R.id.edtEmail);

        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                strEmail = edtEmail.getEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(strEmail)){
                    ResetPassword();
                } else {
                    edtEmail.setError("Email field can't be empty");
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this , LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private void ResetPassword() {
        mAuth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgotPasswordActivity.this, "Reset Password: A link has been sent to your registered Email", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}