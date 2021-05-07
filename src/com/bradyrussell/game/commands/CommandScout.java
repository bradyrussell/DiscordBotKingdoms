package com.bradyrussell.game.commands;

import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Emojis;
import com.bradyrussell.data.GoldUtil;
import com.bradyrussell.data.UnitTypes;
import com.bradyrussell.data.dbobjects.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jdautilities.menu.SelectionDialog;
import org.hibernate.Session;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandScout extends Command {
    private final EventWaiter eventWaiter;

    public CommandScout(EventWaiter eventWaiter) {
        this.name = "scout";
        this.eventWaiter = eventWaiter;
        this.help = "Send a scout on a scouting mission.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong(), commandEvent.getChannel().getIdLong());

            if (player.kingdom != null) {
                if(true) {
                    if (true) {
                        Integer specifiedLevel = null;
                        if (!commandEvent.getArgs().isBlank()) {
                            String[] split = commandEvent.getArgs().split(" ");

                            if (split[0].equalsIgnoreCase("level")) {
                                try {
                                    specifiedLevel = Integer.parseInt(split[1]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if(true) {
                            List<Unit> potentialUnits = player.kingdom.getUnitsByType(session, UnitTypes.Scout, specifiedLevel == null ? 1 : specifiedLevel);

                            Unit selectedUnit = null;

                            for (Unit potentialUnit : potentialUnits) {
                                if(potentialUnit.isReady() && potentialUnit.isTrained() && !potentialUnit.isInArmy()) {
                                    selectedUnit = potentialUnit;
                                    break;
                                }
                            }

                            if(selectedUnit != null) {
                                Unit finalSelectedUnit = selectedUnit;

                                new ButtonMenu.Builder()
                                        .setUsers(commandEvent.getAuthor())
                                        .setChoices(Emojis.CONFIRM,Emojis.CANCEL)
                                        .setText("Are you sure you want to send this scout?")
                                        .setDescription("Level "+finalSelectedUnit.level+" "+finalSelectedUnit.type.DisplayName)
                                        .setAction(reactionEmote -> {
                                            if(reactionEmote.getName().equals(Emojis.CONFIRM)) {
                                                Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();
                                                session2.beginTransaction();

                                                finalSelectedUnit.setOccupiedFor(300);
                                                session2.saveOrUpdate(finalSelectedUnit);
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
                                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                                commandEvent.reply("You do not have any "+(specifiedLevel == null ? "" :"level "+specifiedLevel+" ")+"scouts ready!");
                            }

                        } else {
                            commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                            commandEvent.reply("Unknown unit type!");
                        }


                    } else {
                        commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                        commandEvent.reply("Please specify a unit to assign: assign [level N] <type> !");
                    }
                } else {
                    commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                    commandEvent.reply("Please create an army first: createarmy <name> !");
                }
            } else {
                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                commandEvent.reply("Sorry, but you must have a kingdom to scout!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
