package com.bradyrussell.game.commands;

import com.bradyrussell.data.*;
import com.bradyrussell.data.dbobjects.Building;
import com.bradyrussell.data.dbobjects.Player;
import com.bradyrussell.data.dbobjects.Unit;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import org.hibernate.Session;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandTrain extends Command {
    private final EventWaiter eventWaiter;

    public CommandTrain(EventWaiter eventWaiter) {
        this.name = "train";
        this.eventWaiter = eventWaiter;
        this.help = "Begin training a unit. Unit types can be seen with the units command.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong(), commandEvent.getChannel().getIdLong());

            if (player.kingdom != null) {
                if (!commandEvent.getArgs().isBlank()) {
                    UnitTypes unitType = UnitTypes.search(commandEvent.getArgs().trim());
                    if(unitType != null) {
                        boolean bCanTrainUnit = false;

                        List<Building> potentialBuildings = player.kingdom.getBuildingsByType(session, unitType.TrainingBuilding, unitType.TrainingBuildingLevel);
                        for (Building potentialBuilding : potentialBuildings) {
                            if(potentialBuilding.isReady()) {
                                bCanTrainUnit = true;
                                break;
                            }
                        }

                        if(bCanTrainUnit) {
                            if(player.kingdom.money >= unitType.TrainingCost) {
                                new ButtonMenu.Builder()
                                        .setUsers(commandEvent.getAuthor())
                                        .setChoices(Emojis.CONFIRM, Emojis.CANCEL)
                                        .setText("Are you sure you want to train this unit? It will cost "+ GoldUtil.getGoldSilverCopper(unitType.TrainingCost, true)+".")
                                        .setDescription(unitType.DisplayName)
                                        .setAction(reactionEmote -> {
                                            if(reactionEmote.getName().equals(Emojis.CONFIRM)) {
                                                Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();
                                                session2.refresh(player);
                                                session2.refresh(player.kingdom);
                                                player.kingdom.tick(session2);

                                                Building trainingBuilding = null;

                                                for (Building potentialBuilding : potentialBuildings) {
                                                    session2.refresh(potentialBuilding);
                                                    if(potentialBuilding.isReady()) {
                                                        trainingBuilding = potentialBuilding;
                                                        break;
                                                    }
                                                }

                                                session2.beginTransaction();

                                                if(player.kingdom.money < unitType.TrainingCost) {
                                                    commandEvent.reply("Nice try, but you cannot afford to build this! :sunglasses: ");
                                                    return;
                                                }

                                                if(trainingBuilding == null) {
                                                    commandEvent.reply("Sorry, but you do not have any buildings ready for training!");
                                                    return;
                                                }

                                               // trainingBuilding.setOccupiedFor(unitType.TrainingTime); // cant persist this one

                                                Building building = session2.get(Building.class, trainingBuilding.id);
                                                building.setOccupiedFor(unitType.TrainingTime);
                                                session2.saveOrUpdate(building);

                                                player.kingdom.money -= unitType.TrainingCost;
                                                session2.saveOrUpdate(player.kingdom);

                                                Unit unit = new Unit(player.kingdom, unitType);
                                                session2.persist(unit);

                                                session2.getTransaction().commit();
                                                session2.close();

                                                commandEvent.getMessage().reply("Training started for "+unitType.DisplayName+"!").queue();
                                            } else {
                                                commandEvent.getMessage().reply("Canceled training!").queue();
                                            }
                                        })
                                        .setFinalAction(message -> {
                                            message.clearReactions().queue();
                                        }).setEventWaiter(eventWaiter).setTimeout(30, TimeUnit.SECONDS).build().display(commandEvent.getChannel());
                            } else {
                                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                                commandEvent.reply("You cannot afford to train this unit!");
                            }
                        } else {
                            commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                            commandEvent.reply("You do not have all the prerequisites to build this! Requires: "+unitType.TrainingBuilding.DisplayName+" Level "+unitType.TrainingBuildingLevel);
                        }
                    } else {
                        commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                        commandEvent.reply("Unknown unit type!");
                    }
                } else {
                    commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                    commandEvent.reply("You must specify the type of unit: train <type> ! Use the \"units\" command to see what types are available.");
                }
            } else {
                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                commandEvent.reply("Sorry, but you must have a kingdom before you can train units!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
