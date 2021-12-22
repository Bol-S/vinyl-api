package com.github.bols.vinylapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bols.vinylapi.TestData;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;
import com.github.bols.vinylapi.service.MusicGroupService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MusicGroupController.class)
class MusicGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MusicGroupService groupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetGroups() throws Exception {

        List<MusicGroup> musicGroups = TestData.SampleMusicGroup.getAllMusicGroups();
        when(groupService.findAll()).thenReturn(musicGroups);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(musicGroups)));

    }

    @Test
    void testGetGroupByName() throws Exception {

        String endpoint = "/groups/find/name/{name}";
        
        MusicGroup musicGroup = TestData.SampleMusicGroup.getMusicGroup01().orElseThrow();
        when(groupService.findByName("Onyx")).thenReturn(musicGroup);
        when(groupService.findByName("A Tribe Called Quest")).thenThrow(NoSuchElementException.class);


        mockMvc.perform(get(endpoint, "Onyx"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(musicGroup)));


        mockMvc.perform(get(endpoint, "A Tribe Called Quest").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, " ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(groupService, times(2)).findByName(any());

    }

    @Test
    void testGetGroupById() throws Exception {
        
        String endpoint = "/groups/find/id/{id}";

        MusicGroup musicGroup = TestData.SampleMusicGroup.getMusicGroup01().orElseThrow();
        when(groupService.findById(1)).thenReturn(musicGroup);
        when(groupService.findById(9)).thenThrow(NoSuchElementException.class);


        mockMvc.perform(
                        get(endpoint, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(musicGroup)));

        mockMvc.perform(
                        get(endpoint, "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(musicGroup)));

        mockMvc.perform(get(endpoint, 9))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, "asdf"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(endpoint, -1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get(endpoint, " "))
                .andExpect(status().isBadRequest());

        verify(groupService, times(3)).findById(any());
    }

    @Test
    void testGetGroupByArtist() throws Exception {

        String endpoint = "/groups/find/artist/{artist}";

        MusicGroup group = TestData.SampleMusicGroup.getMusicGroup02().orElseThrow();
        when(groupService.findByArtist("Krazy Drayz")).thenReturn(group);
        when(groupService.findByArtist("Jerobi")).thenThrow(NoSuchElementException.class);


        mockMvc.perform(
                        get(endpoint, "Krazy Drayz"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(group)));

        mockMvc.perform(get(endpoint, "Jerobi").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(endpoint, " ").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(groupService, times(2)).findByArtist(any());
    }

    @Test
    void testLinkArtistToGroup() throws Exception {

        String endpoint = "/groups/link/{groupId}/{artistId}";

        mockMvc.perform(post(endpoint, 1, 5))
                .andExpect(status().isOk());

        mockMvc.perform(post(endpoint, -1, 2))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(endpoint, 2, -1))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(endpoint, "asdf", 2))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(endpoint, 2, "asdf"))
                .andExpect(status().isBadRequest());


        mockMvc.perform(post(endpoint, " ", 2))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(endpoint, 2, " "))
                .andExpect(status().isBadRequest());

        verify(groupService, times(1)).linkArtistToGroup(any(), any());
    }

    @Test
    void testAddGroup() throws Exception {

        String endpoint = "/groups";

        MusicGroup group = new MusicGroup(null, "A Tribe Called Quest", null);
        when(groupService.save(group)).then(invocation -> {
            MusicGroup g = invocation.getArgument(0);
            g.setId(3);
            return g;
        });
        MusicGroup duplicatedGroup = TestData.SampleMusicGroup.getMusicGroup01().orElseThrow();
        MusicGroup badGroup = new MusicGroup(null, "", null);
        when(groupService.save(duplicatedGroup)).thenThrow(InvalidParameterException.class);


        MusicGroup expectedGroup = new MusicGroup(3, "A Tribe Called Quest", null);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGroup)));

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatedGroup)))
                .andExpect(status().isConflict());

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badGroup)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(groupService, times(2)).save(any());
    }

    @Test
    void testRemoveGroup() throws Exception {

        String endpoint = "/groups/{id}";

        when(groupService.findById(1)).thenReturn(TestData.SampleMusicGroup.getMusicGroup01().orElseThrow());
        when(groupService.findById(9)).thenThrow(NoSuchElementException.class);


        mockMvc.perform(delete(endpoint, 1))
                .andExpect(status().isOk());


        ArgumentCaptor<MusicGroup> userCaptor = ArgumentCaptor.forClass(MusicGroup.class);

        verify(groupService, times(1)).delete(userCaptor.capture());
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