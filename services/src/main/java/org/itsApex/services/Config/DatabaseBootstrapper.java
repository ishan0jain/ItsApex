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
			bootstrapProductSequence();
			logTableSchema("dba_product");
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

	private void bootstrapProductSequence() {
		jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS dba_product_product_id_seq");
		jdbcTemplate.execute("ALTER TABLE dba_product ALTER COLUMN product_id SET DEFAULT nextval('dba_product_product_id_seq')");
		jdbcTemplate.execute("SELECT setval('dba_product_product_id_seq', COALESCE((SELECT MAX(product_id) FROM dba_product), 0) + 1, false)");
		log.info("Ensured dba_product.product_id sequence defaults are in place.");
	}

	private void logTableSchema(String tableName) {
		String sql = """
				SELECT column_name, data_type, is_nullable, column_default
				FROM information_schema.columns
				WHERE table_schema = current_schema()
				  AND table_name = ?
				ORDER BY ordinal_position
				""";
		jdbcTemplate.query(sql, rs -> {
			String columnName = rs.getString("column_name");
			String dataType = rs.getString("data_type");
			String nullable = rs.getString("is_nullable");
			String defaultValue = rs.getString("column_default");
			log.info("Schema {}.{}: {} {} nullable={} default={}",
					jdbcTemplate.getDataSource() == null ? "?" : "current_schema",
					tableName,
					columnName,
					dataType,
					nullable,
					defaultValue);
		}, tableName);
	}
}
