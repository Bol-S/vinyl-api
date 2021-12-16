package com.github.bols.vinylapi.model;

import com.github.bols.vinylapi.model.enums.Condition;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "albums", indexes = {
        @Index(name = "fk_albums_artists_idx", columnList = "artist_id")
})
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "condition", length = 100)
    private Condition condition;

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    public Album() {
    }

    public Album(Integer id, String name, Integer year, String label, Artist artist, Condition condition) {
        this.id = id;
        this.name = name;
        this.releaseYear = year;
        this.label = label;
        this.artist = artist;
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
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

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year=" + releaseYear +
                ", label='" + label + '\'' +
                ", artist=" + artist +
                ", condition=" + condition +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(id, album.id) && Objects.equals(name, album.name) && Objects.equals(releaseYear, album.releaseYear) && Objects.equals(label, album.label) && Objects.equals(artist, album.artist) && Objects.equals(condition, album.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, releaseYear, label, artist, condition);
    }
}