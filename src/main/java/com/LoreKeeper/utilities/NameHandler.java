package com.LoreKeeper.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.LoreKeeper.exceptions.NameHandlerException;


public class NameHandler{
    private ArrayList<String[]> fileContents;
    private File tempFile;
    public NameHandler(File aFile) throws FileNotFoundException{
        fileContents = new ArrayList<String[]>();
        tempFile = aFile;
        Scanner toArray = new Scanner(tempFile);
        while(toArray.hasNextLine()){
            fileContents.add(toArray.nextLine().split("\\s\\$\\s"));
        }
        toArray.close();
    }

    public String getName(long id) throws NameHandlerException{
        String retString = "";
        String[] userNickname = searchUserID(id);
        for(int i = 1; i<userNickname.length;i++){
            retString+=userNickname[i]+" ";
        }
        return retString;
    }

    public boolean hasName(long id){
        try{
            searchUserID(id);
            return true;
        }
        catch (NameHandlerException e){
            return false;
        }
    }

    private String[] searchUserID(long id) throws NameHandlerException{
        if(!(fileContents == null)){
            for(String[] iter : fileContents){
                if(Long.parseLong(iter[0]) == id){
                    return iter;
                }
            }
        }
        throw new NameHandlerException("Name Not Found!");
    }

    /**
     * Sets a nickname for the specified user.
     * @param id
     * @param newName
     */
    public void setName(long id, String newName) throws NameHandlerException{
        System.out.println(newName);
        if(newName.contains(" $ ")){
            throw new NameHandlerException("Invalid name, please do not use '$' in your name");
        }
        try{
            searchUserID(id)[1] = newName;
        }
        catch(NameHandlerException e){ 
            String[] tempString = {""+id,newName};
            System.out.println(tempString);
            fileContents.add(tempString);
        }
        try {
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() throws IOException {
        tempFile.setWritable(true);
        FileWriter fWrite = new FileWriter(tempFile, false);
        for(String[] iter : fileContents){
            fWrite.write(iter[0] + " $ " + iter[1] +"\n");
        }
        // Scanner toArray = new Scanner(tempFile); // For debugging
        // while(toArray.hasNextLine()){ 
        //     System.out.println(toArray.nextLine()); 
        // }
        // toArray.close();
        fWrite.close();
    }

}
