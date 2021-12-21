package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.dao.MusicGroupDao;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MusicGroupServiceImpl implements MusicGroupService {

    @Autowired
    private MusicGroupDao musicGroupDao;

    @Autowired
    private ArtistDao artistDao;

    @Override
    @Transactional(readOnly = true)
    public List<MusicGroup> findAll() {

        return (List<MusicGroup>) musicGroupDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MusicGroup findByName(String name) {

        return musicGroupDao.findByName(name).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public MusicGroup findById(Integer id) {

        return musicGroupDao.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public MusicGroup findByArtist(String artist) {

        Artist foundArtist = artistDao.findByName(artist).orElseThrow();

        // Artist only have real name if they are solo, not a group. Groups have null values on real name field
        if (foundArtist.getRealName() == null) {
            return musicGroupDao.findByName(artist).orElseThrow();
        }

        List<MusicGroup> musicGroups = (List<MusicGroup>) musicGroupDao.findAll();

        return musicGroups.stream()
                .filter(g -> g.getMembers().contains(foundArtist))
                .findFirst()
                .orElseThrow();
    }

    @Override
    @Transactional
    public void linkArtistToGroup(Integer musicGroupId, Integer artistId) {

        Artist artist = artistDao.findById(artistId).orElseThrow();

        // Artist only have real name if they are solo, not a group. Groups have null values on real name field
        if (artist.getRealName() == null) {
            throw new InvalidParameterException("Artist cannot be a group");
        }

        MusicGroup group = musicGroupDao.findById(musicGroupId).orElseThrow();

        if (group.getMembers().stream().anyMatch(m -> m.getId().equals(artist.getId()))) {
            throw new InvalidParameterException("Artist is already in the group");
        }

        group.addMember(artist);
        musicGroupDao.save(group);
    }

    @Override
    @Transactional
    public MusicGroup save(MusicGroup musicGroup) {

        if (musicGroupDao.findByName(musicGroup.getName()).isPresent()) {
            throw new InvalidParameterException("Group already exists");
        }

        return musicGroupDao.save(musicGroup);
    }

    @Override
    @Transactional
    public void delete(MusicGroup musicGroup) {

        if (musicGroupDao.findByName(musicGroup.getName()).isEmpty()){
            throw new NoSuchElementException("Group not found");
        }

        musicGroupDao.delete(musicGroup);
    }
}
