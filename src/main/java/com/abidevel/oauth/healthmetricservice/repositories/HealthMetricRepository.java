package com.abidevel.oauth.healthmetricservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.abidevel.oauth.healthmetricservice.model.HealthMetric;
import com.abidevel.oauth.healthmetricservice.model.HealthProfile;

import java.util.List;

public interface HealthMetricRepository extends JpaRepository<HealthMetric, Integer> {

  @Query("SELECT h FROM HealthMetric h WHERE h.profile.username=?#{authentication.principal.claims['user_name']}")
  List<HealthMetric> findHealthMetricHistory(String username);

  @Query("DELETE FROM HealthMetric h WHERE h.profile=:profile")
  @Modifying
  void deleteAllForUser(HealthProfile profile);
}
