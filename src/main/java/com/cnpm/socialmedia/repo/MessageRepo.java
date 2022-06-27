package com.cnpm.socialmedia.repo;

import com.cnpm.socialmedia.model.Message;
import com.cnpm.socialmedia.model.Users;
import org.hibernate.sql.Select;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findBySender_IdAndReceiver_IdOrderByCreateTimeDesc(Long senderId, Long receiverId, Pageable pageable);
    List<Message> findAllByRoomOrderByCreateTimeDesc(String room, Pageable pageable);
    @Query("select distinct m.sender,m.receiver from Message m where m.sender.id = :id or m.receiver.id = :id order by m.id desc")
    List<Users> findUserChat(Long id, Pageable pageable);
}
