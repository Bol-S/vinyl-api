package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.TestData;
import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ArtistServiceImplTest {

    @MockBean
    private ArtistDao artistDao;

    @Autowired
    ArtistService artistService;

    @Test
    void testFindAll() {

        when(artistDao.findAll()).thenReturn(TestData.SampleArtist.getAllArtists());

        List<Artist> artists = artistService.findAll();

        assertNotNull(artists);
        assertEquals(6, artists.size());
        assertEquals("Kirk Jones", artists.get(1).getRealName());
        assertNull(artists.get(3).getRealName());
        assertEquals(5, artists.get(4).getId());
        assertEquals("Onyx", artists.get(0).getName());

        verify(artistDao).findAll();
    }

    @Test
    void testFindById() {

        when(artistDao.findById(1)).thenReturn(TestData.SampleArtist.getArtist01());
        when(artistDao.findById(5)).thenReturn(TestData.SampleArtist.getArtist05());
        when(artistDao.findById(9)).thenThrow(NoSuchElementException.class);

        Artist artist1 = artistService.findById(1);
        assertNotNull(artist1);
        assertEquals("Onyx", artist1.getName());
        assertNull(artist1.getRealName());

        Artist artist5 = artistService.findById(5);
        assertNotNull(artist5);
        assertEquals("Krazy Drayz", artist5.getName());
        assertEquals("Andre Weston", artist5.getRealName());

        assertThrows(NoSuchElementException.class, () -> artistService.findById(9));

        assertThrows(InvalidParameterException.class, () -> artistService.findById(null));
        assertThrows(InvalidParameterException.class, () -> artistService.findById(0));
        assertThrows(InvalidParameterException.class, () -> artistService.findById(-1));
        assertThrows(InvalidParameterException.class, () -> artistService.findById(-1000));

        verify(artistDao, times(3)).findById(any());
    }

    @Test
    void testFindByName() {
        
    }

    @Test
    void testFindByGroup() {
        
    }

    @Test
    void testSave() {
        
    }

    @Test
    void testDelete() {

    }
}