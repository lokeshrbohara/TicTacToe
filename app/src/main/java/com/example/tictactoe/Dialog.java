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

public class Dialog extends AppCompatDialogFragment {

    public TextView st;
    String toDisplay;
    public Dialog(String toSet){
        this.toDisplay = toSet;
    }
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_win, null);

        builder.setView(view)
                .setTitle("Result")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Play.fa.finish();
                        Intent intent = new Intent(getActivity(), Play.class);
                        startActivity(intent);
                        
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Play.fa.finish();
                        Intent intent = new Intent(getActivity(), Play.class);
                        startActivity(intent);
                    }
                });
        st = view.findViewById(R.id.result);
        st.setText(toDisplay);
        return builder.create();
    }
}
