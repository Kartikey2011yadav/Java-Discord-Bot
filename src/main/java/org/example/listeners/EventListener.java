package org.example.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

/**
 * Listens for events and responds with our custom code.
 *
 * @author TechnoVision
 */
public class EventListener extends ListenerAdapter {

    /**
     * Event fires when an emoji reaction is added to a message.
     */
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
//        User user = event.getUser();
//        String jumpLink = event.getJumpUrl();
//        String emoji = event.getReaction().clearReactions().getAsReactionCode();
//        String channel = event.getChannel().getAsMention();

//        String message = user.getAsTag() + " reacted to a [message]("+jumpLink+") with " + emoji + " in the " + channel + " channel!";
//        event.getGuild().getDefaultChannel().getManager().queue();
    }

    /**
     * Event fires when a message is sent in discord.
     * Will require "Guild Messages" gateway intent after August 2022!
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.contains("Nathkhat")) {
            System.out.println(event.getAuthor().getName());
            Member m = event.getMessage().getMentions().getMembers().get(0);
            System.out.println(m.getUser().getName());
            event.getChannel().sendMessage("Koham Sothari").queue();
        }
        else if (message.contains("emojiiiiii")) {
            event.getMessage().delete().queue();
            File file = new File("src/main/java/org/example/listeners/image.png");
            event.getChannel().sendFiles(FileUpload.fromData(file)).queue();
        } else if (message.contains("Emoji")) {
            event.getMessage().delete().queue();
//            EmbedBuilder builder = new EmbedBuilder().setTitle("TEST");
//            builder.setImage("https://raw.githubusercontent.com/Kartikey2011yadav/Java-Discord-Bot/master/src/main/java/org/example/assets/image.png");
//            builder.setColor(Color.BLUE);
//            event.getChannel().sendMessageEmbeds(builder.build()).queue();
//            event.getMessage().reply("hello").setMessageReference("1213397174520643654");
            event.getChannel().sendMessage("hello 1").setMessageReference("1213137850552291409").queue();
            event.getChannel().sendMessage("hello 2").setMessageReference("1213148332336091187").queue();

        } else if (message.contains("!emoji")) {
            System.out.println(event.getAuthor().getName());
            Member m = event.getMessage().getMentions().getMembers().get(0);
            System.out.println(event.getRawData());
            System.out.println(m.getUser().getName());
            event.getMessage().delete().queue();
            EmbedBuilder builder = new EmbedBuilder().setTitle("TEST");
            builder.setImage("https://raw.githubusercontent.com/Kartikey2011yadav/Java-Discord-Bot/master/src/main/java/org/example/assets/image.png");
            builder.setColor(Color.BLUE);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();

        } else if (message.contains("meme")) {
                event.getMessage().delete().queue();
                String line,postLink="",link="",title="";
            try {
                URL url = new URL("https://meme-api.com/gimme");
                BufferedReader bf = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
                JSONParser parser = new JSONParser();
                while ((line = bf.readLine() )!= null){
                    JSONArray array = new JSONArray();
                    array.add(parser.parse(line));
                    for (Object o:array) {
                        JSONObject jo = (JSONObject) o;
                        postLink = (String) jo.get("postLink");
                        link = (String) jo.get("url");
                        title = (String) jo.get("title");
                    }
                }
                bf.close();

                EmbedBuilder builder = new EmbedBuilder().setTitle(title, postLink);
                builder.setImage(link);
                builder.setColor(Color.BLUE);
                event.getChannel().sendMessageEmbeds(builder.build()).queue();


            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
//            event.getChannel().sendMessage(link+" Post: "+postLink).queue();
        }

    }

    /**
     * Event fires when a new member joins a guild
     * Requires "Guild Members" gateway intent!
     */
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Role role = event.getGuild().getRoleById(988342442430443540L);
        if (role != null) {
            event.getGuild().addRoleToMember(event.getMember(), role).queue();
        }
    }

    /**
     * Event fires when a user updates their online status
     * Requires "Guild Presences" gateway intent AND cache enabled!
     */
//    @Override
//    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
//        int onlineMembers = 0;
//        for (Member member : event.getGuild().getMembers()) {
//            if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
//                onlineMembers++;
//            }
//        }
//        String message = event.getUser().getAsTag()+"updated their online status! There are "+onlineMembers+" members online now!";
//        event.getGuild().getDefaultChannel().getManager().queue();
//    }
}
