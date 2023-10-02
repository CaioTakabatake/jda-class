package com.caiotakabatake.jdaclass.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("welcome")) {
            String userTag = event.getUser().getName();
            event.reply("Welcome to the server, **" + userTag + "**!").setEphemeral(true).queue();
        } else if (command.equals("roles")) {
            event.deferReply().setEphemeral(true).queue();
            StringBuilder response = new StringBuilder();
            for (Role role : event.getGuild().getRoles()) {
                response.append(role.getAsMention()).append("\n");
            }
            event.getHook().sendMessage(response.toString()).queue();
        } else if (command.equals("say")) {
            OptionMapping messageOption = event.getOption("message");
            String message = messageOption.getAsString();

            MessageChannel channel = event.getChannel();
            OptionMapping channelOption = event.getOption("channel");

            if (channelOption != null) {
                channel = channelOption.getAsChannel().asTextChannel();
            }

            channel.sendMessage(message).queue();
            event.reply("Your message was sent!").setEphemeral(true).queue();
        } else if (command.equals("emote")) {
            OptionMapping option = event.getOption("type");
            String type = option.getAsString();

            System.out.println(type);

            String replyMessage = "";
            switch (type.toLowerCase()) {
                case "hug" -> replyMessage = "You hug the closet person to you.";
                case "laugh" -> replyMessage = "You laugh hysterically at everyone around you.";
                case "cry" -> replyMessage = "You can't stop crying";
            }

            event.reply(replyMessage).queue();
        } else if (command.equals("giverole")) {
            Member member = event.getOption("user").getAsMember();
            Role role = event.getOption("role").getAsRole();

            event.getGuild().addRoleToMember(member, role).queue();
            event.reply(member.getAsMention() + " has been given the " + role.getAsMention() + " role!").queue();
        }
    }

    // Guild Command
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot."));
        commandData.add(Commands.slash("roles", "Display all roles on the server."));

        OptionData option1 = new OptionData(OptionType.STRING, "message",
                "The message you wants the bot say", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel",
                "The channel you wants to send this message")
                .setChannelTypes(ChannelType.TEXT, ChannelType.NEWS, ChannelType.GUILD_PUBLIC_THREAD);
        commandData.add(Commands.slash("say", "Make the bot say a message.").addOptions(option1, option2));

        OptionData option3 = new OptionData(OptionType.STRING, "type",
                "The type of emotion to express", true)
                .addChoice("Bug", "hug")
                .addChoice("Laugh", "laugh")
                .addChoice("Cry", "cry");
        commandData.add(Commands.slash("emote", "Express your emotions through text.").addOptions(option3));

        OptionData option4 = new OptionData(OptionType.USER, "user", "The user to give the role to", true);
        OptionData option5 = new OptionData(OptionType.ROLE, "role", "The role to be given", true);
        commandData.add(Commands.slash("giverole", "Give a user a role.").addOptions(option4, option5));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    // Global Command

    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot."));
        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
