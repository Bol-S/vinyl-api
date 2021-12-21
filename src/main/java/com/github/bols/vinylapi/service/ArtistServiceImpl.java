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
public class ArtistServiceImpl implements ArtistService {

    @Autowired
    private ArtistDao artistDao;

    @Autowired
    private MusicGroupDao musicGroupDao;

    @Override
    @Transactional(readOnly = true)
    public List<Artist> findAll() {
        return (List<Artist>) artistDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Artist findByName(String name) {

        return artistDao.findByName(name).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Artist findById(Integer id) {

        return artistDao.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artist> findByGroup(String group) {

        MusicGroup musicGroup = musicGroupDao.findByName(group).orElseThrow();

        return musicGroup.getMembers().stream().toList();
    }

    @Override
    @Transactional
    public Artist save(Artist artist) {

        if (artistDao.findByName(artist.getName()).isPresent()) {
            throw new InvalidParameterException("Artist already exists");
        }

        return artistDao.save(artist);
    }

    @Override
    @Transactional
    public void delete(Artist artist) {

        if (artistDao.findByName(artist.getName()).isEmpty()) {
            throw new NoSuchElementException("Artist not found");
        }

        artistDao.delete(artist);
    }
}
