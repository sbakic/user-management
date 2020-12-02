package com.sbakic.usermanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "application_user")
public class ApplicationUser implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(min = 2, max = 255)
  @Column(name = "first_name")
  private String firstName;

  @Size(min = 2, max = 255)
  @Column(name = "last_name")
  private String lastName;

  @NotNull
  @Size(min = 2, max = 50)
  @Column(length = 50, unique = true, nullable = false)
  private String email;

  @JsonProperty(access = Access.WRITE_ONLY)
  @NotNull
  @Size(min = 60, max = 60)
  @Column(name = "password_hash", length = 60, nullable = false)
  private String password;

  @Size(min = 2, max = 50)
  @Column(length = 50)
  private String country;

  @Size(min = 2, max = 255)
  private String address;

  @JsonProperty(access = Access.WRITE_ONLY)
  @ManyToMany
  @JoinTable(name = "application_user_authority", joinColumns = {
      @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
      @JoinColumn(name = "authority_name", referencedColumnName = "name")})
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @BatchSize(size = 20)
  private Set<Authority> authorities;

}
