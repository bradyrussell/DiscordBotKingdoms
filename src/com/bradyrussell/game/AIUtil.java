package com.bradyrussell.game;

import com.bradyrussell.data.dbobjects.Kingdom;
import com.bradyrussell.data.dbobjects.Player;
import org.hibernate.Session;

import java.util.concurrent.ThreadLocalRandom;

public class AIUtil {
    public static long createAIKingdom(Session session, long level) {
        Player ai = new Player((Player.getAIs(session).size() + 1) * -1);
        Kingdom aiKingdom = new Kingdom(ai, "Kingdom" + ai.userid);

        aiKingdom.level = (int) level;
        aiKingdom.money = (long) (level * 5000 * ThreadLocalRandom.current().nextFloat());
        aiKingdom.population = (int) (level * 10 * ThreadLocalRandom.current().nextFloat());

        session.beginTransaction();
        session.persist(ai);
        session.persist(aiKingdom);
        session.getTransaction().commit();
        return ai.userid;
    }
}
