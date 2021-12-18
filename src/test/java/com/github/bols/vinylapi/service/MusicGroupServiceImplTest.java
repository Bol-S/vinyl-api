package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.TestData;
import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.dao.MusicGroupDao;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.InvalidParameterException;
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
        assertEquals(2, groups.get(0).getMembers().size());
        assertEquals(2, groups.get(1).getMembers().size());
        assertTrue(groups.stream().anyMatch(g -> "Onyx".equals(g.getName())));
        assertTrue(groups.get(1).getMembers().stream().anyMatch(a -> "William Hines".equals(a.getRealName())));

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
        assertEquals(2, onyx.getMembers().size());
        assertEquals(2, dasefx.getMembers().size());
        assertTrue(onyx.getMembers().stream().anyMatch(a -> "Kirk Jones".equals(a.getRealName())));
        assertTrue(dasefx.getMembers().stream().anyMatch(a -> 5 == a.getId()));


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
        assertEquals(2, onyx.getMembers().size());
        assertEquals(2, dasefx.getMembers().size());
        assertTrue(onyx.getMembers().stream().anyMatch(a -> "Kirk Jones".equals(a.getRealName())));
        assertTrue(dasefx.getMembers().stream().anyMatch(a -> 5 == a.getId()));

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
        assertEquals(2, onyx.getMembers().size());
        assertEquals(2, dasefx.getMembers().size());
        assertTrue(onyx.getMembers().stream().anyMatch(a -> "Kirk Jones".equals(a.getRealName())));
        assertTrue(dasefx.getMembers().stream().anyMatch(a -> 5 == a.getId()));

        assertThrows(NoSuchElementException.class, () -> musicGroupService.findByArtist("Q-Tip"));

        verify(musicGroupDao).findAll();
        verify(musicGroupDao).findByName(any());
        verify(artistDao, times(3)).findByName(any());
    }

    @Test
    void testLinkArtistToGroup() {

        MusicGroup group = TestData.SampleMusicGroup.getMusicGroup01().orElseThrow();
        Artist newArtist = new Artist(7, "Q-Tip", "Jonathan Davis");
        MusicGroup groupWithNewMember = TestData.SampleMusicGroup.getMusicGroup01().orElseThrow();
        groupWithNewMember.addMember(newArtist);
        Artist groupArtist = new Artist(8, "A Tribe Called Quest", null);

        when(artistDao.findById(1)).thenReturn(TestData.SampleArtist.getArtist01());
        when(artistDao.findById(2)).thenReturn(TestData.SampleArtist.getArtist02());
        when(artistDao.findById(7)).thenReturn(java.util.Optional.of(newArtist));
        when(artistDao.findById(8)).thenReturn(java.util.Optional.of(groupArtist));

        when(musicGroupDao.findById(1)).thenReturn(java.util.Optional.of(group));
        when(musicGroupDao.save(group)).thenReturn(groupWithNewMember);


        musicGroupService.linkArtistToGroup(1, 7);


        assertEquals(3, group.getMembers().size());
        assertTrue(group.getMembers().stream().anyMatch(a -> a.getId() == 7));

        // Try to add artist that is a group, not solo
        assertThrows(InvalidParameterException.class, () -> musicGroupService.linkArtistToGroup(1, 8));

        // Try to add artist already in group
        assertThrows(InvalidParameterException.class, () -> musicGroupService.linkArtistToGroup(1, 2));

        verify(musicGroupDao).save(any());
    }

    @Test
    void testSave() {

        MusicGroup group = new MusicGroup(null, "A Tribe Called Quest", null);
        when(musicGroupDao.save(group)).then(invocationOnMock -> {
            group.setId(3);
            return group;
        });
        MusicGroup duplicatedGroup = TestData.SampleMusicGroup.getMusicGroup01().orElseThrow();
        when(musicGroupDao.findByName("Onyx")).thenReturn(java.util.Optional.of(duplicatedGroup));


        MusicGroup savedGroup = musicGroupService.save(group);


        assertNotNull(savedGroup);
        assertEquals(3, savedGroup.getId());
        assertEquals("A Tribe Called Quest", savedGroup.getName());
        assertTrue(savedGroup.getMembers().isEmpty());

        assertThrows(InvalidParameterException.class, () -> musicGroupService.save(duplicatedGroup));

        verify(musicGroupDao).save(group);
        verify(musicGroupDao, times(1)).save(any());
    }

    @Test
    void testDelete() {

        MusicGroup groupToDelete = TestData.SampleMusicGroup.getMusicGroup01().orElseThrow();
        when(musicGroupDao.findByName("Onyx")).thenReturn(java.util.Optional.of(groupToDelete));
        MusicGroup fakeMusicGroup = new MusicGroup(null, "A Tribe Called Quest", null);

        musicGroupService.delete(groupToDelete);

        assertThrows(NoSuchElementException.class, () -> musicGroupService.delete(fakeMusicGroup));

        verify(musicGroupDao).delete(groupToDelete);
    }
}