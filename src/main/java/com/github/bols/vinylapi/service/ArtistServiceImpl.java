package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.dao.MusicGroupDao;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Artist> findAll() {
        return (List<Artist>) artistDao.findAll();
    }

    @Override
    public Artist findByName(String name) {

        if (name == null) {
            throw new InvalidParameterException("Name cannot be null");
        }

        if (name.isEmpty()) {
            throw new InvalidParameterException("Name cannot be empty");
        }

        return artistDao.findByName(name).orElseThrow();
    }

    @Override
    public Artist findById(Integer id) {

        if (id == null) {
            throw new InvalidParameterException("Id cannot be null");
        }

        if (id < 1) {
            throw new InvalidParameterException("Id must be greater than zero");
        }

        return artistDao.findById(id).orElseThrow();
    }

    @Override
    public List<Artist> findByGroup(String group) {

        if (group == null) {
            throw new InvalidParameterException("Group cannot be null");
        }

        if (group.isEmpty()) {
            throw new InvalidParameterException("Group cannot be empty");
        }

        MusicGroup musicGroup = musicGroupDao.findByName(group).orElseThrow();

        return musicGroup.getMiembros().stream().toList();
    }

    @Override
    public Artist save(Artist artist) {

        if (artist == null) {
            throw new InvalidParameterException("Artist cannot be null");
        }

        if (artist.getName() == null) {
            throw new InvalidParameterException("Artist cannot be null");
        }

        if (artistDao.findByName(artist.getName()).isPresent()) {
            throw new InvalidParameterException("Artist already exists");
        }

        return artistDao.save(artist);
    }

    @Override
    public void delete(Artist artist) {

        if (artist == null) {
            throw new InvalidParameterException("Artist cannot be null");
        }

        if (artist.getName() == null) {
            throw new InvalidParameterException("Artist cannot be null");
        }

        if (artistDao.findByName(artist.getName()).isEmpty()) {
            throw new NoSuchElementException("Artist not found");
        }

        artistDao.delete(artist);
    }
}
