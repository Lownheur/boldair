package boldair.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import boldair.data.Compte;

public interface DaoCompte extends CrudRepository<Compte, Long>, PagingAndSortingRepository<Compte, Long> {

	Compte findByPseudo( String pseudo );

	Compte findByEmail( String email );

	Page<Compte> findAllByOrderByPseudo( Pageable pageable );

	Page<Compte> findByPseudoContainingIgnoreCase( String search, Pageable pageable );

	Page<Compte> findByPseudoContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByPseudo( String search,
			String search2, Pageable pageable );
	
	@Query("SELECT COUNT(*)=0 FROM compte WHERE pseudo = :pseudo AND id_compte <> COALESCE(:id, 0)")
	boolean verifierUnicitePseudo( String pseudo, Long id );

	@Query("SELECT COUNT(*)=0 FROM compte WHERE email = :email AND id_compte <> COALESCE(:id, 0)")
	boolean verifierUniciteEmail( String email, Long id );


}
