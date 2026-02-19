package com.yash.journalApp.controller;

import com.yash.journalApp.entity.JournalEntry;
import com.yash.journalApp.entity.User;
import com.yash.journalApp.service.JournalEntryService;
import com.yash.journalApp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name = "Journal APIs")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // ---------------- Get Journals of Current Authenticated User ----------------
    @GetMapping("/me")
    @Operation(summary = "Get Journals of Current Authenticated User")
    public ResponseEntity<?> getAllJournalEntriesOfMe() {
        String currentUsername = getAuthenticatedUsername();
        return getJournalEntriesByUsername(currentUsername);
    }

    // ---------------- Get Journals by Username (Optional) ----------------
    @GetMapping
    @Operation(summary = "Get all journal entries of a user" )
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
    @Operation(summary = "Create Journal for Current User")
    public ResponseEntity<JournalEntry> createEntryForMe(@RequestBody JournalEntry myEntry) {
        String currentUsername = getAuthenticatedUsername();
        journalEntryService.saveEntry(myEntry, currentUsername);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
    }

    // ---------------- Create Journal for Specific User (Optional) ----------------
    @PostMapping
    @Operation(summary = "Create Journal for Specific User (Optional)")
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
    @Operation(summary = "Get journal entries by specific by ID" )
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId) {
        ObjectId objectId = new ObjectId(myId);
        Optional<JournalEntry> journalEntry = journalEntryService.findById(objectId);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/id/{myId}")
    @Operation(summary = "delete journal entries by specific by ID" )
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
    @Operation(summary = "Update journal entries by specific by ID" )
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if(journalEntry.isPresent()){
                JournalEntry old = journalEntry.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
