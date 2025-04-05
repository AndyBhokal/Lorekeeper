package com.LoreKeeper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.LoreKeeper.exceptions.AchievementHandlerException;
import com.LoreKeeper.exceptions.CommandHandlerException;
import com.LoreKeeper.exceptions.EntryListException;
import com.LoreKeeper.exceptions.NameHandlerException;
import com.LoreKeeper.objects.Entry;
import com.LoreKeeper.objects.EntryList;
import com.LoreKeeper.utilities.AchievementHandler;
import com.LoreKeeper.utilities.CommandHandler;
import com.LoreKeeper.utilities.NameHandler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
// import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class Main extends ListenerAdapter{

    // EmbedFormatter msgFormat;
    static JDA loreKeeperBot;
    static boolean active = false;
    EntryList logs;
    static NameHandler nameHandler;
    static CommandHandler commandHandler;
    static AchievementHandler achievementHandler;
    static ArrayList<String> finalMessage;
    
    public static void main(String[] args) {
        File configFile= new File("cppbot\\config.txt");;
        try{
            configFile.createNewFile();
        }
        catch(IOException e){
            System.err.println(e.getStackTrace());
        }


        String token = "";
        JDABuilder loreKeeperBotBuilder = JDABuilder.createDefault(token);
        loreKeeperBotBuilder.setStatus(OnlineStatus.ONLINE);
        loreKeeperBotBuilder.setActivity(Activity.watching("you all roll Nat 1s"));
        loreKeeperBotBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.DIRECT_MESSAGES,GatewayIntent.MESSAGE_CONTENT);
        loreKeeperBotBuilder.addEventListeners(new Main());
        loreKeeperBot = loreKeeperBotBuilder.build();

        try {
            nameHandler = new NameHandler(new File("cppbot\\data\\Users.txt"));
            commandHandler = new CommandHandler(new File(    "cppbot\\data\\Commands.txt"));
            achievementHandler = new AchievementHandler(new File("cppbot\\data\\Achievements.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("Can't find the File: " + e);
        }
    }

    @Override 
    public void onReady(ReadyEvent event) {
        System.out.println("I am ready to go!");
    } 
    
    @Override 
    public void onMessageReceived(MessageReceivedEvent event) {      
        String messageContent = event.getMessage().getContentRaw().toString();
        MessageChannel channel = event.getChannel();
        User user = event.getAuthor();
        if(user.isBot()){
            return;
        }

        if (messageContent.charAt(0) == ';'){
            System.out.println("command");
            
            String messageArg = messageContent.substring(messageContent.indexOf(" ")+1);

            switch(messageContent.substring(1,(messageContent.contains(" ") ? messageContent.indexOf(" ") : messageContent.length()))){
                case "ach":
                case "achievement":
                    System.out.println("achievements");
                    if(messageArg.charAt(0) == ';'){
                        channel.sendMessage("Proper format for this command: ;achievement <achievement name>").queue();
                    }
                    else{
                        try{
                            channel.sendMessage(achievementHandler.achievementFormat(achievementHandler.searchAchievement(messageArg))).queue();
                        }catch(AchievementHandlerException e){
                            channel.sendMessage(errorOut(e)).queue();
                        }
                    }
                    break;

                case "add":
                    if(checkBotActive(messageContent,channel)){
                        if(messageContent.indexOf(" ") == -1){  //adding blank message
                            channel.sendMessage("Proper format for this command: ;add <message>.").queue();
                        }
                        // msgFormat.addDesc(userName(user), messageContent.substring(messageContent.indexOf(" ")+1)); //old way, using Embeds
                        try{
                            logs.add(new Entry(userName(user),messageArg));
                            channel.sendMessage("Added to the logs").queue();
                        }catch(EntryListException e){
                            channel.sendMessage(errorOut(e)).queue();
                        }
                    }
                    break;

                case "edit":
                    if(checkBotActive(messageContent,channel)){
                        try{
                            logs.edit(messageContent.substring(messageContent.indexOf(" ")+1));
                            channel.sendMessage("Edited Previous Message").queue();
                        }catch(EntryListException e){
                            channel.sendMessage(errorOut(e)).queue();
                        }
                    }
                    break;

                case "end":
                    if(checkBotActive(messageContent,channel)){
                        
                        messageBuild(logs);
                        for(String outString : finalMessage){
                            event.getGuild().getTextChannelById(1076351283834916945l).sendMessage(outString).queue();
                        }
                        channel.sendMessage("Posted Logs").queue();
                        active = false;
                        try{
                            nameHandler.update();
                        }
                        catch(IOException e){
                            channel.sendMessage(e.toString());
                        }
                    }
                    break;

                    case "help":
                        // System.out.println(messageArg);
                        if(messageArg.charAt(0) == ';'){
                            user.openPrivateChannel().flatMap(channel1 -> channel1.sendMessage(commandHandler.getAllCommands())).queue();
                        }
                        else{
                            try{
                                channel.sendMessage(commandHandler.getCommand(messageArg)).queue();
                            }
                            catch(CommandHandlerException e){
                                channel.sendMessage(errorOut(e)).queue();
                            }
                        }
                        break;

                case "nick":
                    try {
                        nameHandler.setName(user.getIdLong(), messageArg);
                        channel.sendMessage("Your new name is "+messageArg).queue();
                    } catch (NameHandlerException e) {
                        channel.sendMessage(errorOut(e)).queue();;
                    }

                    break;

                case "remove":
                    if(checkBotActive(messageContent,channel)){
                        try{
                            logs.remove();
                            channel.sendMessage("Removed from the logs").queue();
                        }catch(EntryListException e){
                            channel.sendMessage(errorOut(e)).queue();
                        }
                    }
                    break;

                case "start":
                    if(!active){  
                        // channel.sendMessage(new EmbedFormatter().toMessage()).queue();
                        // msgFormat = new EmbedFormatter("D&D Session - "+dateStr); //Old way using Embeds
                        finalMessage = new ArrayList<String>();
                        logs = new EntryList();
                        active = true;
                        
                        channel.sendMessage("Today's Achievements of the day: \n"+AchievementHandler.dailyAchievements).queue();
                    }
                    break;


                case "undo":
                    if(checkBotActive(messageContent,channel)){
                        try{
                            logs.undo();
                            channel.sendMessage("Undone").queue();
                        }catch(EntryListException e){
                            channel.sendMessage(errorOut(e)).queue();
                        }
                    }
                    break;

                case "whoami":
                    try{
                        String tempName = nameHandler.getName(user.getIdLong());
                        channel.sendMessage(tempName).queue();
                    }
                    catch(NameHandlerException e){
                        channel.sendMessage(errorOut(e)).queue();
                    }
                    break;

                case "whois":
                    try{
                        String tempName = nameHandler.getName(event.getMessage().getMentions().getUsers().get(0).getIdLong());
                        channel.sendMessage(tempName).queue();
                    }
                    catch(NameHandlerException e){
                        channel.sendMessage(errorOut(e)).queue();
                    }
                    break;

                default:
                    channel.sendMessage("What'd you say?").queue();
                    break;
            }

        }
    }

    public static boolean checkBotActive(String messageContent, MessageChannel channel){
        if(!active && !messageContent.substring(1).equals("start")){    //bot is not set to log messages.
            channel.sendMessage("Bot is not activated, type ;start to start listening to messages.").queue();
            return false;
        }
        return true;
    }

    public static void messageBuild(EntryList e){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        String retString = formatter.format(date)+"\n";

        for(Entry e1 : e){
            if(retString.length()+e1.toString().length() >= Message.MAX_CONTENT_LENGTH){

                finalMessage.add(retString);
                retString = "";
            }
            retString += e1.toString();
            System.out.println(retString);
        }
        finalMessage.add(retString);
        System.out.println(finalMessage.toString());
    }

    public static String errorOut(Exception e){
        return e.toString().substring(e.toString().lastIndexOf(": ")+2);
        
    }

    public static String userName(User u){
        String retString = u.getName();
        try{
            retString = nameHandler.getName(u.getIdLong());
        }
        catch(NameHandlerException e){}
        return retString;
    }
}