package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.model.Artist;

import java.util.List;

public interface ArtistService {

    List<Artist> findAll();

    Artist findByName(String name);

    Artist findById(Integer id);

    List<Artist> findByGroup(String group);

    Artist save(Artist artist);

    void delete(Artist artist);
}
