package com.example.tictactoe;

public class OnlineUser {
    public String accept;
    public String request;

    public OnlineUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public OnlineUser(String accept1, String request1) {
        this.accept = accept1;
        this.request = request1;
    }
}
