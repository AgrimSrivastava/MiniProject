package com.myapp.theagrim.torguo;

public class InstantMessage {
    private String message;
    private int author;

    public InstantMessage(String message,int author){
        this.message=message;
        this.author=author;
    }

    public InstantMessage(){}

    String getMessage(){
        return message;
    }

    public int getAuthor() {
        return author;
    }
}
