package com.yash.journalApp.service;

import com.yash.journalApp.entity.JournalEntry;
import com.yash.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    private static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    public void saveEntry(JournalEntry journalEntry){
        try{
            journalEntry.setDate(LocalDateTime.now());
            journalEntryRepository.save(journalEntry);
        } catch (Exception e){
            log.error("Exception" + e);
        }

    }
    public List<JournalEntry> getAllEntries() {

        return journalEntryRepository.findAll();
    }
    public Optional<JournalEntry> findById(ObjectId id){

        return journalEntryRepository.findById(id);
    }
    public void deleteById(ObjectId id){
        journalEntryRepository.deleteById(id);
    }

}
