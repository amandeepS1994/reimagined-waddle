package com.abidevel.oauth.healthmetricservice.services;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abidevel.oauth.healthmetricservice.exception.NonExistentHealthProfileException;
import com.abidevel.oauth.healthmetricservice.model.HealthMetric;
import com.abidevel.oauth.healthmetricservice.model.HealthProfile;
import com.abidevel.oauth.healthmetricservice.repositories.HealthMetricRepository;
import com.abidevel.oauth.healthmetricservice.repositories.HealthProfileRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HealthMetricService {

  private final HealthMetricRepository healthMetricRepository;
  private final HealthProfileRepository healthProfileRepository;

  public HealthMetricService(HealthMetricRepository healthMetricRepository, HealthProfileRepository healthProfileRepository) {
    this.healthMetricRepository = healthMetricRepository;
    this.healthProfileRepository = healthProfileRepository;
  }

  // If is current user.
  @PreAuthorize("#healthMetric.profile.username == authentication.principal.claims['user_name']")
  public void addHealthMetric(HealthMetric healthMetric) {
    Optional<HealthProfile> profile = healthProfileRepository.findHealthProfileByUsername(healthMetric.getProfile().getUsername());

    profile.ifPresentOrElse(
            p ->
            {
              healthMetric.setProfile(p);
              healthMetricRepository.save(healthMetric);
            },
            () -> {
              throw new NonExistentHealthProfileException("The profile doesn't exist");
            });

    ;
  }

  public List<HealthMetric> findHealthMetricHistory(String username) {
    return healthMetricRepository.findHealthMetricHistory(username);
  }

  @PreAuthorize("hasAuthority('admin')")
  public void deleteHealthMetricForUser(String username) {
    Optional<HealthProfile> profile = healthProfileRepository.findHealthProfileByUsername(username);

    profile.ifPresentOrElse(healthMetricRepository::deleteAllForUser,
            () -> {
              throw new NonExistentHealthProfileException("The profile doesn't exist");
            }
    );
  }
}
