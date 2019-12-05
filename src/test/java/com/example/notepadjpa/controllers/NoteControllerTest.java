package com.example.notepadjpa.controllers;

import com.example.notepadjpa.models.Note;
import com.example.notepadjpa.repositories.NoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NoteRepository noteRepository;

    @Test
    void getAllNotes() throws Exception {
        //given
        given(noteRepository.findAll()).willReturn(getTestList());

        //then
        mockMvc.perform(get("/note"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[4].name").value("Nazwa5"));
    }

    @Test
    void getById() throws Exception {
        //given
        long id;
        given(noteRepository.existsById(3L)).willReturn(true);
        given(noteRepository.existsById(7L)).willReturn(false);
        given(noteRepository.findById(3L)).willReturn(Optional.of(getTestList().get(2)));


        //then
        mockMvc.perform(get("/note/{Id}", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.name").value("Nazwa3"));

        mockMvc.perform(get("/note/{Id}", 7))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addNote() throws Exception {
        //given
        Note note = new Note(6L, "Nazwa6", "Tresc6", "Ip6");
        String noteJson = mapper.writeValueAsString(note);
        given(noteRepository.save(any(Note.class))).willReturn(note);


        //then
        mockMvc.perform(post("/note").
                contentType(MediaType.APPLICATION_JSON).
                content(noteJson))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        //given
        String noteJson = mapper.writeValueAsString(new Note(6L, "Nazwa6", "Tresc6", "Ip6"));
        given(noteRepository.existsById(6L)).willReturn(true);
        given(noteRepository.existsById(4L)).willReturn(false);
        given(noteRepository.save(any(Note.class))).willReturn(new Note());


        //then
        mockMvc.perform(put("/note/{Id}", 6).
                contentType(MediaType.APPLICATION_JSON).
                content(noteJson))
                .andExpect(status().isOk());

        mockMvc.perform(put("/note/{Id}", 4).
                contentType(MediaType.APPLICATION_JSON).
                content(noteJson))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void deleteTest() throws Exception {

        //given
        given(noteRepository.existsById(11L)).willReturn(true);
        given(noteRepository.existsById(12L)).willReturn(false);


        //then
        mockMvc.perform(delete("/note/{id}", 11L))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/note/{id}", 12L))
                .andExpect(status().isNotFound());
    }

    @Test
    void isLive() throws Exception {
        mockMvc.perform(get("/note/islive"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    List<Note> getTestList() {
        List<Note> list = new ArrayList<>();
        list.add(new Note(1L, "Nazwa1", "Tresc1", "Ip1"));
        list.add(new Note(2L, "Nazwa2", "Tresc2", "Ip2"));
        list.add(new Note(3L, "Nazwa3", "Tresc3", "Ip3"));
        list.add(new Note(4L, "Nazwa4", "Tresc4", "Ip4"));
        list.add(new Note(5L, "Nazwa5", "Tresc5", "Ip5"));
        return list;
    }
}
