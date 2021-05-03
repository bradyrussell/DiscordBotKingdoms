package com.bradyrussell.data.dbobjects;

import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity @Table(name="units")
public class Unit {
    public static Unit get(Session session, long id){
        return session.get(Unit.class, id);
    }

    @Id
    @Column(name = "id")  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "kingdom", referencedColumnName = "id")
    Kingdom kingdom;

    @Column(name = "level")
    public int level;

    @Column(name = "health")
    public int health;

    @CreationTimestamp
    @Column(name = "created")
    public Timestamp created;

    @UpdateTimestamp
    @Column(name = "updated")
    public Timestamp updated;
}
