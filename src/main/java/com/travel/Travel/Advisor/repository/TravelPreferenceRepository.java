package com.travel.Travel.Advisor.repository;

import com.travel.Travel.Advisor.model.TravelPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPreferenceRepository extends JpaRepository<TravelPreference,Long> {
}
