package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.dao.AlbumDao;
import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AlbumServiceImpl implements AlbumService{

    @Autowired
    private AlbumDao albumDao;

    @Override
    public List<Album> findAll() {
        return (List<Album>) albumDao.findAll();
    }

    @Override
    public Album findByName(String name) {
        return albumDao.findByName(name).orElseThrow();
    }

    @Override
    public Album findById(Integer id) {
        return albumDao.findById(id).orElseThrow();
    }

    @Override
    public List<Album> findByLabel(String label) {
        return albumDao.findByLabel(label);
    }

    @Override
    public List<Album> findByArtist(Artist artist) {

        List<Album> album = albumDao.findByArtist(artist.getName());

        if (album.isEmpty()) {
            throw new NoSuchElementException("No albums found for this artist");
        }

        return album;
    }

    @Override
    public Album save(Album album) {

        if (albumDao.findByName(album.getName()).isPresent()) {
            throw new InvalidParameterException("Album already exists");
        }

        return albumDao.save(album);
    }

    @Override
    public void delete(Album album) {

        if (albumDao.findByName(album.getName()).isEmpty()){
            throw new NoSuchElementException("Album not found");
        }

        albumDao.delete(album);
    }
}
