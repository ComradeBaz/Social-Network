/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.domain;

/**
 *
 * @author ComradeBaz
 */
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author ComradeBaz
 */
@Entity
@Table(name = "networks",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
            , @UniqueConstraint(columnNames = {"name"})
        })

@NamedQueries({
    @NamedQuery(name = "Network.findAll", query = "SELECT n FROM Network n")
    , @NamedQuery(name = "Network.findAllNetworks", query = "SELECT n.name FROM Network n")
    , @NamedQuery(name = "Network.findOneByName", query = "SELECT n FROM Network n WHERE n.name = :name")
    , @NamedQuery(name = "Network.findOneById", query = "SELECT n FROM Network n WHERE n.id = :networkId")
    , @NamedQuery(name = "Network.findByIpAddress", query = "SELECT n FROM Network n WHERE n.ipAddress = :ipAddress")
    , @NamedQuery(name = "Network.findByCity", query = "SELECT n FROM Network n WHERE n.city = :city")
    , @NamedQuery(name = "Network.findByCountry", query = "SELECT n FROM Network n WHERE n.country = :country")})
public class Network extends BaseEntity {
    
    static final Logger LOGGER = Logger.getLogger(Network.class.getName());

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ip_address")
    private String ipAddress;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "city")
    private String city;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "country")
    private String country;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "postal")
    private String postal;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;

    @OneToMany(
            mappedBy = "network",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WallPost> wallPosts = new ArrayList<>();

    @OneToMany(
            mappedBy = "network",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Event> events = new ArrayList<>();

    public Network() {
    }

    public Network(String name, String ipAddress, String city, String country) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public List<WallPost> getWallPosts() {
        return wallPosts;
    }

    public void setWallPosts(List<WallPost> wallPosts) {
        this.wallPosts = wallPosts;
    }

    public void addWallPost(WallPost wallPost) {
        LOGGER.log(Level.INFO, "Adding wallpost to network");
        wallPost.setNetwork(this);
        wallPosts.add(wallPost);
    }

    public void removeWallPost(WallPost wallPost) {
        LOGGER.log(Level.INFO, "Removing wallpost from network");
        wallPost.setNetwork(null);
        wallPosts.remove(wallPost);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
    public void addEvent(Event event) {
        LOGGER.log(Level.INFO, "Adding event to network");
        events.add(event);
        event.setNetwork(this);
    }
    
    public void removeEvent(Event event) {
        LOGGER.log(Level.INFO, "Removing wallpost from network");
        events.remove(event);
        event.setNetwork(null);
    }

    @Override
    public String toString() {
        return "com.domrade.domain.Networks[ipAddress= " + ipAddress + " name=" + name + " ]";
    }
}
