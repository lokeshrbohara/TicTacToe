package com.example.tictactoe;

public class OnlineGame {
    public String email_request;
    public String email_accept;
    public String request_msg;
    public String accept_msg;

    public OnlineGame() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public OnlineGame(String accept, String request, String a_msg, String r_msg) {
        this.email_request = request;
        this.email_accept = accept;
        this.request_msg = r_msg;
        this.accept_msg  = a_msg;
    }
}
