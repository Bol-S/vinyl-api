package com.github.bols.vinylapi.dao;

import com.github.bols.vinylapi.model.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistDao extends CrudRepository<Artist, Integer> {

    Optional<Artist> findByName(String name);

    @Query(value = "select a.* from artists a, music_groups g, members m " +
            "where a.artist_id = m.artist_id " +
            "and m.group_id = g.group_id " +
            "and g.name = ?1", nativeQuery = true)
    List<Artist> findByGroup(String group);
}
