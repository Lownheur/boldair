package boldair.dao;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import boldair.data.Benevol;

public interface DaoBenevol extends CrudRepository<Benevol, Long>, PagingAndSortingRepository<Benevol, Long> {

	Benevol findByEmail( String email );

	@Query( "SELECT b.* FROM Benevol b JOIN compte_benevol cb ON b.Id_Benevol = cb.id_benevol WHERE cb.id_compte = :idCompte" )
	Benevol findByCompteId( Long idCompte );
}
