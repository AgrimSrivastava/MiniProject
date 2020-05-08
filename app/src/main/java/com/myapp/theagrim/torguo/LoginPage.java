package com.myapp.theagrim.torguo;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginPage extends AppCompatActivity {

    Button lgn_btn;
    TextView forgotpsswrd;
    TextView createaccnt;
    EditText email;
    EditText password;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        lgn_btn=findViewById(R.id.login);
        forgotpsswrd=findViewById(R.id.frgt_passwrd);
        createaccnt=findViewById(R.id.create_accnt);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        if(user!=null){
            redirect();
        }

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



    }



    public void signInExistingUser(View v){
        attemptLogin();
    }

    public void attemptLogin(){
        String email_=email.getText().toString().trim();
        String password_=password.getText().toString().trim();
        if(TextUtils.isEmpty(email_)||TextUtils.isEmpty(password_)){
            Toast.makeText(this,"Check credentials",Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email_,password_).addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.d("TourGo","SignIn not Successful"+task.getException());

                    Toast.makeText(LoginPage.this,"Check your Credentials ",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginPage.this, "Login Krre ho Kya Bhai", Toast.LENGTH_SHORT).show();
                    redirect();
                }

            }
        });
    }

    public void redirect(){
//      start HomePage activity
        startActivity(new Intent(LoginPage.this,ProviderHomePage.class));
        finish();
    }

    public void registerNewUser(View v){
        startActivity(new Intent(LoginPage.this,Register.class));
    }

    public void setForgotpsswrd(View v){
        startActivity(new Intent(LoginPage.this,ForgotPassword.class));
    }


}
