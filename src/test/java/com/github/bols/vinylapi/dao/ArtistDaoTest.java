package com.github.bols.vinylapi.dao;

import com.github.bols.vinylapi.model.Artist;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArtistDaoTest {

    @Autowired
    private ArtistDao artistDao;

    @Test
    void testFindAll() {
        List<Artist> artists = (List<Artist>) artistDao.findAll();

        assertNotNull(artists);
        assertEquals(6, artists.size());
        assertEquals("Fredro Starr", artists.get(2).getName());
        assertNull(artists.get(3).getRealName());
        assertEquals("William Hines", artists.get(5).getRealName());
    }

    @Test
    void testFindById() {
        Artist artist = artistDao.findById(1).orElseThrow();

        assertNotNull(artist);
        assertEquals("Onyx", artist.getName());
        assertNull(artist.getRealName());
    }

    @Test
    void testFindByName() {
        Artist artist = artistDao.findByName("Das Efx").orElseThrow();

        assertNotNull(artist);
        assertEquals(4, artist.getId());
        assertEquals("Das Efx", artist.getName());
        assertNull(artist.getRealName());
    }

    @Order(1)
    @Test
    void testSave() {
        Artist artist = artistDao.save(new Artist(7, "A Tribe Called Quest", null));

        assertNotNull(artist);
        assertEquals(7, artist.getId());
        assertEquals("A Tribe Called Quest", artist.getName());
        assertNull(artist.getRealName());


        Artist foundArtist = artistDao.findByName("A Tribe Called Quest").orElseThrow();

        assertNotNull(foundArtist);
        assertEquals(7, foundArtist.getId());
        assertEquals("A Tribe Called Quest", foundArtist.getName());
        assertNull(foundArtist.getRealName());
    }

    @Order(2)
    @Test
    void testUpdate() {
        Artist artist = artistDao.save(new Artist(8, "Q Tip", "Jonathan Davis"));

        artist.setName("Q-Tip");
        Artist savedArtist = artistDao.save(artist);

        assertNotNull(savedArtist);
        assertEquals(8, savedArtist.getId());
        assertEquals("Q-Tip", savedArtist.getName());
        assertEquals("Jonathan Davis", savedArtist.getRealName());
    }

    @Order(3)
    @Test
    void testDelete() {
        Artist artist = artistDao.save(new Artist(9, "Phife Dawg", "Malik Taylor"));

        artistDao.delete(artist);

        assertThrows(NoSuchElementException.class, () -> artistDao.findByName("Phife Dawg").orElseThrow());
    }
}