package com.abidevel.oauth.healthmetricservice.model;


import javax.persistence.*;

import com.abidevel.oauth.healthmetricservice.model.enumeration.HealthMetricType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "health_metric")
public class HealthMetric {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @ToString.Include
  private int id;

  @Basic
  @ToString.Include
  private double value;

  @Enumerated(EnumType.STRING)
  @ToString.Include
  private HealthMetricType type;

  @ManyToOne
  private HealthProfile profile;

}
