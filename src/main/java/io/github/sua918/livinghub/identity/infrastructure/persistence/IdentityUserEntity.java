package io.github.sua918.livinghub.identity.infrastructure.persistence;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "identity_users")
public class IdentityUserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String username;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	protected IdentityUserEntity() {
	}

	IdentityUserEntity(String username, String passwordHash) {
		this.username = Objects.requireNonNull(username, "username must not be null");
		this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
	}

	Long getId() {
		return id;
	}

	String getUsername() {
		return username;
	}

}
