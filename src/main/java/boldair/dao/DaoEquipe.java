package boldair.dao;

import org.springframework.data.repository.CrudRepository;

import boldair.data.Equipe;

public interface DaoEquipe extends CrudRepository<Equipe, Long> {

	Equipe findByNomEquipe( String nomEquipe );

}
