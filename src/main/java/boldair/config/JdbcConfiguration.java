package boldair.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories( basePackages = "boldair.dao" )
public class JdbcConfiguration {
	// Configuration simplifiée - utilise le search_path par défaut de PostgreSQL
}
