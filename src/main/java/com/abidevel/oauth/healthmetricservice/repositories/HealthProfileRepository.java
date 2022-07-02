package com.abidevel.oauth.healthmetricservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abidevel.oauth.healthmetricservice.model.HealthProfile;

import java.util.Optional;

public interface HealthProfileRepository extends JpaRepository<HealthProfile, Integer> {

  Optional<HealthProfile> findHealthProfileByUsername(String username);
}
