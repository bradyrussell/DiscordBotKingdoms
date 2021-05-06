package com.bradyrussell.data.dbobjects;

import com.bradyrussell.data.BuildingTypes;
import com.bradyrussell.data.ResourceTypes;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

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
        this.ready = Timestamp.from(Instant.now());
    }

    public boolean isReady() { return ready.before(Timestamp.from(Instant.now())); }
    public void setOccupiedFor(long seconds) { ready = Timestamp.from(Instant.now().plusSeconds(seconds)); }

    public boolean isConstructed() {
        return created.before(Timestamp.from(Instant.now().minusSeconds(type.BuildingTime)));
    }

    @Id @Column(name = "id")  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "kingdom", referencedColumnName = "id")
    public Kingdom kingdom;

    @Column(name = "level")
    public int level;

    @Column(name = "health")
    public int health;

    @Column(name = "ready")
    public Timestamp ready;

    @CreationTimestamp @Column(name = "created")
    public Timestamp created;

    @UpdateTimestamp @Column(name = "updated")
    public Timestamp updated;

    @Column(name = "type") @Enumerated(EnumType.STRING)
    public BuildingTypes type;

    public void tick(Session session, long elapsedSeconds) {

        switch (type){

            case ThroneRoom -> {
                if(isReady())
                kingdom.money+= elapsedSeconds;
            }

            case CopperMine -> {
                if(isReady()) {
                    kingdom.addResource(ResourceTypes.CopperMetal, elapsedSeconds);
                }
            }

        }

        //session.saveOrUpdate(this);
    }
}
