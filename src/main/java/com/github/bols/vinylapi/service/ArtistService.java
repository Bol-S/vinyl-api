package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.model.Artist;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

public interface ArtistService {

    List<Artist> findAll();

    Artist findByName(String name) throws NoSuchElementException;

    Artist findById(Integer id) throws NoSuchElementException;

    List<Artist> findByGroup(String group) throws NoSuchElementException;

    Artist save(Artist artist) throws InvalidParameterException;

    void delete(Artist artist) throws NoSuchElementException;
}
