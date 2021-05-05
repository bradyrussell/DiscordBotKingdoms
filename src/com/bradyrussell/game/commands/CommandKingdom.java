package com.bradyrussell.game.commands;

import com.bradyrussell.data.AssetEmojis;
import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Emojis;
import com.bradyrussell.data.GoldUtil;
import com.bradyrussell.data.dbobjects.Player;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu;
import org.hibernate.Session;

import java.util.concurrent.TimeUnit;

public class CommandKingdom extends Command {
    private final EventWaiter eventWaiter;

    public CommandKingdom(EventWaiter eventWaiter) {
        this.name = "kingdom";
        this.aliases = new String[]{"k", "view"};
        this.eventWaiter = eventWaiter;
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
                sb.append("Assets available: ");
                for (AssetEmojis value : AssetEmojis.values()) {
                    sb.append(value.get());
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
