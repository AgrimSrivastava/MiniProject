package com.myapp.theagrim.torguo;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Register extends AppCompatActivity {

    Button signup;
    EditText et_email,et_pass,et_repass;
    FirebaseAuth firebaseAuth;
    String email;
    LinearLayout linearLayout;
    ImageView imageView;
    TextView login;
    ProgressBar progressBar;

    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageView=findViewById(R.id.blurimageview);

        Glide.with(getApplicationContext()).load(R.drawable.blackred)
                .apply(bitmapTransform(new BlurTransformation(50)))
                .into(imageView);

        et_email=findViewById(R.id.email);
        progressBar=findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);

        et_pass=findViewById(R.id.password);
        et_repass=findViewById(R.id.repassword);
        firebaseAuth=FirebaseAuth.getInstance();
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });

        et_repass.setImeOptions(EditorInfo.IME_ACTION_DONE);

        et_repass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL||id==EditorInfo.IME_ACTION_DONE||id==EditorInfo.IME_ACTION_GO) {
                    verify();
                    return true;
                }
                return false;
            }
        });


    }

    public void verify() {
        progressBar.setVisibility(View.VISIBLE);
        signup.setVisibility(View.INVISIBLE);
       email=et_email.getText().toString().trim();
        String password=et_pass.getText().toString();
        String cnfrm=et_repass.getText().toString();

        if(TextUtils.isEmpty(email)){
            signup.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            et_email.setError("Empty email Address");
            return;
        }

        if(TextUtils.isEmpty(password)){
            signup.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            et_pass.setError("Empty password");
            return;
        }

        if(!password.equals(cnfrm)){
            signup.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            et_repass.setError("Both passwords do not match");

            return;
        }
        et_repass.setText("");

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                signup.setVisibility(View.VISIBLE);
                if(!task.isSuccessful()){
                   if(task.getException().toString().contains("The email address is already in use by another account.")){
                       et_email.setError("Email already in use");
                   }
                    Toast.makeText(getApplicationContext(),"Something went wrong Try again",Toast.LENGTH_LONG).show();
                }

                else {
                    new AlertDialog.Builder(Register.this).setTitle("Account Created Successfully!!!").setMessage("Thanks for joining us .Your account is created .Go back to login page").setCancelable(false).setPositiveButton("LogIn page", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            dialog.dismiss();
                        }
                    }).create().show();
                }
            }
        });

    }


}
