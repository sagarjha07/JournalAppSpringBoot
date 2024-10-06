package com.spring.sagar.journalApp.service;

import com.spring.sagar.journalApp.entity.JournalEntry;
import com.spring.sagar.journalApp.entity.User;
import com.spring.sagar.journalApp.repository.JournalEntryRepository;
import com.spring.sagar.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public User getAuthenticatedUser(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error in getting authenticated user", e);
        }
    }

    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry){
        try {
            User user = getAuthenticatedUser();
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);
            if (user != null) {
                user.getJournalEntryList().add(savedJournalEntry);
                userService.saveUser(user);
            }
            return savedJournalEntry;
        } catch (Exception e) {
            throw new RuntimeException("Error while saving journal entry {}", e);
        }
    }

    public List<JournalEntry> getAllEntries(){
        try {
            User user = getAuthenticatedUser();
            if (user != null) {
                return user.getJournalEntryList();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error while getting all entries for this user", e);
        }
    }

    public void updateEntry(ObjectId id, JournalEntry journalEntry){
        try {
            User user = getAuthenticatedUser();
            Optional<JournalEntry> journalEntryOptional = journalEntryRepository.findById(id);
            if(journalEntryOptional.isPresent()) {
                JournalEntry savedJournalEntry = journalEntryOptional.get();
                savedJournalEntry.setTitle(journalEntry.getTitle() != null ? journalEntry.getTitle() : savedJournalEntry.getTitle());
                savedJournalEntry.setContent(journalEntry.getContent()!=null ? journalEntry.getContent() : savedJournalEntry.getContent());
                savedJournalEntry.setDate(LocalDateTime.now());
                journalEntryRepository.save(savedJournalEntry);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error in updating journal entry", e);
        }
    }

    public void deleteEntry(ObjectId id) {
        try {
            journalEntryRepository.deleteById(id);
            User user = getAuthenticatedUser();
            user.getJournalEntryList().removeIf(x->x.getId().equals(id));
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Error in deleting journal entry", e);
        }
    }

}
