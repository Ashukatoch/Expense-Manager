package com.example.expensemanager2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText email,password;
    private Button login;
    private TextView swapactivity;
    private LinearLayout loginlayout;
    private FirebaseAuth mauth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.emailedittext);
        password=(EditText)findViewById(R.id.passedittext);
        swapactivity=(TextView)findViewById(R.id.loginsignup);
        login=(Button)findViewById(R.id.loginbtn);
        loginlayout=findViewById(R.id.loginlayout);
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        loginlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager input=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(email.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                input.hideSoftInputFromWindow(password.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String lemail=email.getText().toString().trim();
            String lpass=password.getText().toString().trim();
            if(TextUtils.isEmpty(lemail))
            {
                email.setError("Email Required...");
                return;
            }

            if(TextUtils.isEmpty(lpass)) {
                password.setError("Password Required...");
            return;
            }
            password.setText("");
            mauth.signInWithEmailAndPassword(lemail,lpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Login Successfull!!",Toast.LENGTH_LONG);
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Login Failed!!",Toast.LENGTH_LONG);
                }
            });
            }
        });
        swapactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registrationactivity.class));
            }
        });
    }
    }

