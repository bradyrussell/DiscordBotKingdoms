package com.bradyrussell.game.commands;

import com.bradyrussell.data.*;
import com.bradyrussell.data.dbobjects.Army;
import com.bradyrussell.data.dbobjects.Building;
import com.bradyrussell.data.dbobjects.Player;
import com.bradyrussell.data.dbobjects.Unit;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.hibernate.Session;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class CommandKingdom extends Command {
    private final EventWaiter eventWaiter;

    public CommandKingdom(EventWaiter eventWaiter) {
        this.name = "kingdom";
        this.aliases = new String[]{"k", "view"};
        this.eventWaiter = eventWaiter;
        this.help = "Display information about your kingdom.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong());

            if (player.kingdom != null) {

                session.refresh(player.kingdom);
                player.kingdom.tick(session);

                StringBuilder sb = new StringBuilder();
                for (Building building : player.kingdom.buildings) {
                    sb.append("Level ").append(building.isConstructed() ? building.level : 0).append(" ").append(building.type.DisplayName).append(building.isConstructed() ? "" : " (Under Construction, " + ((building.created.toInstant().getEpochSecond() + building.type.BuildingTime) - Instant.now().getEpochSecond()) + "s remaining)").append(" ").append(building.isReady() ? "" : "(Busy, " + (building.ready.toInstant().getEpochSecond() - Instant.now().getEpochSecond()) + "s remaining)").append("\n");
                }
                String buildings = sb.toString();

                sb = new StringBuilder();
                for (ResourceTypes value : ResourceTypes.values()) { // todo why is this null?????
                    sb.append(value.DisplayName).append(": ").append(player.kingdom.getResource(value)).append("\n");
                }
                String resources = sb.toString();

                commandEvent.reply("Armies: "+player.kingdom.armies.size());

                sb = new StringBuilder();
                for (Army army:player.kingdom.armies) {
                    sb.append(army.name).append(" with ").append(army.units == null ? "null" : army.units.size()).append(" units").append("\n");
                }
                String armies = sb.toString();

                sb = new StringBuilder();
                for (Unit unit:player.kingdom.units) {
                    sb.append("Level ").append(unit.isTrained() ? unit.level : 0).append(" ").append(unit.type.DisplayName).append(unit.army == null ? " Unassigned" : " Assigned to " + unit.army.name).append(unit.isTrained() ? "" : " (Training, " + ((unit.created.toInstant().getEpochSecond() + unit.type.TrainingTime) - Instant.now().getEpochSecond()) + "s remaining)").append(" ").append(unit.isReady() ? "" : "(Busy, " + (unit.ready.toInstant().getEpochSecond() - Instant.now().getEpochSecond()) + "s remaining)").append("\n");
                }
                String units = sb.toString();

                int numBuildings = player.kingdom.buildings.size();
                int numUnits = player.kingdom.units.size();
                commandEvent.reply(new EmbedBuilder()
                        .setTitle(player.kingdom.name)
                        .setDescription("The "+player.kingdom.getSize()+" of "+player.kingdom.name+" has "+ GoldUtil.getGoldSilverCopper(player.kingdom.money, true)+", "+ numBuildings +" building"+(numBuildings == 1 ? "":"s")+", and "+ numUnits +" unit"+(numUnits == 1 ? "":"s")+".")
                        .addField("Buildings: ",buildings,false)
                        .addField("Resources: ",resources,false)
                        .addField("Armies: ",armies,false)
                        .addField("Unit Upkeep per Second: ",GoldUtil.getGoldSilverCopper(player.kingdom.getUnitsUpkeep(),true),false)
                        .addField("Units: ",units,false)
                        .build());

                //commandEvent.reply(sb.toString());
            } else {
                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                commandEvent.reply("Sorry, but you must have a kingdom to use this command!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
