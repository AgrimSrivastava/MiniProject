package com.myapp.theagrim.torguo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    Button btn;
    EditText txt1,txt2,txt3;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener listener;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn=findViewById(R.id.verify);
        txt1=findViewById(R.id.email);
        txt2=findViewById(R.id.password);
        txt3=findViewById(R.id.cnfrmpassword);
        firebaseAuth=FirebaseAuth.getInstance();

        listener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("TourGo","sendVerificationEmail() callback");
                    sendVerificationEmail();
                }
                else {

                }
            }
        };
    }

    public void verify(View view) {
       email=txt1.getText().toString().trim();
        String password=txt2.getText().toString();
        final String cnfrm=txt3.getText().toString();
        btn.setText(R.string.verifying);

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(Register.this,"Empty email address or password",Toast.LENGTH_LONG).show();
            btn.setText(R.string.verify);
            return;
        }

        if(!password.equals(cnfrm)){
            Toast.makeText(Register.this,"Both passwords are different",Toast.LENGTH_LONG).show();
            btn.setText(R.string.verify);
            txt3.setText("");

            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.d("TourGo",task.getException().toString());
                    Toast.makeText(getApplicationContext(),"Something went wrong Try again",Toast.LENGTH_LONG).show();
                    txt2.setText("");
                    txt3.setText("");
                    btn.setText(R.string.verify);
                }

                else {
                    Toast.makeText(getApplicationContext(),"Account created successfully",Toast.LENGTH_LONG).show();
                    sendVerificationEmail();
                    txt2.setText("");
                    txt3.setText("");
                    btn.setText(R.string.verify);
                }
            }
        });

    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            FirebaseAuth.getInstance().signOut();
                            Intent intent=new Intent(Register.this, CreateUser.class);
                            intent.putExtra("E-mail",email);
                            startActivity(intent);

                            finish();

                        }
                        else
                        {
                            Toast.makeText(Register.this,"Unable to send Email Try Again",Toast.LENGTH_LONG).show();
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }



}
