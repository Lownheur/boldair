package boldair.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import boldair.data.Participant;

public interface DaoParticipant extends CrudRepository<Participant, Long> {

	List<Participant> findByIdEquipe( Long idEquipe );
	
	List<Participant> findByEmail(String email);

}
