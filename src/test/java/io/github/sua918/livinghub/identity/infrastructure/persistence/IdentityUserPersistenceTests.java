package io.github.sua918.livinghub.identity.infrastructure.persistence;

import io.github.sua918.livinghub.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@Transactional
class IdentityUserPersistenceTests {

	private static final String TEST_PASSWORD_HASH = "{test}encoded-password";

	@Autowired
	private IdentityUserRepository repository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void savesAndFindsUserByNormalizedUsername() {
		IdentityUserEntity saved = repository.saveAndFlush(
				new IdentityUserEntity("living_user", TEST_PASSWORD_HASH));

		assertNotNull(saved.getId());
		assertTrue(saved.getId() > 0);
		IdentityUserEntity found = repository.findByUsername("living_user").orElseThrow();
		assertEquals(saved.getId(), found.getId());
		assertEquals("living_user", found.getUsername());
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"ab",
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
			"1user",
			"User",
			"_user",
			"user-name",
			"user name"
	})
	void rejectsUsernameOutsideApprovedFormat(String username) {
		assertThrows(DataIntegrityViolationException.class, () ->
				jdbcTemplate.update(
						"INSERT INTO identity_users (username, password_hash) VALUES (?, ?)",
						username,
						TEST_PASSWORD_HASH));
	}

	@Test
	void rejectsDuplicateUsername() {
		jdbcTemplate.update(
				"INSERT INTO identity_users (username, password_hash) VALUES (?, ?)",
				"duplicate_user",
				TEST_PASSWORD_HASH);

		assertThrows(DataIntegrityViolationException.class, () ->
				jdbcTemplate.update(
						"INSERT INTO identity_users (username, password_hash) VALUES (?, ?)",
						"duplicate_user",
						TEST_PASSWORD_HASH));
	}

	@Test
	void rejectsNullUsername() {
		assertThrows(DataIntegrityViolationException.class, () ->
				jdbcTemplate.update(
						"INSERT INTO identity_users (username, password_hash) VALUES (?, ?)",
						null,
						TEST_PASSWORD_HASH));
	}

	@Test
	void rejectsNullPasswordHash() {
		assertThrows(DataIntegrityViolationException.class, () ->
				jdbcTemplate.update(
						"INSERT INTO identity_users (username, password_hash) VALUES (?, ?)",
						"required_hash_user",
						null));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "   " })
	void rejectsBlankPasswordHash(String passwordHash) {
		assertThrows(DataIntegrityViolationException.class, () ->
				jdbcTemplate.update(
						"INSERT INTO identity_users (username, password_hash) VALUES (?, ?)",
						"blank_hash_user",
						passwordHash));
	}

}
