package com.bradyrussell.data.dbobjects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="armies")
public class Army {
    public Army() {
    }

    public Army(Kingdom kingdom, String name) {
        this.kingdom = kingdom;
        this.name = name;
        this.units = new ArrayList<>();
        this.ready = Timestamp.from(Instant.now());
    }

    @Id
    @Column(name = "id")  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "kingdom", referencedColumnName = "id")
    public Kingdom kingdom;

    @Column(name = "name")
    public String name;

    @Column(name = "ready")
    public Timestamp ready;

    @CreationTimestamp
    @Column(name = "created")
    public Timestamp created;

    @UpdateTimestamp
    @Column(name = "updated")
    public Timestamp updated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="army")
    public List<Unit> units;

    public void addUnit(Unit unit){
        units.add(unit);
        unit.army = this;
    }
}
