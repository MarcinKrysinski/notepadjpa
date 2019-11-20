package com.example.notepadjpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if (noteRepository.existsById(Id))
            return new ResponseEntity<>(noteRepository.findById(Id).get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity addNote(@RequestBody Note note) {
        if (noteRepository.save(note) != null)
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/{Id}")
    public ResponseEntity update(@PathVariable long Id, @RequestBody Note note) {
        if (noteRepository.existsById(Id)) {
            noteRepository.save(note);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

    }

    @DeleteMapping("/{Id}")
    public ResponseEntity delete(@PathVariable long Id) {
        if (noteRepository.existsById(Id)) {
            noteRepository.deleteById(Id);
            return new ResponseEntity(HttpStatus.OK);
        } else
            return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    @GetMapping("/islive")
    public boolean isLive() {
        return true;
        // frontend będzie czekał aż backend wstanie na serwerze
    }
}
