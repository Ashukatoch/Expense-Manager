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

public class Registrationactivity extends AppCompatActivity {

    private EditText email,password;
    private Button signup;
    private TextView swapactivity;
    private LinearLayout registrationlayout;
    private FirebaseAuth mauth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationactivity);
        email=(EditText)findViewById(R.id.signupemail);
        password=(EditText)findViewById(R.id.signuppassword);
        swapactivity=(TextView)findViewById(R.id.signuplogin);
        signup=(Button)findViewById(R.id.signupbtn);
        registrationlayout=findViewById(R.id.registrationlayout);
        mauth=FirebaseAuth.getInstance();
        registrationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager input=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
//        user=mauth.getCurrentUser();
//        if(user!=null)
//        {
//            Toast.makeText(getApplicationContext(),user.getEmail()+"logged off successfully",Toast.LENGTH_LONG);
//            mauth.signOut();
//        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String memail=email.getText().toString().trim();
                String mpass=password.getText().toString().trim();
                if(TextUtils.isEmpty(memail))
                {
                email.setError("Email Required...");
                return;
                }
                if(TextUtils.isEmpty(mpass))
                {
                password.setError("Password Required...");
                return;
                }
                password.setText("");
                mauth.createUserWithEmailAndPassword(memail,mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Signup Successfull!!",Toast.LENGTH_LONG);
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Signup Failed!!",Toast.LENGTH_LONG);
                    }
                });

            }
        });
        swapactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }
}
