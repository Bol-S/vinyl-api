package com.github.bols.vinylapi.dao;

import com.github.bols.vinylapi.model.Artist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistDao extends CrudRepository<Artist, Integer> {

    Optional<Artist> findByName(String name);
}
