package com.travel.Travel.Advisor.controller;

import com.travel.Travel.Advisor.model.ChatMessage;
import com.travel.Travel.Advisor.model.Conversation;
import com.travel.Travel.Advisor.model.User;
import com.travel.Travel.Advisor.repository.ChatMessageRepository;
import com.travel.Travel.Advisor.repository.ConversationRepository;
import com.travel.Travel.Advisor.service.ChatService;
import com.travel.Travel.Advisor.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/travel")
public class ChatController {

    private final ChatService chatService;
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final ChatMessageRepository chatMessageRepository;

    public ChatController(ChatService chatService, ConversationRepository conversationRepository, UserService userService, ChatMessageRepository chatMessageRepository) {
        this.chatService = chatService;
        this.conversationRepository = conversationRepository;
        this.userService = userService;
        this.chatMessageRepository = chatMessageRepository;
    }


    @GetMapping("/chat")
    public String newChat(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName()).orElseThrow();

        Conversation newConversation = chatService.startNewConversation(user);
        List<Conversation> history = chatService.getUserConversations(user.getId());

        model.addAttribute("conversationId", newConversation.getId());
        model.addAttribute("chatMessages", List.of());
        model.addAttribute("conversations", history);
        return "chatbot";
    }
//    @GetMapping("/chat")
//    public String chatPage(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//
//        User user = userService.findByUsername(username).orElseThrow();
//        List<ChatMessage> chatHistory = chatMessageRepository.findByUser(username);
//
//        model.addAttribute("chatMessages", chatHistory);
//        model.addAttribute("username", username);
//        return "chat";
//    }

    /*
    @PostMapping("/chat/bot")
    public String sendMessage(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String message,
                              @RequestParam String destinationType,
                              @RequestParam String region,
                              @RequestParam String preference,
                              Model model) {

        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        String fullMessage = String.format("Destination type: %s, Region: %s, Preference: %s. %s",
                destinationType, region, preference, message);

        String botResponse = chatService.getBotResponse(user, fullMessage);
        chatService.saveChat(user, fullMessage, botResponse);

        List<ChatMessage> chatHistory = chatMessageRepository.findByUser(user.getUsername());
        model.addAttribute("chatMessages", chatHistory);
        model.addAttribute("username", user.getUsername());

        return "chat";
    }
*/



    @GetMapping("/chat/{conversationId}")
    public String resumeChat(@PathVariable Long conversationId, Model model) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow();
        List<ChatMessage> chatMessages = chatService.getChatMessages(conversationId);
        List<Conversation> history = chatService.getUserConversations(conversation.getUser().getId());

        model.addAttribute("conversationId", conversationId);
        model.addAttribute("chatMessages", chatMessages);
        model.addAttribute("conversations", history);
        return "chatbot";
    }

    @PostMapping("/chat/bot")
    public String sendMessage(@RequestParam Long conversationId,
                              @RequestParam String message,
                              @RequestParam String destinationType,
                              @RequestParam String region,
                              @RequestParam String preference,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow();

        String fullMessage = String.format("Destination type: %s, Region: %s, Preference: %s. %s",
                destinationType, region, preference, message);

        String botResponse = chatService.getBotResponse(user, fullMessage);
        chatService.saveChat(conversation, user, fullMessage, botResponse);

        List<ChatMessage> chatHistory = chatService.getChatMessages(conversationId);
        List<Conversation> conversations = chatService.getUserConversations(user.getId());

        model.addAttribute("conversationId", conversationId);
        model.addAttribute("chatMessages", chatHistory);
        model.addAttribute("conversations", conversations);
        model.addAttribute("username", user.getUsername());

        return "chatbot";
    }


}
