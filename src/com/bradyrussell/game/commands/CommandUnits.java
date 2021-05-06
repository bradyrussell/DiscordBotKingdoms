package com.bradyrussell.game.commands;

import com.bradyrussell.data.BuildingTypes;
import com.bradyrussell.data.GoldUtil;
import com.bradyrussell.data.UnitTypes;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

public class CommandUnits extends Command {
    private final EventWaiter eventWaiter;

    public CommandUnits(EventWaiter eventWaiter) {
        this.name = "units";
        this.eventWaiter = eventWaiter;
        this.help = "List unit types.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if (commandEvent.getAuthor().isBot()) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Available units: \n");
        for (UnitTypes type : UnitTypes.values()) {
            sb.append(type.DisplayName).append(":\t\t").append(GoldUtil.getGoldSilverCopper(type.TrainingCost, true)).append("\t\tRequires: ").append(type.TrainingBuilding.DisplayName).append(" Level ").append(type.TrainingBuildingLevel).append("\t\t").append(type.Description).append("\n");
        }

        commandEvent.reply(sb.toString());
    }
}
