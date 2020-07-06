package com.myapp.theagrim.torguo;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class LoginPage extends AppCompatActivity {


    EditText email;
    EditText password;
    ImageView imageView;
    Button loginbtn;
    FirebaseAuth firebaseAuth;
    TextView forgotpassword,signup;
    ProgressBar progressBar;
    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        imageView=findViewById(R.id.blurimageview);
        progressBar=findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);
        Glide.with(getApplicationContext()).load(R.drawable.blackred)
                .apply(bitmapTransform(new BlurTransformation(80)))
                .into(imageView);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        loginbtn=findViewById(R.id.login);
        forgotpassword=findViewById(R.id.forgotpassword);
        signup=findViewById(R.id.signup);

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start activity for forgot password

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginPage.this,Register.class));
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });



        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptLogin();
            }
        });



    }


    public void attemptLogin(){
        loginbtn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        String email_=email.getText().toString().trim();
        String password_=password.getText().toString().trim();
        if(TextUtils.isEmpty(email_)){
            progressBar.setVisibility(View.INVISIBLE);
            loginbtn.setVisibility(View.VISIBLE);
            email.setError("Empty email");
            return;
        }
        if(TextUtils.isEmpty(password_)){
            progressBar.setVisibility(View.INVISIBLE);
            loginbtn.setVisibility(View.VISIBLE);
            password.setError("Empty password");
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email_,password_).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                loginbtn.setVisibility(View.VISIBLE);
                if(!task.isSuccessful()){
                    password.setError("Invalid username or password");
                }
                else{
                    Intent intent=new Intent(LoginPage.this,ReDirectActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

            }
        });
    }




}
