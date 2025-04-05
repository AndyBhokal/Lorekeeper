package com.LoreKeeper.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

import com.LoreKeeper.exceptions.AchievementHandlerException;

public class AchievementHandler {
    private ArrayList<String[]> fileContents;
    private File tempFile;
    public static String dailyAchievements;

    public AchievementHandler(File aFile) throws FileNotFoundException{
        dailyAchievements = "";
        fileContents = new ArrayList<String[]>();
        tempFile = aFile;
        Scanner toArray = new Scanner(tempFile);
        while(toArray.hasNextLine()){
            fileContents.add(toArray.nextLine().split("\\s\\$\\s"));
        }
        toArray.close();

        Random rand = new Random();
        for(int i = 0; i<3;i++){
            String[] tempAchString = fileContents.get(rand.nextInt(fileContents.size()));
            dailyAchievements+=(achievementFormat(tempAchString)+"\n");
        }
    }

    public String achievementFormat(String[] rawString){
        return String.format("%s - **%s**: %s", rawString[0], rawString[1], rawString[2]);
    }

    /**
     * @param key
     * @return Raw achievement data. 
     * @throws AchievementHandlerException
     * 
     * Performs a Binary Search of the total list of achievements and returns the achievement that matches the given key.
     */
    public String[] searchAchievement(String key) throws AchievementHandlerException{
        System.out.printf("Key: %s\n",key);
        int comp = 0, left = 0, right = fileContents.size(), center = left + (right - left) / 2;
        String tempString;
        while(left <= right){
            center = left + (right - left) / 2;
            System.out.printf("center: %3d, Entry: %s\n", center, fileContents.get(center)[0]);
            tempString = fileContents.get(center)[0];
            comp = key.compareToIgnoreCase(tempString);
            if(comp == 0){
                return fileContents.get(center);
            }
            else if(comp < 0){
                right = center - 1;
            }
            else{
                left = center + 1;
            }
        }
        throw new AchievementHandlerException("Achievement Not Found!");
    }
}