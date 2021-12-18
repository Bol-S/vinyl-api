package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.TestData;
import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.dao.MusicGroupDao;
import com.github.bols.vinylapi.model.MusicGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MusicGroupServiceImplTest {

    @MockBean
    private MusicGroupDao musicGroupDao;

    @MockBean
    private ArtistDao artistDao;

    @Autowired
    private MusicGroupService musicGroupService;

    @Test
    void testFindAll() {

        when(musicGroupDao.findAll()).thenReturn(TestData.SampleMusicGroup.getAllMusicGroups());

        List<MusicGroup> groups = musicGroupService.findAll();

        assertNotNull(groups);
        assertEquals(2, groups.size());
        assertEquals(2, groups.get(0).getMiembros().size());
        assertEquals(2, groups.get(1).getMiembros().size());
        assertTrue(groups.stream().anyMatch(g -> "Onyx".equals(g.getName())));
        assertTrue(groups.get(1).getMiembros().stream().anyMatch(a -> "William Hines".equals(a.getRealName())));

        verify(musicGroupDao).findAll();
    }

    @Test
    void testFindByName() {

        when(musicGroupDao.findByName("Onyx")).thenReturn(TestData.SampleMusicGroup.getMusicGroup01());
        when(musicGroupDao.findByName("Das Efx")).thenReturn(TestData.SampleMusicGroup.getMusicGroup02());
        when(musicGroupDao.findByName("A Tribe Called Quest")).thenThrow(NoSuchElementException.class);

        MusicGroup onyx = musicGroupService.findByName("Onyx");
        MusicGroup dasefx = musicGroupService.findByName("Das Efx");

        assertNotNull(onyx);
        assertNotNull(dasefx);
        assertEquals("Onyx", onyx.getName());
        assertEquals("Das Efx", dasefx.getName());
        assertEquals(2, onyx.getMiembros().size());
        assertEquals(2, dasefx.getMiembros().size());
        assertTrue(onyx.getMiembros().stream().anyMatch(a -> "Kirk Jones".equals(a.getRealName())));
        assertTrue(dasefx.getMiembros().stream().anyMatch(a -> 5 == a.getId()));


        assertThrows(NoSuchElementException.class, () -> musicGroupService.findByName("A Tribe Called Quest"));

        verify(musicGroupDao, times(3)).findByName(any());
    }

    @Test
    void testFindById() {

        when(musicGroupDao.findById(1)).thenReturn(TestData.SampleMusicGroup.getMusicGroup01());
        when(musicGroupDao.findById(2)).thenReturn(TestData.SampleMusicGroup.getMusicGroup02());
        when(musicGroupDao.findById(6)).thenThrow(NoSuchElementException.class);

        MusicGroup onyx = musicGroupService.findById(1);
        MusicGroup dasefx = musicGroupService.findById(2);

        assertNotNull(onyx);
        assertNotNull(dasefx);
        assertEquals("Onyx", onyx.getName());
        assertEquals("Das Efx", dasefx.getName());
        assertEquals(2, onyx.getMiembros().size());
        assertEquals(2, dasefx.getMiembros().size());
        assertTrue(onyx.getMiembros().stream().anyMatch(a -> "Kirk Jones".equals(a.getRealName())));
        assertTrue(dasefx.getMiembros().stream().anyMatch(a -> 5 == a.getId()));

        assertThrows(NoSuchElementException.class, () -> musicGroupService.findById(9));

        verify(musicGroupDao, times(3)).findById(any());
    }

    @Test
    void testFindByArtist() {

        when(musicGroupDao.findAll()).thenReturn(TestData.SampleMusicGroup.getAllMusicGroups());
        when(musicGroupDao.findByName("Onyx")).thenReturn(TestData.SampleMusicGroup.getMusicGroup01());
        when(artistDao.findByName("Onyx")).thenReturn(TestData.SampleArtist.getArtist01());
        when(artistDao.findByName("Krazy Drayz")).thenReturn(TestData.SampleArtist.getArtist05());
        when(artistDao.findByName("Q-Tip")).thenThrow(NoSuchElementException.class);

        MusicGroup onyx = musicGroupService.findByArtist("Onyx");
        MusicGroup dasefx = musicGroupService.findByArtist("Krazy Drayz");

        assertNotNull(onyx);
        assertNotNull(dasefx);
        assertEquals("Onyx", onyx.getName());
        assertEquals("Das Efx", dasefx.getName());
        assertEquals(2, onyx.getMiembros().size());
        assertEquals(2, dasefx.getMiembros().size());
        assertTrue(onyx.getMiembros().stream().anyMatch(a -> "Kirk Jones".equals(a.getRealName())));
        assertTrue(dasefx.getMiembros().stream().anyMatch(a -> 5 == a.getId()));

        assertThrows(NoSuchElementException.class, () -> musicGroupService.findByArtist("Q-Tip"));

        verify(musicGroupDao).findAll();
        verify(musicGroupDao).findByName(any());
        verify(artistDao, times(3)).findByName(any());
    }

    @Test
    void testLinkArtist() {


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