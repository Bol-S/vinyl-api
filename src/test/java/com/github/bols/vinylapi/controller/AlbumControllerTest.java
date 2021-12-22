package com.github.bols.vinylapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bols.vinylapi.TestData;
import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.enums.Condition;
import com.github.bols.vinylapi.service.AlbumService;
import com.github.bols.vinylapi.service.ArtistService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlbumController.class)
class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @MockBean
    private ArtistService artistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAlbums() throws Exception {

        List<Album> albums = TestData.SampleAlbum.getAllAlbums();
        when(albumService.findAll()).thenReturn(albums);

        mockMvc.perform(get("/albums"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(albums)));
    }

    @Test
    void testGetAlbumByName() throws Exception {

        String endpoint = "/albums/find/name/{name}";

        Album album = TestData.SampleAlbum.getAlbum01().orElseThrow();
        when(albumService.findByName("Bacdafucup")).thenReturn(album);
        when(albumService.findByName("Midnight Marauders")).thenThrow(NoSuchElementException.class);


        mockMvc.perform(
                        get(endpoint, "Bacdafucup"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(album)));

        mockMvc.perform(get(endpoint, "Midnight Marauders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, " ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(albumService, times(2)).findByName(any());
    }

    @Test
    void testGetAlbumById() throws Exception {

        String endpoint = "/albums/find/id/{id}";

        Album album = TestData.SampleAlbum.getAlbum01().orElseThrow();
        when(albumService.findById(1)).thenReturn(album);
        when(albumService.findById(19)).thenThrow(NoSuchElementException.class);


        mockMvc.perform(
                        get(endpoint, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(album)));

        mockMvc.perform(
                        get(endpoint, "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(album)));

        mockMvc.perform(get(endpoint, 19))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, "asdf"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(endpoint, -1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(endpoint, " "))
                .andExpect(status().is5xxServerError());

        verify(albumService, times(3)).findById(any());
    }

    @Test
    void testGetAlbumByLabel() throws Exception {

        String endpoint = "/albums/find/label/{label}";

        List<Album> albums = Arrays.asList(
                                        TestData.SampleAlbum.getAlbum01().orElseThrow(),
                                        TestData.SampleAlbum.getAlbum02().orElseThrow(),
                                        TestData.SampleAlbum.getAlbum03().orElseThrow());
        when(albumService.findByLabel("JMJ")).thenReturn(albums);


        mockMvc.perform(
                        get(endpoint, "JMJ"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(albums)));

        mockMvc.perform(get(endpoint, "Interscope").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        mockMvc.perform(get(endpoint, " ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(albumService, times(2)).findByLabel(any());
    }

    @Test
    void testGetAlbumByArtist() throws Exception {

        String endpoint = "/albums/find/artist/{artist}";

        Artist artist = TestData.SampleArtist.getArtist01().orElseThrow();
        List<Album> albums = Arrays.asList(
                TestData.SampleAlbum.getAlbum01().orElseThrow(),
                TestData.SampleAlbum.getAlbum02().orElseThrow(),
                TestData.SampleAlbum.getAlbum03().orElseThrow());

        when(artistService.findByName("Onyx")).thenReturn(artist);
        when(artistService.findByName("A Tribe Called Quest")).thenThrow(NoSuchElementException.class);
        when(albumService.findByArtist(artist)).thenReturn(albums);


        mockMvc.perform(
                        get(endpoint, "Onyx"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(albums)));

        mockMvc.perform(get(endpoint, "A Tribe Called Quest").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, " ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(albumService, times(1)).findByArtist(artist);

    }

    @Test
    void testAddAlbum() throws Exception {

        String endpoint = "/albums";

        Album album = new Album(null,
                "Hold It Down",
                1995,
                "EastWest Records America",
                TestData.SampleArtist.getArtist04().orElseThrow(),
                Condition.GOOD);
        when(albumService.save(album)).then(invocation -> {
            Album a = invocation.getArgument(0);
            a.setId(11);
            return a;
        });
        Album duplicatedAlbum = TestData.SampleAlbum.getAlbum01().orElseThrow();
        Album badAlbum = new Album(null, "", null, "", null, null);
        when(albumService.save(duplicatedAlbum)).thenThrow(InvalidParameterException.class);


        Album expectedAlbum = new Album(11,
                "Hold It Down",
                1995,
                "EastWest Records America",
                TestData.SampleArtist.getArtist04().orElseThrow(),
                Condition.GOOD);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(album)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAlbum)));

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatedAlbum)))
                .andExpect(status().isConflict());

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badAlbum)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(albumService, times(2)).save(any());
    }

    @Test
    void testRemoveAlbum() throws Exception {

        String endpoint = "/albums/{id}";

        when(albumService.findById(1)).thenReturn(TestData.SampleAlbum.getAlbum01().orElseThrow());
        when(albumService.findById(19)).thenThrow(NoSuchElementException.class);


        mockMvc.perform(delete(endpoint, 1))
                .andExpect(status().isOk());


        ArgumentCaptor<Album> userCaptor = ArgumentCaptor.forClass(Album.class);

        verify(albumService, times(1)).delete(userCaptor.capture());
        assertEquals("Bacdafucup", userCaptor.getValue().getName());
        assertEquals(1, userCaptor.getValue().getId());


        mockMvc.perform(delete(endpoint, 19))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete(endpoint, -1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete(endpoint, "19"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete(endpoint, "asdf"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete(endpoint, " "))
                .andExpect(status().is5xxServerError());
    }
}