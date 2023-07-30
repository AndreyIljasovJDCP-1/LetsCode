package com.example.letscode.repositories;

import com.example.letscode.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByTagContains(String tag);
}
