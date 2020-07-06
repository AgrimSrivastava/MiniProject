package com.myapp.theagrim.torguo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    Button email_btn;
    TextView textView1;
    EditText editText;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    private void toggle(View v1,View v2){
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editText=findViewById(R.id.email);
        firebaseAuth= FirebaseAuth.getInstance();
        email_btn=findViewById(R.id.reset);
        progressBar=findViewById(R.id.progress_circular);
        toggle(email_btn,progressBar);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this,LoginPage.class));
            }
        });
        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(progressBar,email_btn);
                String email_= editText.getText().toString().trim();

                if(isValid(email_)) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email_)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    toggle(email_btn,progressBar);
                                    if (task.isSuccessful()) {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPassword.this);
                                        alert.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(ForgotPassword.this, LoginPage.class));
                                            }
                                        }).setMessage("Password Reset Email sent successfully").setTitle("Information");
                                        AlertDialog alertDialog = alert.create();
                                        alertDialog.show();


                                    } else {
                                        Toast.makeText(ForgotPassword.this, "An error occured" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    toggle(email_btn,progressBar);
                    editText.setError("Invalid e-mail address");

                }
            }
        });

    }
    static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
