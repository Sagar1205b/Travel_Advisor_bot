package com.travel.Travel.Advisor.repository;



import com.travel.Travel.Advisor.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserId(Long userId);
    List<Conversation> findByUserIdOrderByCreatedAtDesc(Long userId);

}

