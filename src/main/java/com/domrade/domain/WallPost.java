/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ComradeBaz
 */
@Entity
@Table(name = "wall_posts",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id"})
        })
public class WallPost extends BaseEntity {

    @Column(name = "author_id", nullable = false)
    private long authorId;

    @Column(name = "author_first_name", length = 50, nullable = false)
    private String authorFirstName;

    @Column(name = "author_last_name", length = 50, nullable = false)
    private String authorLastName;

    @Column(name = "post_text", length = 240, nullable = false)
    private String postText;

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private Network network;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "wallPost",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WallPostReply> wallPostReplies = new ArrayList<>();
    
    @Column(name = "time_stamp")
    private String timeStamp;

    public WallPost() {
        // No arg constructor
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public List<WallPostReply> getWallPostReplies() {
        return wallPostReplies;
    }

    public void setWallPostReplies(List<WallPostReply> wallPostReplies) {
        this.wallPostReplies = wallPostReplies;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WallPost)) {
            return false;
        }
        if (getId() != 0L) {
            return getId() == (((WallPost) o).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.authorFirstName);
        hash = 71 * hash + Objects.hashCode(this.authorLastName);
        hash = 71 * hash + Objects.hashCode(this.postText);
        hash = 71 * hash + Objects.hashCode(this.network);
        return hash;
    }

    @Override
    public String toString() {
        return "WallPost{" + ", authorId=" + authorId + ", authorFirstName=" + authorFirstName + ", authorLastName=" + authorLastName + ", postText=" + postText + '}';
    }
}
