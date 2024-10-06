package com.spring.sagar.journalApp.controller;

import com.spring.sagar.journalApp.entity.JournalEntry;
import com.spring.sagar.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping("/create")
    private ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry){
        try {
            journalEntryService.saveEntry(journalEntry);
            return new ResponseEntity<>(journalEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-all")
    private ResponseEntity<List<JournalEntry>> getAllEntries() {
        try {
            List<JournalEntry> journalEntryList = journalEntryService.getAllEntries();
            return new ResponseEntity<>(journalEntryList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/id/{id}")
    private ResponseEntity<?> updateEntry(@PathVariable ObjectId id, @RequestBody JournalEntry journalEntry){
        try {
            journalEntryService.updateEntry(id, journalEntry);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/id/{id}")
    private ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        try {
            journalEntryService.deleteEntry(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
