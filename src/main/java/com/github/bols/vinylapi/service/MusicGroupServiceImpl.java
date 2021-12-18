package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.dao.MusicGroupDao;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicGroupServiceImpl implements MusicGroupService {

    @Autowired
    private MusicGroupDao musicGroupDao;

    @Autowired
    private ArtistDao artistDao;

    @Override
    public List<MusicGroup> findAll() {

        return (List<MusicGroup>) musicGroupDao.findAll();
    }

    @Override
    public MusicGroup findByName(String name) {

        return musicGroupDao.findByName(name).orElseThrow();
    }

    @Override
    public MusicGroup findById(Integer id) {

        return musicGroupDao.findById(id).orElseThrow();
    }

    @Override
    public MusicGroup findByArtist(String artist) {

        Artist foundArtist = artistDao.findByName(artist).orElseThrow();

        // Artist only have real name if they are solo, not a group. Groups have null values on real name field
        if (foundArtist.getRealName() == null)
            return musicGroupDao.findByName(artist).orElseThrow();

        List<MusicGroup> musicGroups = (List<MusicGroup>) musicGroupDao.findAll();

        return musicGroups.stream()
                .filter(g -> g.getMiembros().contains(foundArtist))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void linkArtist(Integer musicGroupId, Integer artistId) {

    }

    @Override
    public MusicGroup save(MusicGroup musicGroup) {
        return null;
    }

    @Override
    public void delete(MusicGroup musicGroup) {

    }
}
