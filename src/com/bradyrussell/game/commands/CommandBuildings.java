package com.bradyrussell.game.commands;

import com.bradyrussell.data.BuildingTypes;
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

public class CommandBuildings extends Command {
    private final EventWaiter eventWaiter;

    public CommandBuildings(EventWaiter eventWaiter) {
        this.name = "buildings";
        this.eventWaiter = eventWaiter;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Available buildings: \n");
        for (BuildingTypes type : BuildingTypes.values()) {
            sb.append(type.DisplayName).append(":\t\t").append(GoldUtil.getGoldSilverCopper(type.BuildCost, true)).append("\t\tRequires: ").append(type.prerequisitesString()).append("\t\t").append(type.Description).append("\n");
        }

        commandEvent.reply(sb.toString());
    }
}
