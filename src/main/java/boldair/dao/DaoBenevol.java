package boldair.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jdbc.repository.query.Modifying;

import boldair.data.Benevol;
import java.util.List;

public interface DaoBenevol extends CrudRepository<Benevol, Long>, PagingAndSortingRepository<Benevol, Long> {

	Benevol findByEmail( String email );

	@Query( "SELECT * FROM boldair.Benevol WHERE id_compte = :idCompte" )
	Benevol findByCompteId( @Param("idCompte") Long idCompte );
	
	@Query( "SELECT COUNT(*) FROM boldair.Benevol" )
	long countAllVolunteers();

	// Nouvelles méthodes pour la gestion des rôles
	@Query( "SELECT * FROM boldair.Benevol WHERE id_role = :idRole" )
	List<Benevol> findByRoleId( @Param("idRole") Long idRole );

	@Query( "SELECT COUNT(*) FROM boldair.Benevol WHERE id_role = :idRole" )
	long countByRoleId( @Param("idRole") Long idRole );

	@Modifying
	@Query( "UPDATE boldair.Benevol SET id_role = :idRole WHERE id_benevol = :idBenevol" )
	void assignRole( @Param("idBenevol") Long idBenevol, @Param("idRole") Long idRole );

	@Modifying
	@Query( "UPDATE boldair.Benevol SET id_role = NULL WHERE id_benevol = :idBenevol" )
	void removeRole( @Param("idBenevol") Long idBenevol );

	@Query( "SELECT * FROM boldair.Benevol WHERE id_role IS NULL" )
	List<Benevol> findBenevolsWithoutRole();
}
