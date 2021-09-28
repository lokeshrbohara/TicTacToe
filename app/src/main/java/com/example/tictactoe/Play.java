package com.example.tictactoe;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Play extends AppCompatActivity {

    private DatabaseReference mDatabaseUser;
    private DatabaseReference mDatabaseGame;
    private String lusername;
    private String lmail;
    private String luserimage;
    private String lplayerType;
    private String lgameID;

    boolean gameActive = true;
    int counter = 0;
    int playerType=9;
    int activePlayer = 0;
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    public void playerTap(View view){

        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());
        TextView status = findViewById(R.id.status);
        String statusString=status.getText().toString();
        if(!gameActive){
            resetGame();
        }
        if(gameState[tappedImage] == 2 && playerType==activePlayer){
            Log.d("GameState", gameState.toString());
            gameState[tappedImage] = activePlayer;
            if(lplayerType=="acceptor") mDatabaseGame.child("email_accept").setValue(String.valueOf(tappedImage));
            else mDatabaseGame.child("email_request").setValue(String.valueOf(tappedImage));

            img.setTranslationY(-1000f);
            if(activePlayer == 0){
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                //O's Turn
                statusString = lgameID.split("_")[0]+"'s Turn";
                status.setText(statusString);
                counter++;
            }
            else{
                img.setImageResource(R.drawable.o);
                activePlayer = 0;
                //"X's turn"
                statusString = lgameID.split("_")[1]+"'s Turn";
                status.setText(statusString);
                counter++;
            }
            img.animate().translationYBy(1000f).setDuration(300);
        }

        if(!someoneWon(statusString) && counter==9){
            gameActive = false;
            openDialog("Game is Draw");
        }

    }

    public boolean someoneWon(String a){
        String winner = a;
        for(int[] winPosition: winPositions){
            if(gameState[winPosition[0]]==gameState[winPosition[1]] &&
                    gameState[winPosition[1]]==gameState[winPosition[2]] &&
                    gameState[winPosition[0]]!=2){
                if(gameState[winPosition[0]]==0){
                    winner = "X has Won!!";
                }
                else{
                    winner = "O has Won!!";
                }
            }
        }


        if(winner == a) return false;
        else{
            gameActive = false;
            openDialog(winner);
            return true;
        }
    }

    public void resetGame(){
        gameActive = true;
        counter = 0;
        for(int i=0; i<9; i++)
        {
            gameState[i] = 2;
        }
        ((ImageView) findViewById(R.id.imageView0)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView) findViewById(R.id.imageView8)).setImageResource(0);
    }

    public void openDialog(String toSet){
        Dialog dialog = new Dialog(toSet);
        dialog.show(getSupportFragmentManager(), "message dialog");
        resetGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

//        System.out.println("Help");
//        SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);
//        lusername = preferences.getString("username","");
//        lmail = preferences.getString("emailID" , "");
//        luserimage = preferences.getString("userPhoto" , "");
//        lplayerType = preferences.getString("playerType" , "");
//        lgameID = preferences.getString("gameID", "");
//
//        if(lplayerType=="acceptor") playerType=0;
//        else playerType=1;
//        mDatabaseUser = FirebaseDatabase.getInstance().getReference("online_users").child(lmail.split("@")[0]);
//
//        mDatabaseGame = FirebaseDatabase.getInstance().getReference("online_game").child(lgameID);
//
//
//        String oppositePlayer = lgameID.replace(lmail.split("@")[0],"");
//        oppositePlayer = lgameID.replace("_","");
//
//        String finalOppositePlayer = oppositePlayer;
//
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                OnlineGame post = dataSnapshot.getValue(OnlineGame.class);
//                if(lplayerType=="acceptor") {
//                    if (post.email_request!=finalOppositePlayer) {
//                       gameState[Integer.parseInt(post.email_request)]=playerType-1;
//                    }
//                }
//                else{
//                    if (post.email_accept!=finalOppositePlayer) {
//                        gameState[Integer.parseInt(post.email_accept)]=playerType-1;;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        mDatabaseGame.addValueEventListener(postListener);

    }

//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//        mDatabaseUser.child("accept").setValue("");
//    }
}