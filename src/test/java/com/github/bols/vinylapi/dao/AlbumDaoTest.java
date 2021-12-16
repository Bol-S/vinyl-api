package com.github.bols.vinylapi.dao;

import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.enums.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AlbumDaoTest {

    @Autowired
    private AlbumDao albumDao;

    @Autowired
    private ArtistDao artistDao;

    @Test
    void testFindAll() {

        List<Album> albums = (List<Album>) albumDao.findAll();

        assertNotNull(albums);
        assertEquals(10, albums.size());
        assertEquals(1, albums.get(2).getArtist().getId());
        assertEquals("Andre Weston", albums.get(9).getArtist().getRealName());
    }

    @Test
    void testFindById() {

        Album album = albumDao.findById(7).orElseThrow();

        assertNotNull(album);
        assertEquals("Firestarr", album.getName());
        assertEquals("Fredro Starr", album.getArtist().getName());
    }

    @Test
    void testFindByName() {

        Album album = albumDao.findByName("Dead Serious").orElseThrow();

        assertNotNull(album);
        assertEquals(1991, album.getReleaseYear());
        assertEquals("EastWest Records America", album.getLabel());
        assertEquals(4, album.getArtist().getId());
        assertEquals(Condition.NEAR_MINT, album.getCondition());

        assertThrows(NoSuchElementException.class, () -> albumDao.findByName("asdf").orElseThrow());
    }

    @Test
    void findByLabel() {

        List<Album> albums = albumDao.findByLabel("EastWest Records America");

        assertNotNull(albums);
        assertEquals(2, albums.size());
        assertTrue(albums.contains(albumDao.findByName("Dead Serious").orElseThrow()));
    }

    @Test
    void findByArtist() {

        List<Album> albums = albumDao.findByArtist("Das Efx");

        assertNotNull(albums);
        assertEquals(2, albums.size());
        assertTrue(albums.contains(albumDao.findByName("Straight Up Sewaside").orElseThrow()));
    }

    @Test
    void testSave() {

        Album album = new Album();
        Artist onyx = artistDao.findByName("Onyx").orElseThrow();

        album.setName("Black Rock");
        album.setArtist(onyx);
        album.setReleaseYear(2018);
        album.setLabel("X-Ray Records");
        album.setCondition(Condition.VERY_GOOD_PLUS);

        Album savedAlbum = albumDao.save(album);

        assertNotNull(savedAlbum);
        assertEquals("Black Rock", savedAlbum.getName());
        assertEquals(onyx, savedAlbum.getArtist());
        assertEquals(2018, savedAlbum.getReleaseYear());
        assertEquals("X-Ray Records", savedAlbum.getLabel());
        assertEquals(Condition.VERY_GOOD_PLUS, savedAlbum.getCondition());
    }

    @Test
    void testUpdate() {

        Album album = new Album();
        Artist dasefx = artistDao.findByName("Das Efx").orElseThrow();

        album.setName("Hold t Down");
        album.setArtist(dasefx);
        album.setReleaseYear(1996);
        album.setLabel("EastWest Records America");
        album.setCondition(Condition.NEAR_MINT);

        albumDao.save(album);

        album.setName("Hold It Down");
        album.setReleaseYear(1995);
        albumDao.save(album);

        Album retrievedAlbum = albumDao.findByName("Hold It Down").orElseThrow();


        assertNotNull(retrievedAlbum);
        assertEquals(1995, retrievedAlbum.getReleaseYear());
        assertEquals(dasefx, retrievedAlbum.getArtist());
        assertEquals("EastWest Records America", retrievedAlbum.getLabel());
        assertEquals(Condition.NEAR_MINT, retrievedAlbum.getCondition());
    }

    @Test
    void testDelete() {

        Album album = new Album();
        Artist dasefx = artistDao.findByName("Das Efx").orElseThrow();

        album.setName("Generation EFX");
        album.setArtist(dasefx);
        album.setReleaseYear(1998);
        album.setLabel("EastWest Records America");
        album.setCondition(Condition.VERY_GOOD_PLUS);

        albumDao.save(album);

        albumDao.delete(album);

        assertThrows(NoSuchElementException.class, () -> albumDao.findByName("Generation EFX").orElseThrow());
    }
}