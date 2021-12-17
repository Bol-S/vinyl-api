package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.TestData;
import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.dao.MusicGroupDao;
import com.github.bols.vinylapi.model.Artist;
import org.junit.jupiter.api.Disabled;
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

    @MockBean
    private MusicGroupDao musicGroupDao;

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

        when(artistDao.findByName("Onyx")).thenReturn(TestData.SampleArtist.getArtist01());
        when(artistDao.findByName("Skoob")).thenReturn(TestData.SampleArtist.getArtist06());
        when(artistDao.findByName("Jerobi")).thenThrow(NoSuchElementException.class);

        Artist artist1 = artistService.findByName("Onyx");
        assertNotNull(artist1);
        assertEquals(1, artist1.getId());
        assertNull(artist1.getRealName());

        Artist artist6 = artistService.findByName("Skoob");
        assertNotNull(artist6);
        assertEquals(6, artist6.getId());
        assertEquals("William Hines", artist6.getRealName());

        assertThrows(NoSuchElementException.class, () -> artistService.findByName("Jerobi"));

        assertThrows(InvalidParameterException.class, () -> artistService.findByName(null));
        assertThrows(InvalidParameterException.class, () -> artistService.findByName(""));

        verify(artistDao, times(3)).findByName(any());
    }

    @Test
    void testFindByGroup() {

        when(musicGroupDao.findByName("Onyx")).thenReturn(TestData.SampleMusicGroup.getMusicGroup01());
        when(musicGroupDao.findByName("Das Efx")).thenReturn(TestData.SampleMusicGroup.getMusicGroup01());
        when(musicGroupDao.findByName("A Tribe Called Quest")).thenThrow(NoSuchElementException.class);

        List<Artist> artists1 = artistService.findByGroup("Onyx");
        assertNotNull(artists1);
        assertEquals(2, artists1.size());
        assertEquals(2, artists1.get(0).getId());
        assertEquals("Fredro Starr", artists1.get(1).getName());

        List<Artist> artists2 = artistService.findByGroup("Das Efx");
        assertNotNull(artists2);
        assertEquals(2, artists2.size());
        assertEquals(5, artists2.get(0).getId());
        assertEquals("Skoob", artists2.get(1).getName());

        assertThrows(NoSuchElementException.class, () -> artistService.findByGroup("A Tribe Called Quest"));
        assertThrows(InvalidParameterException.class, () -> artistService.findByGroup(null));
        assertThrows(InvalidParameterException.class, () -> artistService.findByGroup(""));

        verify(musicGroupDao, times(2)).findByName(any());
        verify(artistDao, never()).findByName(any());
    }

    @Disabled
    @Test
    void testSave() {
        
    }

    @Disabled
    @Test
    void testDelete() {

    }
}