package com.bradyrussell.data.objects;

import org.hibernate.Session;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.*;

@Entity @Table(name="kingdoms")
public class Kingdom {
    public Kingdom() {
    }

    public Kingdom(long owner, String name) {
        this.owner = owner;
        this.name = name;
        this.level = 1;
    }

    public static Kingdom get(Session session, long id){
        return session.get(Kingdom.class, id);
    }

    public static Kingdom getByOwner(Session session, long owner) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Kingdom> query = builder.createQuery(Kingdom.class);
        Root<Kingdom> root = query.from(Kingdom.class);
        query.select(root).where(builder.equal(root.get("owner"), owner));

        return session.createQuery(query).getSingleResult();
    }

    @Id @Column(name = "id")  @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    @Column(name = "owner")
    public long owner;
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
}
