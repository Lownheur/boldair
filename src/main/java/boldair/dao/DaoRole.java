package boldair.dao;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import boldair.data.Role;

public interface DaoRole extends CrudRepository<Role, Long> {

    // Trouver tous les rôles triés par nom
    @Query("SELECT * FROM boldair.roles ORDER BY nomrole ASC")
    List<Role> findAllByOrderByNomRoleAsc();
}
