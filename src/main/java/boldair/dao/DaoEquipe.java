package boldair.dao;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import boldair.data.Equipe;

public interface DaoEquipe extends CrudRepository<Equipe, Long> {

	Equipe findByNomEquipe( String nomEquipe );
	
	@Query( "SELECT COUNT(*) FROM Equipe" )
	long countAllTeams();
	
	@Query( "SELECT * FROM Equipe ORDER BY nom_equipe ASC" )
	List<Equipe> findAllOrderByName();

}
