package com.bradyrussell.data.dbobjects;

import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.*;
import java.util.List;

@Entity @Table(name="kingdoms")
public class Kingdom {
    public Kingdom() {
    }

    public Kingdom(Player owner, String name) {
        this.owner = owner;
        this.name = name;
        this.level = 1;
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

    @Id @Column(name = "id")  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(/*fetch = FetchType.LAZY*//*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "owner", referencedColumnName = "userid")
    public Player owner;

    @Column(name = "name")
    public String name;

    @Column(name = "level")
    public int level;

    @Column(name = "population")
    public int population;

    @CreationTimestamp @Column(name = "created")
    public Timestamp created;

    @UpdateTimestamp @Column(name = "updated")
    public Timestamp updated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kingdom")
    public List<Unit> units;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kingdom")
    public List<Building> buildings;
}
