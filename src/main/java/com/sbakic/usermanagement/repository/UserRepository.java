package com.sbakic.usermanagement.repository;

import com.sbakic.usermanagement.domain.ApplicationUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

  @EntityGraph(attributePaths = "authorities")
  Optional<ApplicationUser> findUserByEmail(String email);

}
