package com.github.bols.vinylapi.controller;

import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Artist> getArtists() {
        return artistService.findAll();
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<?> getArtistByName(@PathVariable String name) {

        if (name == null || name.trim().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(artistService.findByName(name), HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find/id/{id}")
    public ResponseEntity<?> getArtistById(@PathVariable Integer id) {

        try{
            if (id < 1){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Artist artist = artistService.findById(id);
            return new ResponseEntity<>(artist, HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/find/group/{group}")
    public ResponseEntity<?> getArtistByGroup(@PathVariable String group){

        if (group == null || group.trim().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(artistService.findByGroup(group), HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> addArtist(@RequestBody Artist artist){

        if (artist == null || artist.getName() == null || artist.getName().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(artistService.save(artist), HttpStatus.CREATED);
        }
        catch (InvalidParameterException exception){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeArtist(@PathVariable Integer id){

        try {
            if (id < 1){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Artist artistToDelete = artistService.findById(id);
            artistService.delete(artistToDelete);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
