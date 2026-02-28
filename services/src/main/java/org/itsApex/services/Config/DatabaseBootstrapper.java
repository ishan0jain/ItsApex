package org.itsApex.services.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseBootstrapper implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(DatabaseBootstrapper.class);
	private final JdbcTemplate jdbcTemplate;

	public DatabaseBootstrapper(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void run(String... args) {
		try {
			bootstrapShopSequence();
		} catch (Exception e) {
			log.warn("Database bootstrap skipped: {}", e.getMessage());
		}
	}

	private void bootstrapShopSequence() {
		jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS dba_shop_shop_id_seq");
		jdbcTemplate.execute("ALTER TABLE dba_shop ALTER COLUMN shop_id SET DEFAULT nextval('dba_shop_shop_id_seq')");
		jdbcTemplate.execute("SELECT setval('dba_shop_shop_id_seq', COALESCE((SELECT MAX(shop_id) FROM dba_shop), 0) + 1, false)");
		log.info("Ensured dba_shop.shop_id sequence defaults are in place.");
	}
}
