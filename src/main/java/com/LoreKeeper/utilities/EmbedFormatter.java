package com.LoreKeeper.utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class EmbedFormatter {
    EmbedBuilder mbd;
    public EmbedFormatter(){
        this("Embed"
            ,255255255);
    }
    public EmbedFormatter(String Name){
        mbd = new EmbedBuilder().setTitle(Name);
    }
    public EmbedFormatter(String Name, int color){
        mbd = new EmbedBuilder()
                            .setTitle(Name)
                            .setColor(color);

    }
    public MessageCreateData toMessage(){
        return new MessageCreateBuilder().setEmbeds(getEmbed()).build();
    }

    public void addDesc(){
        addDesc("name","value");
    }
    public void addDesc(String name, String value){
        mbd.addField(name, value, false);
    }
    public MessageEmbed getEmbed(){
        return mbd.build();
    }
}
