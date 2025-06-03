package boldair.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import boldair.data.Benevol;

public interface DaoBenevol extends CrudRepository<Benevol, Long>, PagingAndSortingRepository<Benevol, Long> {

	Benevol findByEmail( String email );

	@Query( "SELECT * FROM Benevol WHERE id_compte = :idCompte" )
	Benevol findByCompteId( @Param("idCompte") Long idCompte );
	
	@Query( "SELECT COUNT(*) FROM Benevol" )
	long countAllVolunteers();
}
