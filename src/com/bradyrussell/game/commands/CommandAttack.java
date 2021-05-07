package com.bradyrussell.game.commands;

import com.bradyrussell.data.BuildingTypes;
import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Emojis;
import com.bradyrussell.data.GoldUtil;
import com.bradyrussell.data.dbobjects.Army;
import com.bradyrussell.data.dbobjects.Building;
import com.bradyrussell.data.dbobjects.Player;
import com.bradyrussell.data.dbobjects.Unit;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jdautilities.menu.SelectionDialog;
import org.hibernate.Session;

import java.util.concurrent.TimeUnit;

public class CommandAttack extends Command {
    private final EventWaiter eventWaiter;

    public CommandAttack(EventWaiter eventWaiter) {
        this.name = "attack";
        this.eventWaiter = eventWaiter;
        this.help = "Send an army to attack an enemy kingdom.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong(), commandEvent.getChannel().getIdLong());

            if (player.kingdom != null) {
                if (!commandEvent.getArgs().isBlank() && commandEvent.getMessage().getMentionedUsers().size() > 0) {
                    Player playerToAttack = null;

                    if(commandEvent.getMessage().getMentionedUsers().get(0).getIdLong() == commandEvent.getJDA().getSelfUser().getIdLong()) {
                        // attack bot
                        String[] s = commandEvent.getArgs().split(" ");
                        Long aiID = null;
                        try {
                            aiID = Long.parseLong(s[s.length-1]);
                        } catch (NumberFormatException ignored){

                        }
                        if(aiID != null) {
                            playerToAttack = Player.get(session, aiID, null);
                        }
                    } else {
                        playerToAttack = Player.get(session, commandEvent.getMessage().getMentionedUsers().get(0).getIdLong(), null);
                    }

                    if(playerToAttack != null && playerToAttack.kingdom != null) {
                        if(true) {

                            Player finalPlayerToAttack = playerToAttack;
                            SelectionDialog.Builder builder = new SelectionDialog.Builder()
                                    .setUsers(commandEvent.getAuthor())
                                    .setText("Attack with which army?")
                                    .setSelectedEnds(">", "<")
                                    .setSelectionConsumer((m, i) -> {
                                        Army army = player.kingdom.armies.get(i - 1);
                                        if(true) {
                                            new ButtonMenu.Builder()
                                                    .setUsers(commandEvent.getAuthor())
                                                    .setChoices(Emojis.CONFIRM, Emojis.CANCEL)
                                                    .setText("Are you sure you want to attack? Your army will remain occupied for 10 minutes.")
                                                    .setDescription("Attack "+ finalPlayerToAttack.kingdom.name+" with "+army.name)
                                                    .setAction(reactionEmote -> {
                                                        if(reactionEmote.getName().equals(Emojis.CONFIRM)) {
                                                            Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();
                                                            session2.refresh(player);

                                                            player.kingdom.tick(session2);

                                                            // notify other player
/*                                                session2.beginTransaction();
                                                session2.getTransaction().commit();*/
                                                            session2.close();

                                                            commandEvent.getMessage().reply("You have sent your army towards "+ finalPlayerToAttack.kingdom.name+"! A messenger should return shortly!").queue();
                                                        } else {
                                                            commandEvent.getMessage().reply("Canceled attack!").queue();
                                                        }
                                                    })
                                                    .setFinalAction(message -> {
                                                        message.clearReactions().queue();
                                                    }).setEventWaiter(eventWaiter).setTimeout(30, TimeUnit.SECONDS).build().display(commandEvent.getChannel());
                                        } else {
                                            commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                                            commandEvent.reply("You cannot afford to build this!");
                                        }

                                        m.clearReactions().queue();
                                    })
                                    .setCanceled((m) -> {
                                        m.clearReactions().queue();
                                    })
                                    .setEventWaiter(eventWaiter).useSingleSelectionMode(true).setTimeout(15, TimeUnit.SECONDS);

                            for (Army army : player.kingdom.armies) {
                                builder.addChoices(army.name);
                            }

                            builder.build().display(commandEvent.getChannel());
                        } else {
                            commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                            commandEvent.reply("You do not have any armies available!");
                        }
                    } else {
                        commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                        commandEvent.reply("They are not the leader of a kingdom!");
                    }
                } else {
                    commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                    commandEvent.reply("You must specify who you want to attack with an @mention!");
                }
            } else {
                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                commandEvent.reply("Sorry, but you must have a kingdom before you can build!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
