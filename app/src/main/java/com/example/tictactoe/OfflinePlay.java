package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class OfflinePlay extends AppCompatActivity {

    boolean gameActive = true;
    int counter = 0;
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
        if(gameState[tappedImage] == 2){
            gameState[tappedImage] = activePlayer;

            img.setTranslationY(-1000f);
            if(activePlayer == 0){
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                statusString = "O's turn";
                status.setText(statusString);
                counter++;
            }
            else{
                img.setImageResource(R.drawable.o);
                activePlayer = 0;
                statusString = "X's turn";
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
        OfflineDialog dialog = new OfflineDialog(toSet);
        dialog.show(getSupportFragmentManager(), "message dialog");
        resetGame();
    }


    public static OfflinePlay offlinePlayEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        offlinePlayEnd = this;
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_offline_play);
    }
}