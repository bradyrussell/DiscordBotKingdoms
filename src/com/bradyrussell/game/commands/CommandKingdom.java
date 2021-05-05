package com.bradyrussell.game.commands;

import com.bradyrussell.data.*;
import com.bradyrussell.data.dbobjects.Building;
import com.bradyrussell.data.dbobjects.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
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

                player.kingdom.tick(session);

                int numBuildings = player.kingdom.buildings.size();
                int numUnits = player.kingdom.units.size();
                commandEvent.reply("The "+player.kingdom.getSize()+" of "+player.kingdom.name+" has "+ GoldUtil.getGoldSilverCopper(player.kingdom.money, true)+", "+ numBuildings +" building"+(numBuildings == 1 ? "":"s")+", and "+ numUnits +" unit"+(numUnits == 1 ? "":"s")+".");

                StringBuilder sb = new StringBuilder();
                sb.append("\nBuildings: \n");
                for (Building building : player.kingdom.buildings) {
                    sb.append("Level ").append(building.isReady() ? building.level : 0).append(" ").append(building.type.DisplayName).append(building.isReady() ? "":" (Under Construction, "+(building.ready.toInstant().getEpochSecond() - Instant.now().getEpochSecond()) +"s remaining)").append("\n");
                }

                sb.append("\nResources: \n");
                for (ResourceTypes value : ResourceTypes.values()) {
                    sb.append(value.DisplayName).append(": ").append(player.kingdom.getResource(value)).append("\n");
                }

                if(commandEvent.getArgs().contains("debug")){
                    sb.append("Assets available: ");
                    for (AssetEmojis value : AssetEmojis.values()) {
                        sb.append(value.get());
                    }
                }

                commandEvent.reply(sb.toString());
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
