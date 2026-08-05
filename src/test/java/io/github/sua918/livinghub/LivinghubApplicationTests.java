package io.github.sua918.livinghub;

import java.sql.Connection;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class LivinghubApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private Flyway flyway;

	@Autowired
	private PostgreSQLContainer postgresContainer;

	@Autowired
	private Environment environment;

	@Test
	void contextLoads() throws Exception {
		assertArrayEquals(new String[] { "test" }, environment.getActiveProfiles());
		assertEquals("true", environment.getProperty("spring.flyway.enabled"));
		assertEquals("validate", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
		assertTrue(entityManagerFactory.isOpen());
		assertNotNull(flyway);
		assertEquals(0, flyway.info().applied().length);

		try (Connection connection = dataSource.getConnection()) {
			assertEquals("PostgreSQL", connection.getMetaData().getDatabaseProductName());
			assertEquals(postgresContainer.getJdbcUrl(), connection.getMetaData().getURL());
		}
	}

}
