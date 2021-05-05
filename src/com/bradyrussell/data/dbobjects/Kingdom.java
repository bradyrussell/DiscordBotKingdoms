package com.bradyrussell.data.dbobjects;

import com.bradyrussell.data.BuildingTypes;
import com.bradyrussell.data.KingdomSizes;
import com.bradyrussell.data.UnitTypes;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity @Table(name="kingdoms")
public class Kingdom {
    public Kingdom() {
    }

    public Kingdom(Player owner, String name) {
        this.owner = owner;
        this.name = name;
        this.level = 1;
        this.population = 1;
        this.money = 50;
        this.lastTick = Timestamp.from(Instant.now());
    }

    public static Kingdom get(Session session, long id){
        return session.get(Kingdom.class, id);
    }

    public static Kingdom getByOwnerId(Session session, long owner) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Kingdom> query = builder.createQuery(Kingdom.class);
        Root<Kingdom> root = query.from(Kingdom.class);
        query.select(root).where(builder.equal(root.get("owner"), owner));

        return session.createQuery(query).getSingleResult();
    }

    public String getSize() {
        return KingdomSizes.getByLevel(level).DisplayName;
    }

    public static long getCount(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Kingdom> root = query.from(Kingdom.class);
        query.select(builder.count(root));

        return session.createQuery(query).getSingleResult();
    }

    public long getBuildingCountByType(Session session, BuildingTypes type) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Building> root = query.from(Building.class);
        query.select(builder.count(root)).where(builder.equal(root.get("type"), type)).where(builder.equal(root.get("kingdom"), id));

        return session.createQuery(query).getSingleResult();
    }

    public long getUnitCountByType(Session session, UnitTypes type) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Unit> root = query.from(Unit.class);
        query.select(builder.count(root)).where(builder.equal(root.get("type"), type)).where(builder.equal(root.get("kingdom"), id));

        return session.createQuery(query).getSingleResult();
    }

    public List<Building> getBuildingsByType(Session session, BuildingTypes type) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Building> query = builder.createQuery(Building.class);
        Root<Building> root = query.from(Building.class);
        query.select(root).where(builder.equal(root.get("type"), type)).where(builder.equal(root.get("kingdom"), id));

        return session.createQuery(query).getResultList();
    }

    public List<Unit> getUnitsByType(Session session, UnitTypes type) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Unit> query = builder.createQuery(Unit.class);
        Root<Unit> root = query.from(Unit.class);
        query.select(root).where(builder.equal(root.get("type"), type)).where(builder.equal(root.get("kingdom"), id));

        return session.createQuery(query).getResultList();
    }

    public boolean hasPrerequisites(BuildingTypes type){
        System.out.println("Building prereqs "+type.Prerequisites.entrySet().stream());
        for (Map.Entry<BuildingTypes, Integer> entry : type.Prerequisites.entrySet()) {
            if(!hasBuildingAtLevel(entry.getKey(),entry.getValue())) return false;
            System.out.println("Has "+entry.toString());
        }
        return true;
    }

    public boolean hasBuildingAtLevel(BuildingTypes type, int level){
        for (Building building : buildings) {
            if(building.isReady() && building.type.equals(type) && building.level >= level) return true;
        }
        return false;
    }

    @Id @Column(name = "id")  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(/*fetch = FetchType.LAZY*//*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "owner", referencedColumnName = "userid")
    public Player owner;

    @Column(name = "name")
    public String name;

    @Column(name = "level")
    public int level;

    @Column(name = "money")
    public long money;

    @Column(name = "population")
    public int population;

    @CreationTimestamp @Column(name = "created")
    public Timestamp created;

    @UpdateTimestamp @Column(name = "updated")
    public Timestamp updated;

    @Column(name = "last_tick")
    public Timestamp lastTick;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kingdom")
    public List<Unit> units;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kingdom")
    public List<Building> buildings;

    // everything is responsible for saving itself if it changes
    public void tick(Session session) {
        long lastTickSecond = lastTick.toInstant().getEpochSecond();
        long currentTickSecond = Instant.now().getEpochSecond();
        long secondsElapsed = currentTickSecond - lastTickSecond;
        System.out.println("Last Tick: "+lastTickSecond+", Current Tick: "+currentTickSecond+", Delta: "+secondsElapsed);

        if(secondsElapsed == 0) return;
        if(secondsElapsed < 0) throw new RuntimeException("Last Tick is before Current Tick! "+lastTickSecond+" is before "+currentTickSecond);

        session.beginTransaction();

        for (Building building : buildings) {
            building.tick(session, secondsElapsed);
        }

        for (Unit unit : units) {
            unit.tick(session, secondsElapsed);
        }

        System.out.println(">>>>>>>> Ticked with "+secondsElapsed+" elapsed.");

        lastTick = Timestamp.from(Instant.now());
        session.saveOrUpdate(this);
        session.getTransaction().commit();
    }
}
