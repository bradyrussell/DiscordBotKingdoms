package com.bradyrussell.game;

import com.bradyrussell.data.dbobjects.Kingdom;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ScheduledEvents {
    private static final ArrayList<Activity> activityList = new ArrayList<Activity>(Arrays.asList(Activity.watching(" you get invaded"), Activity.watching(" you get raided"), Activity.watching(" you get sacked")));

    public static void setRandomActivity(JDA jda){
        jda.getPresence().setActivity(activityList.get(ThreadLocalRandom.current().nextInt(activityList.size())));
    }

    public static void updateAIKingdoms(Session session) {

    }

    public static void scoutingCompleted(Session session, Kingdom kingdom, long scoutUnitId) {
        boolean bFoundAnything = ThreadLocalRandom.current().nextBoolean();

        if(bFoundAnything) {

        } else {
            boolean bWasInjured = ThreadLocalRandom.current().nextBoolean();

        }
    }
}
