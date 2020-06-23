/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ComradeBaz
 */
@Entity
@Table(name = "event",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
        })
public class Event extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "title")
    private String title;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "location")
    private String location;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "network_id")
    @NotNull
    private Network network;

    @ManyToOne
    @JoinColumn(name = "users_id")
    @NotNull
    private User owner;

    @Basic(optional = false)
    @NotNull
    @Column(name = "the_date")
    private Date theDate;

    @Basic(optional = false)
    @NotNull
    @Column(name = "the_time")
    private Date theTime;

    @ManyToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_users",
            joinColumns = {
                @JoinColumn(name = "event_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "users_id")}
    )
    Set<User> theUsers = new HashSet<>();

    public Event() {
        // no arg constructor
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTheDate() {
        return theDate;
    }

    public void setTheDate(Date theDate) {
        this.theDate = theDate;
    }

    public Date getTheTime() {
        return theTime;
    }

    public void setTheTime(Date theTime) {
        this.theTime = theTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network networkId) {
        this.network = networkId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<User> getTheUsers() {
        return theUsers;
    }

    public void setTheUsers(Set<User> theUsers) {
        this.theUsers = theUsers;
    }

    @Override
    public String toString() {
        return "Event{" + "title=" + title + ", location=" + location + ", date=" + theDate + ", time=" + theTime + ", description=" + description + '}';
    }

}
