package com.example.notepadjpa.controllers;

import com.example.notepadjpa.models.Note;
import com.example.notepadjpa.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/note")
public class NoteController {
    private NoteRepository noteRepository;

    @Autowired
    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        return new ResponseEntity<>(noteRepository.findAll(), HttpStatus.OK);
    }


    @GetMapping("/{Id}")
    public ResponseEntity<Note> getById(@PathVariable long Id) {

        if (noteRepository.existsById(Id)) {
            Optional<Note> temp = noteRepository.findById(Id);
            if (temp.isPresent()) {
                return new ResponseEntity<>(temp.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping()
    public ResponseEntity addNote(@RequestBody Note note) {
        if (noteRepository.save(note) != null) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{Id}")
    public ResponseEntity update(@PathVariable long Id, @RequestBody Note note) {
        if (noteRepository.existsById(Id) && noteRepository.save(note) != null) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity delete(@PathVariable long Id) {
        if (noteRepository.existsById(Id)) {
            noteRepository.deleteById(Id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/islive")
    public boolean isLive() {
        return true;
        // frontend będzie czekał aż backend wstanie na serwerze
    }
}
