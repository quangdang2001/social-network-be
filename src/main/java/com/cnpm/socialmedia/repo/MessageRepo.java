package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findBySender_IdAndReceiver_IdOrderByCreateTimeDesc(Long senderId, Long receiverId, Pageable pageable);
    List<Message> findAllByRoomOrderByCreateTimeDesc(String room, Pageable pageable);
}
