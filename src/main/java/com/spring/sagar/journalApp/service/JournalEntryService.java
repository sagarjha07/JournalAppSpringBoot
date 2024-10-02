package com.spring.sagar.journalApp.service;

import com.spring.sagar.journalApp.entity.JournalEntry;
import com.spring.sagar.journalApp.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class JournalEntryService {

    @Autowired
    JournalEntryRepository journalEntryRepository;

    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry){
        try {
            journalEntry.setDate(LocalDateTime.now());
            journalEntry.setTitle(null);
            return journalEntryRepository.save(journalEntry);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving journal entry");
        }
    }

}
