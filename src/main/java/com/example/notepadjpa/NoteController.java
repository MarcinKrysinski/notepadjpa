package com.example.notepadjpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/note")
public class NoteController {
    NoteRepository noteRepository;

    @Autowired
    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    ResponseEntity<List<Note>> getAllNotes(){
       return new ResponseEntity<>(noteRepository.findAll(), HttpStatus.OK);
    }



    @GetMapping("/{Id}")
    ResponseEntity<Note> getById(@PathVariable long Id){
        if(noteRepository.existsById(Id))
        return new ResponseEntity<>( noteRepository.findById(Id).get(),HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    ResponseEntity addNote(@RequestBody Note note){
        if( noteRepository.save(note) != null)
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/{Id}")
    ResponseEntity update(@PathVariable long Id, @RequestBody Note note){
        if(noteRepository.existsById(Id)) {
            noteRepository.save(note);
            return new ResponseEntity(HttpStatus.OK);
        }else
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);

    }

    @DeleteMapping("/{Id}")
    ResponseEntity delete(@PathVariable long Id){
        if(noteRepository.existsById(Id)) {
            noteRepository.deleteById(Id);
            return new ResponseEntity(HttpStatus.OK);
        }else
            return new ResponseEntity(HttpStatus.NOT_FOUND);

    }
    @GetMapping("/islive")
    boolean isLive(){
        return true; // frontend będzie czekał aż backend wstanie na serwerze
    }
}
