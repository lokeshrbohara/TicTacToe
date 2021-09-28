package com.example.tictactoe;

public class OnlineGame {
    public String email_request;
    public String email_accept;

    public OnlineGame() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public OnlineGame(String accept, String request) {
        this.email_request = request;
        this.email_accept = accept;
    }
}
