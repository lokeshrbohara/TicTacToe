package com.example.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dialog extends AppCompatDialogFragment {

    public TextView st;
    private DatabaseReference mDatabaseUser;
    private DatabaseReference mDatabaseGame;
    String userId;
    String toDisplay;
    String gameId;
    public Dialog(String toSet, String userId, String gameId){
        this.toDisplay = toSet;
        this.userId = userId;
        this.gameId = gameId;
    }
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_win, null);

        builder.setView(view)
                .setTitle("Result")
                .setNegativeButton("End Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Play.playEnd.finish();
                        mDatabaseUser = FirebaseDatabase.getInstance().getReference("online_users").child(userId);
                        mDatabaseGame = FirebaseDatabase.getInstance().getReference("online_game").child(gameId);

                        mDatabaseUser.child("accept").setValue("");
                        mDatabaseGame.removeValue();

                        
                    }
                })
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        st = view.findViewById(R.id.result);
        st.setText(toDisplay);
        return builder.create();
    }
}
