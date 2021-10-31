package com.example.tictactoe;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private TextView status, gameCountText, totalWinText, opponentMessage;
    private ImageView imageToChange;
    private String finalOppositePlayer;
    private int turnChanger = 0;
    private int gameCount = 1;
    private int winCount = 0;
    private Button sendMsg;
    private EditText message;
    private String lmail_cleaned;
    boolean gameActive = true;
    private boolean init = false;
    int counter = 0;
    int playerType=10;
    int activePlayer = turnChanger;
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    int[] gameState = {2,2,2,2,2,2,2,2,2};

    public void playerTap(View view){

        System.out.println("Inside Tap__________________________________________________________________________________");
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        String statusString=status.getText().toString();

        if(!gameActive){
            resetGame();
        }
        System.out.println(playerType);
        System.out.println(activePlayer);
        if(gameState[tappedImage] == 2 && playerType==activePlayer){
            Log.d("GameState", gameState.toString());
            gameState[tappedImage] = activePlayer;
            if(lplayerType.equals("acceptor")) mDatabaseGame.child("email_accept").setValue(String.valueOf(tappedImage));
            else mDatabaseGame.child("email_request").setValue(String.valueOf(tappedImage));

            img.setTranslationY(-1000f);
            if(activePlayer == 0){
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                //O's Turn
                statusString = lgameID.split("_")[1]+"'s Turn";
                status.setText(statusString);
                counter++;
            }

            else{
                img.setImageResource(R.drawable.o);
                activePlayer = 0;
                //"X's turn"
                statusString = lgameID.split("_")[0]+"'s Turn";
                status.setText(statusString);
                counter++;
            }
            img.animate().translationYBy(1000f).setDuration(300);
        }
        someoneWon(statusString);
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
                    if(playerType==0){
                        winCount++;
                        totalWinText.setText(String.valueOf(winCount));
                        winner = lmail_cleaned + " has Won";
                    }
                    else {
                        winner = finalOppositePlayer + " has Won";
                    }
                }
                else{
                    if(playerType==1){
                        winCount++;
                        totalWinText.setText(String.valueOf(winCount));
                        winner = lmail_cleaned + " has Won";
                    }
                    else {
                        winner = finalOppositePlayer + " has Won";
                    }
                }
            }
        }


        if(winner.equals(a)) return false;
        else{
            gameActive = false;
            openDialog(winner);
            return true;
        }
    }

    public void resetGame(){
        gameActive = false;
        turnChanger = turnChanger^1;

        mDatabaseGame = FirebaseDatabase.getInstance().getReference("online_game").child(lgameID);
        if(lplayerType.equals("acceptor")) {
            if(turnChanger==0)
                status.setText(lmail_cleaned+"'s Turn");
            else
                status.setText(finalOppositePlayer+"'s Turn");

            mDatabaseGame.child("email_accept").setValue(lmail_cleaned);
            mDatabaseGame.child("email_request").setValue(finalOppositePlayer);

        }
        else{
            if(turnChanger==0)
                status.setText(finalOppositePlayer+"'s Turn");
            else
                status.setText(lmail_cleaned+"'s Turn");

            mDatabaseGame.child("email_accept").setValue(finalOppositePlayer);
            mDatabaseGame.child("email_request").setValue(lmail_cleaned);

        }
        counter = 0;
        activePlayer = turnChanger;
        gameCount++;
        gameCountText.setText(String.valueOf(gameCount));
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
//        gameActive = true;
//        mDatabaseUser.child("accept").setValue("");
//        mDatabaseGame.child("online_game").child(lgameID).removeValue();

    }

    public void openDialog(String toSet){
        Dialog dialog = new Dialog(toSet, lmail_cleaned, lgameID);
        dialog.show(getSupportFragmentManager(), "message dialog");
        resetGame();
    }

    public static Play playEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        playEnd = this;
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_play);

        opponentMessage = findViewById(R.id.opponentMessage);
        message = findViewById(R.id.message);

        gameCountText = findViewById(R.id.gameCountText);
        gameCountText.setText(String.valueOf(gameCount));

        totalWinText = findViewById(R.id.totalWinText);
        totalWinText.setText(String.valueOf(winCount));

        status = findViewById(R.id.status);
        System.out.println("Help");

        SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);
        lusername = preferences.getString("username","");
        lmail = preferences.getString("emailID" , "");
        luserimage = preferences.getString("userPhoto" , "");
        lplayerType = preferences.getString("playerType" , "");
        Log.d("lplayerType", lplayerType);
        lgameID = preferences.getString("gameID", "");

        lmail_cleaned = lmail.split("@")[0];
        lmail_cleaned = lmail_cleaned.replace(".","");
        lmail_cleaned = lmail_cleaned.replace("$","");
        lmail_cleaned = lmail_cleaned.replace("#","");
        lmail_cleaned = lmail_cleaned.replace("[","");
        lmail_cleaned = lmail_cleaned.replace("]","");
        lmail_cleaned = lmail_cleaned.replace("/","");

        if(lplayerType.equals("acceptor"))
        {
            playerType=0;
            status.setText(lmail_cleaned+"'s Turn");
        }
        else
        {
            playerType=1;
            status.setText(lgameID.split("_")[0]+"'s Turn");
        }

        Log.d("check acceprter" ,String.valueOf(playerType) );

        mDatabaseUser = FirebaseDatabase.getInstance().getReference("online_users").child(lmail_cleaned);
        System.out.println(lgameID);
        mDatabaseGame = FirebaseDatabase.getInstance().getReference("online_game").child(lgameID);


        String oppositePlayer = lgameID.replace(lmail_cleaned,"");
        oppositePlayer = oppositePlayer.replace("_","");

        finalOppositePlayer = oppositePlayer;
        System.out.println(finalOppositePlayer);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String statusString=status.getText().toString();
                // Get Post object and use the values to update the UI
                OnlineGame post = dataSnapshot.getValue(OnlineGame.class);
                System.out.println("Post"+ post);
                if(post==null && init)
                {
                    System.out.println("No activity");
                    Toast.makeText(getApplicationContext(), "Opponent Left the Game!", Toast.LENGTH_SHORT).show();
                    Play.this.finish();
                }
                else if(lplayerType.equals("acceptor")) {
                    if(!post.request_msg.isEmpty())
                        opponentMessage.setText(finalOppositePlayer+": "+post.request_msg);
                    if (!post.email_request.equals(finalOppositePlayer)) {
                        if(gameState[Integer.parseInt(post.email_request)]==2 && gameActive) {
                            gameState[Integer.parseInt(post.email_request)] = playerType^1;

                            ImageView img1 = findViewById(getResources().getIdentifier("imageView" + post.email_request, "id", getPackageName()));

                            img1.setTranslationY(-1000f);
                            if (activePlayer == 0) {
                                img1.setImageResource(R.drawable.x);
                                activePlayer = 1;
                                // O's Turn
                                statusString = lmail_cleaned+"'s Turn";
                                status.setText(statusString);
                                counter++;
                            } else {
                                img1.setImageResource(R.drawable.o);
                                activePlayer = 0;
                                // X's turn"
                                statusString = lmail_cleaned+"'s Turn";
                                status.setText(statusString);
                                counter++;
                            }
                            img1.animate().translationYBy(1000f).setDuration(300);
                        }

                    }
                    else gameActive = true;
                }
                else{
                    if(!post.accept_msg.isEmpty())
                        opponentMessage.setText(finalOppositePlayer+": "+post.accept_msg);
                    if (!post.email_accept.equals(finalOppositePlayer)) {
                        if(gameState[Integer.parseInt(post.email_accept)]==2 && gameActive) {
                            gameState[Integer.parseInt(post.email_accept)] = playerType^1;

                            ImageView img1 = findViewById(getResources().getIdentifier("imageView" + post.email_accept, "id", getPackageName()));

                            img1.setTranslationY(-1000f);
                            if (activePlayer == 0) {
                                img1.setImageResource(R.drawable.x);
                                activePlayer = 1;
                                //O's Turn
                                statusString = lmail_cleaned+"'s Turn";
                                status.setText(statusString);
                                counter++;
                            } else {
                                img1.setImageResource(R.drawable.o);
                                activePlayer = 0;
                                //"X's turn"
                                statusString = lmail_cleaned+"'s Turn";
                                status.setText(statusString);
                                counter++;
                            }
                            img1.animate().translationYBy(1000f).setDuration(300);
                        }

                    }
                    else gameActive = true;
                }
                init = true;
                someoneWon(statusString);
                if(!someoneWon(statusString) && counter==9){
                    gameActive = false;
                    openDialog("Game is Draw");
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabaseGame.addValueEventListener(postListener);

        sendMsg = findViewById(R.id.sendMsg);
        sendMsg.setOnClickListener(view->{
            String msg  = message.getText().toString();
            if(!msg.isEmpty())
            {
                if(playerType==0)
                    mDatabaseGame.child("accept_msg").setValue(msg);
                else
                    mDatabaseGame.child("request_msg").setValue(msg);
                message.setText("");
            }
        });
    }

    @Override
    protected void onPause() {

        super.onPause();
//        mDatabaseUser.child("accept").setValue("");
        mDatabaseGame.removeValue();

    }
}