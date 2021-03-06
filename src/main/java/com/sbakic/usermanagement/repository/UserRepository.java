package com.sbakic.usermanagement.repository;

import com.sbakic.usermanagement.domain.ApplicationUser;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long>,
    JpaSpecificationExecutor<ApplicationUser> {

  @EntityGraph(attributePaths = "authorities")
  Optional<ApplicationUser> findUserByEmail(String email);

  @EntityGraph(attributePaths = "authorities")
  Optional<ApplicationUser> findOneWithAuthoritiesByEmail(String email);

  @EntityGraph(attributePaths = "authorities")
  @Nonnull
  List<ApplicationUser> findAll(Specification<ApplicationUser> user);

}
