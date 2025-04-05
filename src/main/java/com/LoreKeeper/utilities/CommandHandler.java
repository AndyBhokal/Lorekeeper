package com.LoreKeeper.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.LoreKeeper.exceptions.CommandHandlerException;

import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class CommandHandler {
    private ArrayList<String[]> fileContents;
    private File tempFile;
    private MessageCreateData allCommands;

    public CommandHandler(File aFile) throws FileNotFoundException{
        fileContents = new ArrayList<String[]>();
        tempFile = aFile;
        Scanner toArray = new Scanner(tempFile);
        while(toArray.hasNextLine()){
            fileContents.add(toArray.nextLine().split("\\s\\$\\s"));
        }
        toArray.close();
        allCommands = noArgHelpCommand();
    }

    public MessageCreateData getCommand(String key) throws CommandHandlerException{
        String[] tempArr = searchCommand(key);
        EmbedFormatter mbd = new EmbedFormatter(tempArr[0], 255128);
        for(int i = 1; i<tempArr.length; i+=2){
            mbd.addDesc(tempArr[i], tempArr[i+1]);
        }
        return mbd.toMessage();
    }

    private String[] searchCommand(String key) throws CommandHandlerException{
        if(!(fileContents == null)){
            for(String[] iter : fileContents){
                if(iter[0].equalsIgnoreCase(key)){
                    return iter;
                }
            }
        }
        throw new CommandHandlerException("Command Not Found!");
    }

    private MessageCreateData noArgHelpCommand(){
        EmbedFormatter mbd = new EmbedFormatter("Command List", 255128);
        for(String[] tempArr : fileContents){
            for(int i = 1; i<tempArr.length; i+=2){
                mbd.addDesc(tempArr[i], tempArr[i+1]);
            }
        }
        return mbd.toMessage();
    }

    public MessageCreateData getAllCommands() {
        return allCommands;
    }
}
