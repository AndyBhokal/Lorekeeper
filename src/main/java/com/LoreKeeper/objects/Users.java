package com.LoreKeeper.objects;

import net.dv8tion.jda.api.entities.User;

public class Users {
    private User discordUser;
    private String logName;
    private int plotPoints;

    public Users(User initUser){
        this(initUser, "", 0);
    }

    public Users(User initUser, String initLogName){
        this(initUser, "", 0);
    }

    public Users(User initUser, String initLogName, int initPlotPoints){
        discordUser = initUser;
        logName = initLogName;
        plotPoints = initPlotPoints;
    }

    public int getPlotPoints(){
        return plotPoints;
    }

    public void addPlotPoints(int newPlotPoints){
        plotPoints += newPlotPoints;
    }

    public void spendPlotPoints(int newPlotPoints){
        plotPoints -= newPlotPoints;
    }

    public void setLogName(String newName){
        logName = newName;
    }

    public String getLogName(){
        return logName;
    }

    public boolean verifyUser(User verifUser){
        return discordUser.getIdLong() == verifUser.getIdLong();
    }
}
