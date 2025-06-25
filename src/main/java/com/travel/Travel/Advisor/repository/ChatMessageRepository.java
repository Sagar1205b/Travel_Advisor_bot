package com.travel.Travel.Advisor.repository;

import com.travel.Travel.Advisor.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    @Query("SELECT s FROM ChatMessage s WHERE s.user.username = :username ORDER BY s.id ASC")
    List<ChatMessage> findByUser(@Param("username") String username);
    List<ChatMessage> findByConversationIdOrderByTimestampAsc(Long conversationId);



}
