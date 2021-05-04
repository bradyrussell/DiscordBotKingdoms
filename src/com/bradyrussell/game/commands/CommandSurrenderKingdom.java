package com.bradyrussell.game.commands;

import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.dbobjects.Kingdom;
import com.bradyrussell.data.dbobjects.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import org.hibernate.Session;

import java.util.concurrent.TimeUnit;

public class CommandSurrenderKingdom extends Command {
    private final String CANCEL = "\u274C"; // ❌
    private final String CONFIRM = "\u2611"; // ☑

    private final EventWaiter eventWaiter;

    public CommandSurrenderKingdom(EventWaiter eventWaiter) {
        this.name = "surrender";
        this.eventWaiter = eventWaiter;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong());

            if (player.kingdom != null) {
                if (!commandEvent.getArgs().isBlank()) {

                    if(commandEvent.getArgs().trim().equals(player.kingdom.name)) {
                        new ButtonMenu.Builder()
                                .setUsers(commandEvent.getAuthor())
                                .setChoices("✅", "❌")
                                .setText("Are you sure you want to surrender your kingdom? This CANNOT be undone.")
                                .setDescription(commandEvent.getArgs())
                                .setAction(reactionEmote -> {
                                    if(reactionEmote.getName().equals("✅")) {
                                        Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();
                                        session2.beginTransaction();

                                        session2.delete(player.kingdom);

                                        session2.getTransaction().commit();
                                        session2.close();

                                        commandEvent.getMessage().reply("You have surrendered your kingdom!").queue();
                                    } else {
                                        commandEvent.getMessage().reply("Canceled surrender!").queue();
                                    }
                                })
                                .setFinalAction(message -> {
                                    message.clearReactions().queue();
                                }).setEventWaiter(eventWaiter).setTimeout(30, TimeUnit.SECONDS).build().display(commandEvent.getChannel());
                    } else {
                        commandEvent.getMessage().addReaction("❌").queue();
                        commandEvent.reply("You entered "+commandEvent.getArgs().trim()+" but were supposed to enter "+player.kingdom.name+" !");
                    }
                } else {
                    commandEvent.getMessage().addReaction("❌").queue();
                    commandEvent.reply("For verification please specify the name of your kingdom: surrender "+player.kingdom.name+" !");
                }
            } else {
                commandEvent.getMessage().addReaction("❌").queue();
                commandEvent.reply("Sorry, but you must have a kingdom before you can surrender it!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
