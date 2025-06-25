package com.travel.Travel.Advisor.model;



import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(unique=true, nullable=false)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    // Bidirectional mapping of chat messages
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<ChatMessage> chatMessages = new ArrayList<>();

    // Travel preferences
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<TravelPreference> preferences = new ArrayList<>();

    // Constructors, getters, setters omitted for brevity

    // Add utility methods to add messages/preferences
    public void addChatMessage(ChatMessage msg) {
        chatMessages.add(msg);
        msg.setUser(this);
    }

    public void addTravelPreference(TravelPreference pref) {
        preferences.add(pref);
        pref.setUser(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public List<TravelPreference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<TravelPreference> preferences) {
        this.preferences = preferences;
    }

    // Getters and setters ...
}
