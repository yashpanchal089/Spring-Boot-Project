package com.yash.journalApp.controller;

import com.yash.journalApp.entity.JournalEntry;
import com.yash.journalApp.entity.User;
import com.yash.journalApp.service.JournalEntryService;
import com.yash.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // ---------------- Get Journals of Current Authenticated User ----------------
    @GetMapping("/me")
    public ResponseEntity<?> getAllJournalEntriesOfMe() {
        String currentUsername = getAuthenticatedUsername();
        return getJournalEntriesByUsername(currentUsername);
    }

    // ---------------- Get Journals by Username (Optional) ----------------
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String currentUsername = getAuthenticatedUsername();
        if (!currentUsername.equals(username)) {
            return new ResponseEntity<>("Forbidden: Cannot access other user's journals", HttpStatus.FORBIDDEN);
        }
        return getJournalEntriesByUsername(username);
    }

    // ---------------- Create Journal for Current User ----------------
    @PostMapping("/me")
    public ResponseEntity<JournalEntry> createEntryForMe(@RequestBody JournalEntry myEntry) {
        String currentUsername = getAuthenticatedUsername();
        journalEntryService.saveEntry(myEntry, currentUsername);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
    }

    // ---------------- Create Journal for Specific User (Optional) ----------------
    @PostMapping
    public ResponseEntity<?> createEntryForUser(@RequestBody JournalEntry myEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String currentUsername = getAuthenticatedUsername();
        if (!currentUsername.equals(username)) {
            return new ResponseEntity<>("Forbidden: Cannot create journal for another user", HttpStatus.FORBIDDEN);
        }
        journalEntryService.saveEntry(myEntry, username);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
    }

    // ---------------- Helper Methods ----------------
    private ResponseEntity<?> getJournalEntriesByUsername(String username) {
        User user = userService.findByUserName(username);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        List<JournalEntry> all = user.getJournalEntries();
        if (all == null || all.isEmpty()) {
            return new ResponseEntity<>("No journal entries found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // ---------------- Other endpoints for update/delete by ID ----------------
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryService.deleteById(myId, username);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId,
                                               @RequestBody JournalEntry newEntry) {
        String currentUsername = getAuthenticatedUsername();
        JournalEntry old = journalEntryService.findById(myId).orElse(null);
        if (old != null) {
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old, currentUsername);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
