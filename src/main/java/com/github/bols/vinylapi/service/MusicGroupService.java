package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

public interface MusicGroupService {

    List<MusicGroup> findAll();

    MusicGroup findByName(String name) throws NoSuchElementException;

    MusicGroup findById(Integer id) throws NoSuchElementException;

    MusicGroup findByArtist(String artist) throws NoSuchElementException;

    void linkArtistToGroup(Integer musicGroupId, Integer artistId) throws NoSuchElementException, InvalidParameterException;

    MusicGroup save(MusicGroup musicGroup) throws NoSuchElementException;

    void delete(MusicGroup musicGroup) throws InvalidParameterException;
}
