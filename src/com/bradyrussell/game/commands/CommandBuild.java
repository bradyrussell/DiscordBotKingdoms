package com.bradyrussell.game.commands;

import com.bradyrussell.data.BuildingTypes;
import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Emojis;
import com.bradyrussell.data.dbobjects.Building;
import com.bradyrussell.data.dbobjects.Kingdom;
import com.bradyrussell.data.dbobjects.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import org.hibernate.Session;

import java.util.concurrent.TimeUnit;

public class CommandBuild extends Command {
    private final EventWaiter eventWaiter;

    public CommandBuild(EventWaiter eventWaiter) {
        this.name = "build";
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
                    BuildingTypes buildingType = BuildingTypes.search(commandEvent.getArgs().trim());
                    if(buildingType != null) {
                        if(player.kingdom.hasPrerequisites(buildingType)) {
                            if(player.kingdom.money >= buildingType.BuildCost) {
                                new ButtonMenu.Builder()
                                        .setUsers(commandEvent.getAuthor())
                                        .setChoices(Emojis.CONFIRM, Emojis.CANCEL)
                                        .setText("Are you sure you want to build this?")
                                        .setDescription(buildingType.DisplayName)
                                        .setAction(reactionEmote -> {
                                            if(reactionEmote.getName().equals(Emojis.CONFIRM)) {
                                                Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();
                                                session2.refresh(player);
                                                session2.beginTransaction();

                                                if(player.kingdom.money < buildingType.BuildCost) {
                                                    commandEvent.reply("Nice try, but you cannot afford to build this! :sunglasses: ");
                                                    return;
                                                }

                                                player.kingdom.money -= buildingType.BuildCost;
                                                session2.saveOrUpdate(player.kingdom);

                                                Building building = new Building(player.kingdom, buildingType);
                                                session2.persist(building);

                                                session2.getTransaction().commit();
                                                session2.close();

                                                commandEvent.getMessage().reply("You have built "+buildingType.DisplayName+"!").queue();
                                            } else {
                                                commandEvent.getMessage().reply("Canceled build!").queue();
                                            }
                                        })
                                        .setFinalAction(message -> {
                                            message.clearReactions().queue();
                                        }).setEventWaiter(eventWaiter).setTimeout(30, TimeUnit.SECONDS).build().display(commandEvent.getChannel());
                            } else {
                                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                                commandEvent.reply("You cannot afford to build this!");
                            }
                        } else {
                            commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                            commandEvent.reply("You do not have all the prerequisites to build this! Requires: "+buildingType.prerequisitesString());
                        }
                    } else {
                        commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                        commandEvent.reply("Unknown building type!");
                    }
                } else {
                    commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                    commandEvent.reply("You must specify the type of building: build <type> ! Use the \"buildings\" command to see what types are available.");
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
