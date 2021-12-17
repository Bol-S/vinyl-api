package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService{

    @Autowired
    private ArtistDao artistDao;

    @Override
    public List<Artist> findAll() {
        return null;
    }

    @Override
    public Artist findByName(String name) {
        return null;
    }

    @Override
    public Artist findById(Integer id) {
        return null;
    }

    @Override
    public List<Artist> findByGroup(String group) {
        return null;
    }

    @Override
    public Artist save(Artist artist) {
        return null;
    }

    @Override
    public void delete(Artist artist) {

    }
}
