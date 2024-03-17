package org.example.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.AttachedFile;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Registers and manages slash commands.
 *
 * @author TechnoVision
 */
public class CommandManager extends ListenerAdapter {

    /**
     * Listens for slash commands and responds accordingly
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("welcome")) {
            // Run the 'ping' command
            String userTag = event.getUser().getAsTag();
            event.reply("Welcome to the server, **" + userTag + "**!").queue();
        }
        else if (command.equals("roles")) {
            // run the 'roles' command
            event.deferReply().queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()) {
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
        }
        else if (command.equals("say")) {
            // Get message option
            OptionMapping messageOption = event.getOption("message");
            String message = messageOption.getAsString();

            // Get channel option if specified
            MessageChannel channel;
            OptionMapping channelOption = event.getOption("channel");
            if (channelOption != null) {
                channel = channelOption.getAsChannel().asGuildMessageChannel();
            } else {
                channel = event.getChannel();
            }

            // Send message
            channel.sendMessage(message).queue();
            event.reply("Your message was sent!").setEphemeral(true).queue();
        }
        else if (command.equals("emoji")) {
            // Get message option
            OptionMapping messageOption = event.getOption("options");
            assert messageOption != null;

            Message.Attachment file = messageOption.getAsAttachment();
            System.out.println(file.getUrl());
            // Send message
            EmbedBuilder builder = new EmbedBuilder().setTitle("TEST");
            builder.setImage(file.getUrl());

            builder.setColor(Color.BLUE);
//            event.getChannel().sendMessageEmbeds(builder.build()).queue();
//            event.getChannel().send
            event.reply("Yousayr message was sent!").setEphemeral(true).queue();
        }
        else if (command.equals("giverole")) {
            Member member = event.getOption("user").getAsMember();
            Role role = event.getOption("role").getAsRole();
            event.getGuild().addRoleToMember(member, role).queue();
            event.reply(member.getAsMention() + " has been given the " + role.getAsMention() + " role!").queue();
        }
        else if (command.equals("emote")) {
            OptionMapping option = event.getOption("type");
            String type = option.getAsString();

            String replyMessage = "";
            switch (type.toLowerCase()) {
                case "hug" -> {
                    replyMessage = "You hug the closest person to you.";
                }
                case "laugh" -> {
                    replyMessage = "You laugh hysterically at everyone around you.";
                }
                case "cry" -> {
                    replyMessage = "You can't stop crying";
                }
            }
            event.reply(replyMessage).queue();
        }
        else if (command.equals("food")) {
            event.reply("Choose your favorite food")
                    .setEphemeral(true)
                    .addActionRow(
                            StringSelectMenu.create("choose-food")
                                    .addOptions(SelectOption.of("Hamburger", "hamburger") // another way to create a SelectOption
                                            .withDescription("Tasty") // this time with a description
                                            .withDefault(true)) // while also being the default option
                                    .build())
                    .queue();
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("choose-food")) {
            event.reply("You chose " + event.getValues().get(0)).queue();
        }
    }

    /**
     * Registers slash commands as GUILD commands (max 100).
     * These commands will update instantly and are great for testing.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot"));
        commandData.add(Commands.slash("roles", "Display all roles on the server"));

        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "The channel you want to send this message in")
                .setChannelTypes(ChannelType.TEXT, ChannelType.NEWS, ChannelType.GUILD_PUBLIC_THREAD);
        commandData.add(Commands.slash("say", "Make the bot say a message").addOptions(option1, option2));

        OptionData op = new OptionData(OptionType.ATTACHMENT,"options","Choose any one",true);
        commandData.add(Commands.slash("emoji","select emoji").addOptions(op));

        OptionData option3 = new OptionData(OptionType.STRING, "type", "The type of emotion to express", true)
                .addChoice("Hug", "hug")
                .addChoice("Laugh", "laugh")
                .addChoice("Cry", "cry");
        commandData.add(Commands.slash("emote", "Express your emotions through text.").addOptions(option3));

        OptionData option4 = new OptionData(OptionType.USER, "user", "The user to give the role to", true);
        OptionData option5 = new OptionData(OptionType.ROLE, "role", "The role to be given", true);
        commandData.add(Commands.slash("giverole", "Give a user a role").addOptions(option4, option5));

        commandData.add(Commands.slash("food", "select food"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }


}