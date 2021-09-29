package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private Button logout;
    private Button request;
    private Button accept;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private String lusername;
    private String lmail;
    private String luserimage;
    private EditText requestText;
    private TextView acceptText;
    private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);
        lusername = preferences.getString("username","");
        lmail = preferences.getString("emailID" , "");
        luserimage = preferences.getString("userPhoto" , "");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference("online_users").child(lmail.split("@")[0]);

        logout = findViewById(R.id.logout);
        request = findViewById(R.id.request);
        accept = findViewById(R.id.accept);
        requestText = findViewById(R.id.requestText);
        acceptText = findViewById(R.id.acceptText);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                OnlineUser post = dataSnapshot.getValue(OnlineUser.class);
                acceptText.setText(post.request);

                if(!post.accept.isEmpty() && !flag)
                {

                    String acceptorEmail = post.accept;
                    SharedPreferences.Editor editor = getApplicationContext()
                            .getSharedPreferences("MyPrefs" , MODE_PRIVATE)
                            .edit();
                    editor.putString("playerType","requestor");
                    editor.putString("gameID",acceptorEmail+"_"+lmail.split("@")[0]);
                    Log.d("WHYHERE" , "_____________________________________________");
                    editor.apply();

                    Intent intent = new Intent(Profile.this, Play.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabaseUser.addValueEventListener(postListener);



        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(Profile.this , MainActivity.class));
            finish();
        });

        request.setOnClickListener(view->{

            String otherUser = requestText.getText().toString();
            mDatabase.child("online_users").child(otherUser).child("request").setValue(lmail.split("@")[0]);
        });

        accept.setOnClickListener(view -> {
            String acceptEmail = acceptText.getText().toString();

            if(!acceptEmail.isEmpty())
            {
                flag = true;
                mDatabase.child("online_users").child(lmail.split("@")[0]).child("accept").setValue(acceptEmail);
                mDatabase.child("online_users").child(acceptEmail).child("accept").setValue(lmail.split("@")[0]);

                OnlineGame game = new OnlineGame(lmail.split("@")[0], acceptEmail);
                mDatabase.child("online_game").child(lmail.split("@")[0]+"_"+acceptEmail).setValue(game);

                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences("MyPrefs" , MODE_PRIVATE)
                        .edit();
                editor.putString("playerType","acceptor");
                editor.putString("gameID",lmail.split("@")[0]+"_"+acceptEmail);

                editor.apply();


                Log.d("Entered", acceptEmail);
                Intent intent1 = new Intent(Profile.this, Play.class);
                startActivity(intent1);

            }
        });

        writeNewUser(lmail.split("@")[0], "", "");


    }

    public void writeNewUser(String userId, String accept, String request) {
        OnlineUser user = new OnlineUser(accept, request);

        mDatabase.child("online_users").child(userId).setValue(user);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mDatabase.child("online_users").child(lmail.split("@")[0]).removeValue();
    }
}