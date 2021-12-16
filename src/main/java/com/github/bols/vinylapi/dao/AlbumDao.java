package com.github.bols.vinylapi.dao;

import com.github.bols.vinylapi.model.Album;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumDao extends CrudRepository<Album, Integer> {

    Optional<Album> findByName(String name);

    List<Album> findByLabel(String name);

    @Query("select al from Album al join fetch al.artist ar where ar.name = ?1")
    List<Album> findByArtist(String name);
}
