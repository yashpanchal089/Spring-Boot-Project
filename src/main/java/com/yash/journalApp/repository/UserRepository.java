package com.yash.journalApp.repository;

import com.yash.journalApp.entity.JournalEntry;
import com.yash.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);

}
