package com.LoreKeeper;
// import net.dv8tion.jda.api.JDA;
// import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
// import net.dv8tion.jda.api.OnlineStatus;
// import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
// import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * 
 *
 */
 public class MessageListener extends ListenerAdapter {
    public static void main( String[] args ) {
        // JDABuilder cppBotBuilder = JDABuilder.createDefault("MTA3NDkyNzg4NzY0OTYwMzY4NA.GtDpXx.WGXl_Kb1HmE66EfGJIF-UtSqOA_x74BC4NlJZg");
        // cppBotBuilder.setStatus(OnlineStatus.ONLINE);
        // cppBotBuilder.setActivity(Activity.watching("the C++ Standard"));
        // cppBotBuilder.enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.DIRECT_MESSAGES,GatewayIntent.MESSAGE_CONTENT);
        // cppBotBuilder.addEventListeners(new MessageListener());
        // // JDA cppBot = cppBotBuilder.build();
    }
    
    @Override 
    public void onReady(ReadyEvent event) {
        System.out.println("I am ready to go!");
    } 
    
    @Override 
    public void onMessageReceived(MessageReceivedEvent event) {
        
        String messageContent = event.getMessage().getContentDisplay().toString();
        // System.out.printf("[%s]: %s\n", event.getAuthor().getName(), event.getMessage().getContentDisplay());
        System.out.println(messageContent.charAt(0));
        if (messageContent.charAt(0) == ';'){
            switch(messageContent.substring(1,(messageContent.contains(" ") ? messageContent.indexOf(" ") : messageContent.length()))){
                case "com":
                    System.out.println("Command Command.");
                    break;
                default:
                    System.out.println("command unrecognized");
            }
        }
    }
}
