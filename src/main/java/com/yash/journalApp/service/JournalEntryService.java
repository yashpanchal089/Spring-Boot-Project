package com.yash.journalApp.service;

import com.yash.journalApp.entity.JournalEntry;
import com.yash.journalApp.entity.User;
import com.yash.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    public void saveEntry(JournalEntry journalEntry, String userName){

        try{
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw  new RuntimeException("An Error occurred while using the entry !" + e);
        }


    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getAllEntries() {

        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findById(ObjectId id){

        return journalEntryRepository.findById(id);
    }
    public void deleteById(ObjectId id, String userName){
        User user = userService.findByUserName(userName);
        user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }

}
