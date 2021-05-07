package com.bradyrussell.game.commands;

import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Emojis;
import com.bradyrussell.data.dbobjects.Army;
import com.bradyrussell.data.dbobjects.Kingdom;
import com.bradyrussell.data.dbobjects.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import org.hibernate.Session;

import java.util.concurrent.TimeUnit;

public class CommandCreateArmy extends Command {
    private final EventWaiter eventWaiter;

    public CommandCreateArmy(EventWaiter eventWaiter) {
        this.name = "createarmy";
        this.eventWaiter = eventWaiter;
        this.help = "Create a new army.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong(), commandEvent.getChannel().getIdLong());

            if (player.kingdom != null) {
                long kingdomArmyCount = player.kingdom.getArmyCount(session);
                if(kingdomArmyCount < player.kingdom.getUnitCount(session)) {
                    if (!commandEvent.getArgs().isBlank()) {
                        new ButtonMenu.Builder()
                                .setUsers(commandEvent.getAuthor())
                                .setChoices(Emojis.CONFIRM,Emojis.CANCEL)
                                .setText("Are you sure you want to name your army this?")
                                .setDescription(commandEvent.getArgs())
                                .setAction(reactionEmote -> {
                                    if(reactionEmote.getName().equals(Emojis.CONFIRM)) {
                                        Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();
                                        session2.beginTransaction();

                                        Army army = new Army(player.kingdom, commandEvent.getArgs().trim());

                                        session2.persist(army);
                                        session2.getTransaction().commit();
                                        session2.close();

                                        commandEvent.getMessage().reply("Your army has been created!").queue();
                                    } else {
                                        commandEvent.getMessage().reply("Canceled army creation!").queue();
                                    }
                                })
                                .setFinalAction(message -> {
                                    message.clearReactions().queue();
                                }).setEventWaiter(eventWaiter).setTimeout(15, TimeUnit.SECONDS).build().display(commandEvent.getChannel());
                    } else {
                        commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                        commandEvent.reply("Please specify a name for your army: createarmy <name> !");
                    }
                } else {
                    commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                    commandEvent.reply("Sorry, but you must have more units to create an"+(kingdomArmyCount == 0 ? "" : "other")+" army!");
                }
            } else {
                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                commandEvent.reply("Sorry, but you must have a kingdom to create an army!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
