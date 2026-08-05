package io.github.sua918.livinghub.identity.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityUserRepository extends JpaRepository<IdentityUserEntity, Long> {

	Optional<IdentityUserEntity> findByUsername(String username);

}
