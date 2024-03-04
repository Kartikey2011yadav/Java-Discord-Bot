package org.example.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
            String userTag = event.getUser().getName();
            event.reply("Welcome to the server, **" + userTag + "**!").queue();
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
        else if (command.equals("roles")) {
            // run the 'roles' command
            event.deferReply().queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()) {
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
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
        commandData.add(Commands.slash("food", "select food"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }


}