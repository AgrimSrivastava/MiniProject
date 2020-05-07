package com.myapp.theagrim.torguo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class CreateUser extends AppCompatActivity {

    EditText editText;
    ImageView imageView;
    Spinner spinner;
    Button button,button1;
    String email;
    DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        editText= findViewById(R.id.name);
        imageView=findViewById(R.id.display);
        spinner=findViewById(R.id.country);
        button= findViewById(R.id.upload);
        button1=findViewById(R.id.choose);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseDatabase=FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();

        imageView.setImageResource(R.drawable.default_img);
        email=getIntent().getStringExtra("E-mail");

        if(savedInstanceState!=null){
            String name=savedInstanceState.getString("name");
            editText.setText(name);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                imageView.setImageResource(android.R.color.transparent);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String uri=taskSnapshot.getDownloadUrl().toString();
                            String name=editText.getText().toString();
                            String country=spinner.getSelectedItem().toString();
                            String key= firebaseAuth.getCurrentUser().getUid();
                            Log.d("Tourgo",key);
                            DataTransfer dataTransfer=new DataTransfer(email,name,country,uri);
                            String k=firebaseDatabase.child("Users").push().getKey();
                            firebaseDatabase.child("Users").child(k).setValue(dataTransfer);

                            progressDialog.dismiss();
                            Toast.makeText(CreateUser.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder alert=new AlertDialog.Builder(CreateUser.this);
                            alert.setMessage("Uploaded").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(CreateUser.this,LoginPage.class));
                                }
                            });
                            AlertDialog alertDialog= alert.create();
                            alertDialog.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Tourgo",String.valueOf(1));
                            progressDialog.dismiss();
                            Toast.makeText(CreateUser.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(),"No image file choosen",Toast.LENGTH_LONG).show();
        }




    }
}
