package com.github.bols.vinylapi.controller;

import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.service.AlbumService;
import com.github.bols.vinylapi.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ArtistService artistService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Album> getAlbums(){
        return albumService.findAll();
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<?> getAlbumByName(@PathVariable String name){

        if (name == null || name.trim().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(albumService.findByName(name), HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find/id/{id}")
    public ResponseEntity<?> getAlbumById(@PathVariable Integer id){

        try{
            if (id < 1){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Album album = albumService.findById(id);
            return new ResponseEntity<>(album, HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find/label/{label}")
    public ResponseEntity<?> getAlbumByLabel(@PathVariable String label){

        if (label == null || label.trim().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(albumService.findByLabel(label), HttpStatus.OK);
    }

    @GetMapping("/find/artist/{artist}")
    public ResponseEntity<?> getAlbumByArtist(@PathVariable String artist){

        if (artist == null || artist.trim().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            Artist oArtist = artistService.findByName(artist);
            return new ResponseEntity<>(albumService.findByArtist(oArtist), HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addAlbum(@RequestBody Album album){

        if (validateAlbumParams(album)) {
            try {
                return new ResponseEntity<>(albumService.save(album), HttpStatus.CREATED);
            } catch (InvalidParameterException exception) {
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validateAlbumParams(Album album){
        return !(album == null || album.getName() == null || album.getName().isEmpty() ||
                album.getReleaseYear() == null || album.getReleaseYear() < 1900 ||
                album.getLabel() == null || album.getLabel().isEmpty() ||
                album.getArtist() == null || album.getArtist().getName() == null || album.getArtist().getName().isEmpty());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAlbum(@PathVariable Integer id){

        try {
            if (id < 1){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Album albumToDelete = albumService.findById(id);
            albumService.delete(albumToDelete);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
