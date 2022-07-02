package com.abidevel.oauth.healthmetricservice.services;


import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abidevel.oauth.healthmetricservice.exception.HealthProfileAlreadyExistsException;
import com.abidevel.oauth.healthmetricservice.exception.NonExistentHealthProfileException;
import com.abidevel.oauth.healthmetricservice.model.HealthProfile;
import com.abidevel.oauth.healthmetricservice.repositories.HealthProfileRepository;

import java.util.Optional;

@Service
@Transactional
public class HealthProfileService {

  private final HealthProfileRepository healthProfileRepository;

  public HealthProfileService(HealthProfileRepository healthProfileRepository) {
    this.healthProfileRepository = healthProfileRepository;
  }

  // If  the profile is the current logged user.
  @PreAuthorize("#profile.username == authentication.principal.claims['user_name']")
  public void addHealthProfile(HealthProfile profile) {
    Optional<HealthProfile> healthProfile = healthProfileRepository.findHealthProfileByUsername(profile.getUsername());

    if (healthProfile.isEmpty()) {
      healthProfileRepository.save(profile);
    } else {
      throw new HealthProfileAlreadyExistsException("This health profile already exists.");
    }
  }

  // if the username is the logged user or has admin authority.
  @PostAuthorize("returnObject.username == authentication.principal.claims['user_name'] or hasAuthority('admin')")
  public HealthProfile findHealthProfile(String username) {
    Optional<HealthProfile> healthProfile =
            healthProfileRepository.findHealthProfileByUsername(username);

    return healthProfile
            .orElseThrow(() -> new NonExistentHealthProfileException("No profile found for the provided username."));
  }

  @PreAuthorize("hasAuthority('admin')")
  public void deleteHealthProfile(String username) {
    Optional<HealthProfile> healthProfile =
            healthProfileRepository.findHealthProfileByUsername(username);

    healthProfile.ifPresentOrElse(
            p -> healthProfileRepository.delete(p),
            () -> {
              throw new NonExistentHealthProfileException("No profile found for the provided username.");
            });
  }
}
