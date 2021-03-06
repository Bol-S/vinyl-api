package com.github.bols.vinylapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bols.vinylapi.TestData;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.service.ArtistService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetArtists() throws Exception {

        List<Artist> artists = TestData.SampleArtist.getAllArtists();
        when(artistService.findAll()).thenReturn(artists);

        mockMvc.perform(get("/artists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(artists)))
                .andExpect(jsonPath("$[0].name").value("Onyx"))
                .andExpect(jsonPath("$[0].realName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[2].name").value("Fredro Starr"))
                .andExpect(jsonPath("$[2].realName").value("Fred Lee Scrugs"));
    }

    @Test
    void testGetArtistByName() throws Exception {

        String endpoint = "/artists/find/name/{name}";
        
        Artist artist = TestData.SampleArtist.getArtist01().orElseThrow();
        when(artistService.findByName("Onyx")).thenReturn(artist);
        when(artistService.findByName("Jerobi")).thenThrow(NoSuchElementException.class);


        mockMvc.perform(
                        get(endpoint, "Onyx"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(artist)));

        mockMvc.perform(get(endpoint, "Jerobi").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, " ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(artistService, times(2)).findByName(any());

    }

    @Test
    void testGetArtistById() throws Exception {

        String endpoint = "/artists/find/id/{id}";
        
        Artist artist = TestData.SampleArtist.getArtist01().orElseThrow();
        when(artistService.findById(1)).thenReturn(artist);
        when(artistService.findById(9)).thenThrow(NoSuchElementException.class);


        mockMvc.perform(
                        get(endpoint, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(artist)));

        mockMvc.perform(
                        get(endpoint, "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(artist)));

        mockMvc.perform(get(endpoint, 9))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, "asdf"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(endpoint, -1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(endpoint, " "))
                .andExpect(status().isBadRequest());

        verify(artistService, times(3)).findById(any());

    }

    @Test
    void testGetArtistByGroup() throws Exception {
        
        String endpoint = "/artists/find/group/{group}";

        List<Artist> artists = Arrays.asList(TestData.SampleArtist.getArtist02().orElseThrow(), TestData.SampleArtist.getArtist03().orElseThrow());
        when(artistService.findByGroup("Onyx")).thenReturn(artists);
        when(artistService.findByGroup("A Tribe Called Quest")).thenThrow(NoSuchElementException.class);


        mockMvc.perform(
                        get(endpoint, "Onyx"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(artists)));

        mockMvc.perform(get(endpoint, "A Tribe Called Quest").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, " ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(artistService, times(2)).findByGroup(any());

    }

    @Test
    void testAddArtist() throws Exception {

        String endpoint = "/artists";

        Artist artist = new Artist(null, "A Tribe Called Quest", null);
        when(artistService.save(artist)).then(invocation -> {
            Artist a = invocation.getArgument(0);
            a.setId(7);
            return a;
        });
        Artist duplicatedArtist = TestData.SampleArtist.getArtist01().orElseThrow();
        Artist badArtist = new Artist(null, "", null);
        when(artistService.save(duplicatedArtist)).thenThrow(InvalidParameterException.class);


        Artist expectedArtist = new Artist(7, "A Tribe Called Quest", null);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedArtist)));

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatedArtist)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badArtist)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(artistService, times(2)).save(any());
    }

    @Test
    void testRemoveArtist() throws Exception {

        String endpoint = "/artists/{id}";

        when(artistService.findById(1)).thenReturn(TestData.SampleArtist.getArtist01().orElseThrow());
        when(artistService.findById(9)).thenThrow(NoSuchElementException.class);


        mockMvc.perform(delete(endpoint, 1))
                .andExpect(status().isOk());


        ArgumentCaptor<Artist> userCaptor = ArgumentCaptor.forClass(Artist.class);

        verify(artistService, times(1)).delete(userCaptor.capture());
        assertEquals("Onyx", userCaptor.getValue().getName());
        assertEquals(1, userCaptor.getValue().getId());


        mockMvc.perform(delete(endpoint, 9))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete(endpoint, -1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete(endpoint, "9"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete(endpoint, "asdf"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete(endpoint, " "))
                .andExpect(status().isBadRequest());
    }
}