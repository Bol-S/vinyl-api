package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

public interface AlbumService {

    List<Album> findAll();

    Album findByName(String name) throws NoSuchElementException;

    Album findById(Integer id) throws NoSuchElementException;

    List<Album> findByLabel(String label);

    List<Album> findByArtist(Artist artist) throws NoSuchElementException;

    Album save(Album album) throws InvalidParameterException;

    void delete(Album album) throws NoSuchElementException;
}
