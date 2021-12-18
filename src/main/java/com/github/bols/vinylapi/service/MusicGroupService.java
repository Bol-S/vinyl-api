package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;

import java.util.List;

public interface MusicGroupService {

    List<MusicGroup> findAll();

    MusicGroup findByName(String name);

    MusicGroup findById(Integer id);

    MusicGroup findByArtist(String artist);

    void linkArtist(Integer musicGroupId, Integer artistId);

    MusicGroup save(MusicGroup musicGroup);

    void delete(MusicGroup musicGroup);
}
