package boldair.db;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import boldair.StartWebServer;

@SpringBootTest( classes = { StartWebServer.class } )
@TestMethodOrder( MethodOrderer.MethodName.class )
public class Init_DB {

	@Test
	@Sql( { "/db/1a-schema.sql", "/db/1b-tables.sql" } )
	void db_1_Tables() {
	}

	@Test
	@Sql( value = "/db/2-procedures.sql", config = @SqlConfig( separator = "\n\n" ) )
	void db_2_Procedures() {
	}

	@Test
	@Sql( {
			"/db/3_delete.sql",
			"/db/4-epreuve-data.sql", // Moved up to ensure epreuve data exists
			"/db/5-roles.sql",        // Moved up as good practice
			"/db/3-compte.sql",       // Now runs after epreuve and roles data is populated
	} )
	void db_3_Data() {
	}

}
