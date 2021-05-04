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

public class CommandCreateKingdom extends Command {
    private final String CANCEL = "\u274C"; // ❌
    private final String CONFIRM = "\u2611"; // ☑

    private final EventWaiter eventWaiter;

    public CommandCreateKingdom(EventWaiter eventWaiter) {
        this.name = "create";
        this.eventWaiter = eventWaiter;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong());

            if (player.kingdom == null) {
                if (!commandEvent.getArgs().isBlank()) {

                    new ButtonMenu.Builder()
                            .setUsers(commandEvent.getAuthor())
                            .setChoices("✅", "❌")
                            .setText("Are you sure you want to name your kingdom this?")
                            .setDescription(commandEvent.getArgs())
                            .setAction(reactionEmote -> {
                                if(reactionEmote.getName().equals("✅")) {
                                    Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();
                                    session2.beginTransaction();

                                    Kingdom kingdom = new Kingdom(player, commandEvent.getArgs().trim());

                                    session2.persist(kingdom);
                                    session2.getTransaction().commit();
                                    session2.close();

                                    commandEvent.getMessage().reply("Your kingdom has been created!").queue();
                                } else {
                                    commandEvent.getMessage().reply("Canceled kingdom creation!").queue();
                                }
                            })
                            .setFinalAction(message -> {
                                message.clearReactions().queue();
                            }).setEventWaiter(eventWaiter).setTimeout(15, TimeUnit.SECONDS).build().display(commandEvent.getChannel());


                } else {
                    commandEvent.getMessage().addReaction("❌").queue();
                    commandEvent.reply("Please specify a name for your kingdom: create <name> !");
                }
            } else {
                commandEvent.getMessage().addReaction("❌").queue();
                commandEvent.reply("Sorry, but you must surrender " + player.kingdom.name + " before you can create a new kingdom!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
