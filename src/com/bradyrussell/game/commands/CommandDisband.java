package com.bradyrussell.game.commands;

import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Emojis;
import com.bradyrussell.data.UnitTypes;
import com.bradyrussell.data.dbobjects.Army;
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

public class CommandDisband extends Command {
    private final EventWaiter eventWaiter;

    public CommandDisband(EventWaiter eventWaiter) {
        this.name = "disband";
        this.eventWaiter = eventWaiter;
        this.help = "Disband an army, leaving all of its units unassigned.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, commandEvent.getAuthor().getIdLong(), commandEvent.getChannel().getIdLong());

            if (player.kingdom != null) {
                if(player.kingdom.getArmyCount(session) > 0) {
                    SelectionDialog.Builder builder = new SelectionDialog.Builder()
                            .setUsers(commandEvent.getAuthor())
                            .setText("Disband which army?")
                            .setSelectedEnds(">", "<")
                            .setSelectionConsumer((m, i) -> {

                                Session session2 = DatabaseUtil.getProductionSessionFactory().openSession();

                                session2.refresh(player.kingdom);

                                session2.beginTransaction();

                                Army army = session2.get(Army.class,player.kingdom.armies.get(i - 1).id);

                                for (Unit unit : army.units) {
                                    unit.army = null;
                                    session2.saveOrUpdate(unit);
                                }

                                session2.delete(army);
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
                    commandEvent.reply("Please create an army first: createarmy <name> !");
                }
            } else {
                commandEvent.getMessage().addReaction(Emojis.CANCEL).queue();
                commandEvent.reply("Sorry, but you must have a kingdom to disband an army!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }
}
