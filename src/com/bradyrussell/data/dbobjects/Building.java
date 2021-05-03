package com.bradyrussell.data.dbobjects;

import com.bradyrussell.data.BuildingTypes;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity @Table(name="buildings")
public class Building {
    public static Building get(Session session, long id){
        return session.get(Building.class, id);
    }

    public Building() {
    }

    public Building(Kingdom kingdom, BuildingTypes type) {
        this.kingdom = kingdom;
        this.type = type;
        this.level = 1;
        this.health = type.MaxHealth;
    }

    @Id @Column(name = "id")  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "kingdom", referencedColumnName = "id")
    public Kingdom kingdom;

    @Column(name = "level")
    public int level;

    @Column(name = "health")
    public int health;

    @CreationTimestamp @Column(name = "created")
    public Timestamp created;

    @UpdateTimestamp @Column(name = "updated")
    public Timestamp updated;

    @Column(name = "type") @Enumerated(EnumType.STRING)
    public BuildingTypes type;
}
