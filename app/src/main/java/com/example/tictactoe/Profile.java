package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile extends AppCompatActivity {

    private Button logout;
    private Button request;
    private Button accept;
    private DatabaseReference mDatabase;
    private DatabaseReference reference;
    private DatabaseReference mDatabaseUser;
    private String lusername;
    private String lmail;
    private String luserimage;
    private EditText requestText;
    private TextView acceptText;
    private Boolean flag = false;
    private ListView onlineList;

    ArrayList<String> onlineUsersList;

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
        onlineList = findViewById(R.id.onlineList);

        onlineUsersList = new ArrayList<String>();
        initializeListView();


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                OnlineUser postq = dataSnapshot.getValue(OnlineUser.class);

                System.out.println("pppppppppppppppppppppppppppppppppppppppppppppppppppppp");
                System.out.println(postq);
                if(postq!=null)
                {
                    acceptText.setText(postq.request);

                    if(!postq.accept.isEmpty() && !flag)
                    {
                        String acceptorEmail = postq.accept;
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabaseUser.addValueEventListener(postListener);

        onlineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                requestText.setText(selectedItem);
            }
        });

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

    private void initializeListView(){

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, onlineUsersList);

        reference = FirebaseDatabase.getInstance().getReference("online_users");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added to
                // our data base and after adding new child
                // we are adding that item inside our array list and
                // notifying our adapter that the data in adapter is changed.
//                System.out.println(snapshot.getKey());
                onlineUsersList.add(snapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when the new child is added.
                // when the new child is added to our list we will be
                // notifying our adapter that data has changed.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // below method is called when we remove a child from our database.
                // inside this method we are removing the child from our array list
                // by comparing with it's value.
                // after removing the data we are notifying our adapter that the
                // data has been changed.
                onlineUsersList.remove(snapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when we move our
                // child in our database.
                // in our code we are note moving any child.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // this method is called when we get any
                // error from Firebase with error.
            }
        });
        // below line is used for setting
        // an adapter to our list view.
        onlineList.setAdapter(adapter);
        requestText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void writeNewUser(String userId, String accept, String request) {
        OnlineUser user = new OnlineUser(accept, request);

        mDatabase.child("online_users").child(userId).setValue(user);
    }

    @Override
    protected void onDestroy() {

//        System.out.println("DEsrroyed");
        super.onDestroy();
        mDatabase.child("online_users").child(lmail.split("@")[0]).removeValue();
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
////        System.out.println("Paused");
//        mDatabase.child("online_users").child(lmail.split("@")[0]).removeValue();
//    }



}