package com.bradyrussell.data.dbobjects;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.*;
import java.util.List;
import java.util.Map;

@Entity @Table(name="players")
public class Player {
    public Player() {
    }

    public Player(long userid) {
        this.userid = userid;
    }

    //creates if one does not exist
    public static Player get(Session session, long userid, Long forChannel){
        Player player = session.get(Player.class, userid);
        if(player == null) {
            session.beginTransaction();

            player = new Player(userid);

            player.channel = forChannel;
            session.persist(player);
            session.getTransaction().commit();
        } else {
            if(forChannel != null && player.channel != forChannel) {
                player.channel = forChannel;
                session.beginTransaction();
                session.saveOrUpdate(player);
                session.getTransaction().commit();
            }
        }
        return player;
    }

    @Id @Column(name="userid")
    public long userid;

    // if null, dm player
    @Column(name="channel")
    public Long channel;

    @CreationTimestamp @Column(name="joined")
    public Timestamp joined;

    @UpdateTimestamp @Column(name = "updated")
    public Timestamp updated;

    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    public Kingdom kingdom;

    public static final Map<String, Integer> AINames = Map.of(
            "William", -1,
            "Billiam", -2,
            "Chilliam", -3,
            "Killiam", -4,
            "Zilliam", -5
    );

    public boolean isAI() {
        return userid < 0;
    }

    public static List<Player> getAIs(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Player> query = builder.createQuery(Player.class);
        Root<Player> root = query.from(Player.class);
        query.select(root).where(builder.lessThan(root.get("userid"), 0));

        return session.createQuery(query).getResultList();
    }

}
