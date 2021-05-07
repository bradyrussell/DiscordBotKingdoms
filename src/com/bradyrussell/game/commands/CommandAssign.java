package com.bradyrussell.game.commands;

import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Emojis;
import com.bradyrussell.data.UnitTypes;
import com.bradyrussell.data.dbobjects.Army;
import com.bradyrussell.data.dbobjects.Kingdom;
import com.bradyrussell.data.dbobjects.Player;
import com.bradyrussell.data.dbobjects.Unit;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import com.jagrosh.jdautilities.menu.SelectionDialog;
import org.hibernate.Session;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandAssign extends Command {
    private final EventWaiter eventWaiter;

    public CommandAssign(EventWaiter eventWaiter) {
        this.name = "assign";
        this.eventWaiter = eventWaiter;
        this.help = "Assign units to an army.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong(), commandEvent.getChannel().getIdLong());

            if (player.kingdom != null) {
                if(player.kingdom.getArmyCount(session) > 0) {
                    if (!commandEvent.getArgs().isBlank()) {

                        UnitTypes specifiedType;
                        Integer specifiedLevel = null;

                        String[] split = commandEvent.getArgs().split(" ");

                        if(split[0].equalsIgnoreCase("level")) {
                            try {
                                specifiedLevel = Integer.parseInt(split[1]);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            specifiedType = UnitTypes.search(commandEvent.getArgs().replace(split[0]+" "+split[1]+" ",""));
                        } else {
                            specifiedType = UnitTypes.search(commandEvent.getArgs());
                        }

                        if(specifiedType != null) {
                            List<Unit> potentialUnits = player.kingdom.getUnitsByType(session, specifiedType, specifiedLevel == null ? 1 : specifiedLevel);

                            Unit selectedUnit = null;

                            for (Unit potentialUnit : potentialUnits) {
                                if(potentialUnit.isReady() && potentialUnit.isTrained() && !potentialUnit.isInArmy()) {
                                    selectedUnit = potentialUnit;
                                    break;
                                }
                            }

                            if(selectedUnit != null) {
                                Unit finalSelectedUnit = selectedUnit;
                                SelectionDialog.Builder builder = new SelectionDialog.Builder()
                                        .setUsers(commandEvent.getAuthor())
                                        .setText("Assign " + (specifiedLevel == null ? "" : "level " + specifiedLevel) + " " + specifiedType.DisplayName + " to what army?")
                                        .setSelectedEnds(">", "<")
                                        .setSelectionConsumer((m, i) -> {

                                            Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();

                                            session2.refresh(player.kingdom);

                                            session2.beginTransaction();

                                           // Army army = session2.get(Army.class,player.kingdom.armies.get(i - 1).id);

                                            Army army = player.kingdom.armies.get(i - 1);
                                            army.addUnit(session2.get(Unit.class, finalSelectedUnit.id));

                                            session2.saveOrUpdate(army);
                                            //session2.saveOrUpdate(unit);
                                            //session2.saveOrUpdate(player.kingdom);
                                            session2.getTransaction().commit();
                                            session2.close();

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
                                commandEvent.reply("You do not have any unassigned "+(specifiedLevel == null ? "" :"level "+specifiedLevel)+" "+specifiedType.DisplayName+" ready!");
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
                commandEvent.reply("Sorry, but you must have a kingdom to create an army!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
