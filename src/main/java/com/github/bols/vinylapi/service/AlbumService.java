package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;

import java.util.List;

public interface AlbumService {

    List<Album> findAll();

    Album findByName(String name);

    Album findById(Integer id);

    List<Album> findByLabel(String label);

    List<Album> findByArtist(Artist artist);

    Album save(Album album);

    void delete(Album album);
}
