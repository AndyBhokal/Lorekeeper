package com.LoreKeeper.objects;

public class Entry {
    private String sender,messageContent;
    private int token;
    private static int numTokens = 0;

    public Entry(String sender, String messageContent){
        this.sender = sender;
        this.messageContent = messageContent;
        this.token = numTokens;
        numTokens++;
    }

    public void edit(String message){
        this.messageContent = message;
    }

    public int getToken(){
        return token;
    }

    public String getMessage(){
        return messageContent;
    }

    public String toString(){
        return "**"+sender+"**\n"+messageContent+"\n\n";
    }
}
