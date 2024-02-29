package org.example.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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
            event.getChannel().sendMessage("Koham Sothari").queue();
        }
        else if (message.contains("emoji")) {
            event.getChannel().sendMessage("\uD83E\uDD23").queue();
        }
        else if (message.contains("$Meme")) {
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
                event.getMessage().delete().queue();

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
    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        int onlineMembers = 0;
        for (Member member : event.getGuild().getMembers()) {
            if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
                onlineMembers++;
            }
        }
        String message = event.getUser().getAsTag()+"updated their online status! There are "+onlineMembers+" members online now!";
        event.getGuild().getDefaultChannel().getManager().queue();
    }
}
