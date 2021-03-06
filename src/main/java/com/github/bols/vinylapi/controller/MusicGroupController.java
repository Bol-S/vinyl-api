package com.github.bols.vinylapi.controller;

import com.github.bols.vinylapi.model.MusicGroup;
import com.github.bols.vinylapi.service.MusicGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/groups")
public class MusicGroupController {

    @Autowired
    private MusicGroupService groupService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MusicGroup> getGroups(){
        return groupService.findAll();
    }

    @GetMapping("/find/name/{name}")
    public ResponseEntity<?> getGroupByName(@PathVariable String name){

        if (name == null || name.trim().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(groupService.findByName(name), HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find/id/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable(required = false) Integer id){

        try{
            if (id == null) {
                throw new InvalidParameterException("Missing id");
            }
            id = Integer.parseInt(String.valueOf(id));

            if (id < 1){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            MusicGroup musicGroup = groupService.findById(id);
            return new ResponseEntity<>(musicGroup, HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (InvalidParameterException | NumberFormatException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/find/artist/{artist}")
    public ResponseEntity<?> getGroupByArtist(@PathVariable String artist){

        if (artist == null || artist.trim().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(groupService.findByArtist(artist), HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/link/{groupId}/{artistId}")
    public ResponseEntity<?> linkArtistToGroup(@PathVariable(required = false) Integer groupId, @PathVariable(required = false) Integer artistId){

        try {
            if (groupId == null || artistId == null) {
                throw new InvalidParameterException("Missing id");
            }
            groupId = Integer.parseInt(String.valueOf(groupId));
            artistId = Integer.parseInt(String.valueOf(artistId));

        }
        catch (InvalidParameterException | NumberFormatException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try{
            if (groupId < 1 || artistId < 1){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            groupService.linkArtistToGroup(groupId, artistId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (InvalidParameterException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<?> addGroup(@RequestBody MusicGroup musicGroup){

        if (musicGroup == null || musicGroup.getName() == null || musicGroup.getName().isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(groupService.save(musicGroup), HttpStatus.CREATED);
        }
        catch (InvalidParameterException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeGroup(@PathVariable(required = false) Integer id){

        try {
            if (id == null) {
                throw new InvalidParameterException("Missing id");
            }
            id = Integer.parseInt(String.valueOf(id));

            if (id < 1){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            MusicGroup musicGroupToDelete = groupService.findById(id);
            groupService.delete(musicGroupToDelete);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (NoSuchElementException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (InvalidParameterException | NumberFormatException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
