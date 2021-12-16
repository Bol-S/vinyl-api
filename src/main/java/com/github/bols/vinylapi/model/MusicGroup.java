package com.github.bols.vinylapi.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

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
    private Set<Artist> miembros;

    public MusicGroup() {
    }

    public MusicGroup(Integer id, String name, Set<Artist> miembros) {
        this.id = id;
        this.name = name;
        this.miembros = miembros;
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

    public Set<Artist> getMiembros() {
        return miembros;
    }

    public void setMiembros(Set<Artist> miembros) {
        this.miembros = miembros;
    }

    @Override
    public String toString() {
        return "MusicGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", miembros=" + miembros +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicGroup that = (MusicGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(miembros, that.miembros);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, miembros);
    }
}