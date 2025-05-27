package boldair.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import boldair.data.Epreuve;

public interface DaoEpreuve extends CrudRepository<Epreuve, Long>, PagingAndSortingRepository<Epreuve, Long> {

	Epreuve findByNomBolDair( String nomBolDair );
}
