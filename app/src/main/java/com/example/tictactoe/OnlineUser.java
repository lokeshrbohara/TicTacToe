package com.example.tictactoe;

public class OnlineUser {
    public String accept;
    public String request;

    public OnlineUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public OnlineUser(String accept, String request) {
        this.accept = accept;
        this.request = request;
    }
}
