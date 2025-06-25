package com.travel.Travel.Advisor.service;

import com.travel.Travel.Advisor.model.ChatMessage;
import com.travel.Travel.Advisor.model.Conversation;
import com.travel.Travel.Advisor.model.User;
import com.travel.Travel.Advisor.repository.ChatMessageRepository;
import com.travel.Travel.Advisor.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;

    @Value("${chat.api.url}")
    private String chatApiUrl;

    @Value("${chat.api.key}")
    private String chatApiKey;

    public ChatService(ChatMessageRepository chatMessageRepository, ConversationRepository conversationRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.conversationRepository = conversationRepository;
    }

    public Conversation startNewConversation(User user){
        Conversation conversation=new Conversation();
        conversation.setUser(user);
        return conversationRepository.save(conversation);
    }

    public String getBotResponse(User user, String userMessage) {
        // Prepare payload for external API call (example using Groq API format)
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "llama3-8b-8192");
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a helpful travel advisor."));
        messages.add(Map.of("role", "user", "content", userMessage));
        payload.put("messages", messages);
        payload.put("temperature", 0.7);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(chatApiKey);
        HttpEntity<Map<String,Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(chatApiUrl, request, Map.class);
            if(response.getStatusCode().is2xxSuccessful()) {
                // Extract the bot message from response JSON structure
                Map<String, Object> body = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String,Object>>) body.get("choices");
                if(choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String botMsg = (String) message.get("content");
                    return botMsg;
                }
            }
        } catch (Exception e) {
            return "Error calling chat API: " + e.getMessage();
        }
        return "Sorry, I couldn't get a response.";
    }

//    public ChatMessage saveChat(User user, String userMsg, String botResponse) {
//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setUser(user);
//        chatMessage.setUserMessage(userMsg);
//        chatMessage.setBotResponse(botResponse);
//        return chatMessageRepository.save(chatMessage);
//    }

    public void saveChat(Conversation conversation, User user, String userMsg, String botResponse) {
        ChatMessage message = new ChatMessage();
        message.setUser(user);
        message.setConversation(conversation);
        message.setUserMessage(userMsg);
        message.setBotResponse(botResponse);
        chatMessageRepository.save(message);

        if (conversation.getTitle() == null) {
            conversation.setTitle(userMsg.length() > 50 ? userMsg.substring(0, 50) + "..." : userMsg);
            conversationRepository.save(conversation);
        }
    }
//
//    public List<ChatMessage> getChat(String user)
//    {
//        return chatMessageRepository.findByUser(user);
//    }
public List<ChatMessage> getChatMessages(Long conversationId) {
    return chatMessageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
}

    public List<Conversation> getUserConversations(Long userId) {
        return conversationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}

