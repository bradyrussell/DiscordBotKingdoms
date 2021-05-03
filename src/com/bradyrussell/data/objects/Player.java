package com.bradyrussell.data.objects;

import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.*;

@Entity @Table(name="players")
public class Player {
    public Player() {
    }

    public Player(long userid) {
        this.userid = userid;
    }

    public static Player get(Session session, long userid){
        return session.get(Player.class, userid);
    }

    @Id @Column(name="userid")
    public long userid;

    @CreationTimestamp @Column(name="joined")
    public Timestamp joined;

    @UpdateTimestamp @Column(name = "updated")
    public Timestamp updated;
}
