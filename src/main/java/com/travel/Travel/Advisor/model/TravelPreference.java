package com.travel.Travel.Advisor.model;

import jakarta.persistence.*;

@Entity
@Table(name="travel_preferences")
public class TravelPreference {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String destinationType;  // e.g. City or Countryside

    private String region;           // e.g. India, Europe

    private String preferences;      // e.g. Budget-Friendly, Luxury, comma separated

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }


    // Getters and setters
}

