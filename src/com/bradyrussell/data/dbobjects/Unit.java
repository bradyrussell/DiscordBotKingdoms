package com.bradyrussell.data.dbobjects;

import com.bradyrussell.data.BuildingTypes;
import com.bradyrussell.data.UnitTypes;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity @Table(name="units")
public class Unit {
    public static Unit get(Session session, long id){
        return session.get(Unit.class, id);
    }

    public Unit() {
    }

    public Unit(Kingdom kingdom, UnitTypes type) {
        this.kingdom = kingdom;
        this.type = type;
        this.level = 1;
        this.health = type.MaxHealth;
        this.ready = Timestamp.from(Instant.now());
    }

    // are we occupied
    public boolean isReady() {
        return ready.before(Timestamp.from(Instant.now()));
    }
    public void setOccupiedFor(long seconds) { ready = Timestamp.from(Instant.now().plusSeconds(seconds)); }

    public boolean isTrained() {
        return created.before(Timestamp.from(Instant.now().minusSeconds(type.TrainingTime)));
    }

    @Id
    @Column(name = "id")  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "kingdom", referencedColumnName = "id")
    public Kingdom kingdom;

    @Column(name = "level")
    public int level;

    @Column(name = "health")
    public int health;

    //cannot be lazy
    @ManyToOne(/*fetch = FetchType.LAZY*/) @JoinColumn(name = "army", referencedColumnName = "id")
    public Army army;

    @Column(name = "ready")
    public Timestamp ready;

    @CreationTimestamp
    @Column(name = "created")
    public Timestamp created;

    @UpdateTimestamp
    @Column(name = "updated")
    public Timestamp updated;

    @Column(name = "type") @Enumerated(EnumType.STRING)
    public UnitTypes type;

    public boolean isInArmy() {
        return army != null;
    }

    public void tick(Session session, long elapsedSeconds) {

        //session.saveOrUpdate(this);
    }
}
