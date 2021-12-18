package com.github.bols.vinylapi.model;

import javax.persistence.*;
import java.util.*;

@Table(name = "music_groups")
@Entity
public class MusicGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> members;

    public MusicGroup() {
    }

    public MusicGroup(Integer id, String name, Set<Artist> miembros) {
        this.id = id;
        this.name = name;

        if (miembros == null){
            miembros = new HashSet<>();
        }
        this.members = miembros;
    }

    public void addMember(Artist artist){
        List<Artist> memberList = new ArrayList<>(members);
        memberList.add(artist);
        this.members = new HashSet<>(memberList);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Artist> getMembers() {
        return members;
    }

    public void setMembers(Set<Artist> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "MusicGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", miembros=" + members +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicGroup that = (MusicGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(members, that.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, members);
    }
}