package boldair.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import boldair.dao.DaoCompte;
import boldair.data.Compte;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceSecurity implements UserDetailsService {

	private DaoCompte daoCompte;

	@Override
	public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {

		Compte compte = daoCompte.findByPseudo( username );
		if ( compte == null ) {
			compte = daoCompte.findByEmail( username );
			if ( compte == null ) {
				throw new UsernameNotFoundException( "Le compte '" + username + "' n'existe pas" );
			}
		}

		List<String> roles = new ArrayList<>();
		roles.add( "USER" );
		if ( compte.isRoleAdmin() ) {
			roles.add( "ADMIN" );
		}

		UserDetails userDetails = User.withUsername( compte.getPseudo() ).password( compte.getEmpreinteMdp() )
				.roles( roles.toArray( new String[] {} ) ).build();

		return userDetails;
	}
}
