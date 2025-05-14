package boldair.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity( jsr250Enabled = true, securedEnabled = true )
public class ConfigSecurity {

	// -------
	// SecurityFilterChain

	@Bean
	SecurityFilterChain securityFilterChain( HttpSecurity httpSecurity, ServiceSecurity userDetailsService )
			throws Exception {

		httpSecurity
				.csrf( csrf -> csrf.disable() ).formLogin( formLogin -> formLogin.loginPage( "/login" ) )
				.rememberMe( rememberMe -> rememberMe.key( "3c707a4c-34c9-4421-a3d4-85f20db0359e" ) )
				.logout( logout -> logout.logoutUrl( "/logout" ).logoutSuccessUrl( "/disconnected" ) )
				.exceptionHandling( req -> req.accessDeniedPage( "/accessDenied" ) )
				.authorizeHttpRequests(
						auth -> auth.requestMatchers( "/*", "/css/**", "/img/**", "/js/**", "/lib/**" ).permitAll() )
				.authorizeHttpRequests( auth -> auth.anyRequest().authenticated() );

		return httpSecurity.build();

	}

	// -------
	// PasswordEncoder

	@Bean
	PasswordEncoder passwordEncoder() {
		var dpe = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
		dpe.setDefaultPasswordEncoderForMatches( new BCryptPasswordEncoder() );
		return dpe;
	}
}
