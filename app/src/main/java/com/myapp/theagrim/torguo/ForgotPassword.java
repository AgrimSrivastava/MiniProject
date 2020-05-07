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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {

    AutoCompleteTextView textView;
    Button email_btn;
    TextView textView1;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        textView=findViewById(R.id.email);
        email_btn=findViewById(R.id.sendmail);
        textView1=findViewById(R.id.back);
        firebaseAuth= FirebaseAuth.getInstance();
        String arr[]=createArray();

        if(arr.length>=1){
            Log.d("TourGo","arr present");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ForgotPassword.this,android.R.layout.select_dialog_item, arr);
            textView.setThreshold(1);
            textView.setAdapter(adapter);
        }

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this,LoginPage.class));
            }
        });
        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_= textView.getText().toString().trim();

                if(email_.contains("@")&&!TextUtils.isEmpty(email_)) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email_)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TourGo", "Email sent");
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
                    Toast.makeText(ForgotPassword.this,"Check your email adrress",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    String[] createArray(){

        SharedPreferences sharedPreferences=getSharedPreferences("data",0);
        if(sharedPreferences!=null){
            Log.d("TourGo","create");
            int size=sharedPreferences.getInt("size",0);
            String[] arr=new String[size];
            for(int i=0;i<size;++i){
                arr[i]=sharedPreferences.getString(String.valueOf(i),"empty");
            }
            return arr;
        }
        return null;
    }
}
