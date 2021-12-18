package com.github.bols.vinylapi.service;

import com.github.bols.vinylapi.TestData;
import com.github.bols.vinylapi.dao.AlbumDao;
import com.github.bols.vinylapi.dao.ArtistDao;
import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.enums.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AlbumServiceImplTest {

    @MockBean
    private AlbumDao albumDao;

    @MockBean
    private ArtistDao artistDao;

    @Autowired
    private AlbumService albumService;

    @Test
    void testFindAll() {

        when(albumDao.findAll()).thenReturn(TestData.SampleAlbum.getAllAlbums());

        List<Album> albums = albumService.findAll();

        assertNotNull(albums);
        assertEquals(10, albums.size());
        assertEquals(Condition.VERY_GOOD_PLUS, albums.get(1).getCondition());
        assertTrue(albums.stream().anyMatch(a -> "Bacdafucup".equals(a.getName())));

        verify(albumDao).findAll();
    }

    @Test
    void testFindByName() {

        when(albumDao.findByName("Bacdafucup")).thenReturn(TestData.SampleAlbum.getAlbum01());
        when(albumDao.findByName("Firestarr")).thenReturn(TestData.SampleAlbum.getAlbum07());
        when(albumDao.findByName("Midnight Marauders")).thenThrow(NoSuchElementException.class);

        Album bacdafucup = albumService.findByName("Bacdafucup");
        Album firestarr = albumService.findByName("Firestarr");

        assertNotNull(bacdafucup);
        assertNotNull(firestarr);
        assertEquals(1, bacdafucup.getId());
        assertEquals(7, firestarr.getId());
        assertEquals("Onyx", bacdafucup.getArtist().getName());
        assertEquals("Fredro Starr", firestarr.getArtist().getName());

        assertThrows(NoSuchElementException.class, () -> albumService.findByName("Midnight Marauders"));

        verify(albumDao, times(3)).findByName(any());
    }

    @Test
    void testFindById() {

        when(albumDao.findById(1)).thenReturn(TestData.SampleAlbum.getAlbum01());
        when(albumDao.findById(7)).thenReturn(TestData.SampleAlbum.getAlbum07());
        when(albumDao.findById(19)).thenThrow(NoSuchElementException.class);

        Album bacdafucup = albumService.findById(1);
        Album firestarr = albumService.findById(7);

        assertNotNull(bacdafucup);
        assertNotNull(firestarr);
        assertEquals("Bacdafucup", bacdafucup.getName());
        assertEquals("Firestarr", firestarr.getName());
        assertEquals("Onyx", bacdafucup.getArtist().getName());
        assertEquals("Fredro Starr", firestarr.getArtist().getName());

        assertThrows(NoSuchElementException.class, () -> albumService.findById(19));

        verify(albumDao, times(3)).findById(any());
    }

    @Test
    void testFindByLabel() {

        when(albumDao.findByLabel("JMJ"))
                .thenReturn(TestData.SampleAlbum.getAllAlbums()
                        .stream().filter(a -> "JMJ".equals(a.getLabel()))
                        .collect(Collectors.toList()));
        when(albumDao.findByLabel("EastWest Records America"))
                .thenReturn(TestData.SampleAlbum.getAllAlbums()
                        .stream().filter(a -> "EastWest Records America".equals(a.getLabel()))
                        .collect(Collectors.toList()));
        when(albumDao.findByLabel("Koch Records"))
                .thenReturn(TestData.SampleAlbum.getAllAlbums()
                        .stream().filter(a -> "Koch Records".equals(a.getLabel()))
                        .collect(Collectors.toList()));
        when(albumDao.findByLabel(null)).thenReturn(TestData.SampleAlbum.getAllAlbums()
                .stream().filter(a -> null == a.getLabel())
                .collect(Collectors.toList()));
        when(albumDao.findByLabel("Interscope")).thenThrow(NoSuchElementException.class);

        List<Album> jmj = albumService.findByLabel("JMJ");
        List<Album> ewra = albumService.findByLabel("EastWest Records America");
        List<Album> koch = albumService.findByLabel("Koch Records");
        List<Album> nolabel = albumService.findByLabel(null);

        assertNotNull(jmj);
        assertNotNull(ewra);
        assertNotNull(koch);
        assertNotNull(nolabel);
        assertEquals(3, jmj.size());
        assertEquals(2, ewra.size());
        assertEquals(1, koch.size());
        assertEquals(2, nolabel.size());
        assertEquals(3, jmj.stream().filter(a -> "Onyx".equals(a.getArtist().getName())).count());
        assertEquals(2, ewra.stream().filter(a -> "Das Efx".equals(a.getArtist().getName())).count());
        assertEquals(1, koch.stream().filter(a -> "Fredro Starr".equals(a.getArtist().getName())).count());
        assertEquals(2, nolabel.stream().filter(a -> "Krazy Drayz".equals(a.getArtist().getName())).count());
        assertEquals(1995, jmj.stream().filter(a -> "All We Got Iz Us".equals(a.getName())).findFirst().orElseThrow().getReleaseYear());
        assertEquals(Condition.MINT, ewra.stream().filter(a -> "Straight Up Sewaside".equals(a.getName())).findFirst().orElseThrow().getCondition());
        assertThrows(NoSuchElementException.class, () -> albumService.findByLabel("Interscope"));

        verify(albumDao, times(5)).findByLabel(any());
    }

    @Test
    void testFindByArtist() {

        Artist artist = TestData.SampleArtist.getArtist04().orElseThrow();
        when(artistDao.findByName("Das Efx")).thenReturn(java.util.Optional.of(artist));
        when(albumDao.findByArtist("Das Efx"))
                .thenReturn(TestData.SampleAlbum.getAllAlbums()
                        .stream().filter(a -> "Das Efx".equals(a.getArtist().getName()))
                        .collect(Collectors.toList()));
        Artist fakeArtist = new Artist(28, "A Tribe Called Quest", null);
        when(albumDao.findByName(fakeArtist.getName())).thenThrow(NoSuchElementException.class);


        List<Album> albums = albumService.findByArtist(artist);


        assertNotNull(albums);
        assertEquals(2, albums.size());
        assertTrue(albums.stream().anyMatch(a -> "Dead Serious".equals(a.getName())));
        assertTrue(albums.stream().anyMatch(a -> "Straight Up Sewaside".equals(a.getName())));

        assertThrows(NoSuchElementException.class, () -> albumService.findByArtist(fakeArtist));

        verify(albumDao, times(2)).findByArtist(any());
    }

    @Test
    void testSave() {

        Album album = new Album(null,
                "Hold It Down",
                1995,
                "EastWest Records America",
                TestData.SampleArtist.getArtist04().orElseThrow(),
                Condition.GOOD);
        when(albumDao.save(album)).then(invocationOnMock -> {
            album.setId(11);
            return album;
        });
        Album duplicatedAlbum = TestData.SampleAlbum.getAlbum03().orElseThrow();
        when(albumDao.findByName(duplicatedAlbum.getName())).thenReturn(java.util.Optional.of(duplicatedAlbum));

        Album savedAlbum = albumService.save(album);

        assertNotNull(savedAlbum);
        assertEquals(11, savedAlbum.getId());
        assertEquals("Hold It Down", savedAlbum.getName());
        assertEquals("Das Efx", savedAlbum.getArtist().getName());

        assertThrows(InvalidParameterException.class, () -> albumService.save(duplicatedAlbum));

        verify(albumDao).save(album);
        verify(albumDao, times(1)).save(any());
    }

    @Test
    void testDelete() {

        Album albumToDelete = TestData.SampleAlbum.getAlbum01().orElseThrow();
        when(albumDao.findByName("Bacdafucup")).thenReturn(java.util.Optional.of(albumToDelete));
        Album fakeAlbum = new Album(null,
                "Hold It Down",
                1995,
                "EastWest Records America",
                TestData.SampleArtist.getArtist04().orElseThrow(),
                Condition.GOOD);

        albumService.delete(albumToDelete);

        assertThrows(NoSuchElementException.class, () -> albumService.delete(fakeAlbum));

        verify(albumDao).delete(albumToDelete);
        verify(albumDao, times(1)).delete(any());
    }
}