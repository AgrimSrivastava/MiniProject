package com.myapp.theagrim.torguo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageAdmin extends AppCompatActivity {


    private ListView listView;
    private ImageButton imageButton;
    private EditText editText;
    private DatabaseReference databaseReference;
    private ChatListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_admin);

        android.support.v7.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView=findViewById(R.id.chat_list_view);
        imageButton=findViewById(R.id.sendButton);
        editText=findViewById(R.id.messageInput);

//        FirebaseAuth.getInstance().getCurrentUser().getUid()

        databaseReference=FirebaseDatabase.getInstance().getReference().child("Provider").
                                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                sendMessage();
                return true;
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void sendMessage(){
        String message=editText.getText().toString().trim();
        if(!TextUtils.isEmpty(message)){
            InstantMessage instantMessage=new InstantMessage(message,1);
            databaseReference.child("Messages").push().setValue(instantMessage);
            editText.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(),"Empty Message Body",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter=new ChatListAdapter(this,databaseReference);
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.cleanup();
    }
}
