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

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class ConfigSecurity {

	private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

	// -------
	// SecurityFilterChain

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, ServiceSecurity userDetailsService)
			throws Exception {

		httpSecurity
				.csrf(csrf -> csrf.disable())
				.formLogin(formLogin -> formLogin
					.loginPage("/login")
					.successHandler(authenticationSuccessHandler)
				)
				.rememberMe(rememberMe -> rememberMe
					.key("3c707a4c-34c9-4421-a3d4-85f20db0359e")
				)
				.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/disconnected")
				)
				.exceptionHandling(req -> req
					.accessDeniedPage("/accessDenied")
				)
				.authorizeHttpRequests(
						auth -> auth
							.requestMatchers("/*", "/css/**", "/img/**", "/js/**", "/lib/**", "/inscription", "/classement")
							.permitAll()
				)
				.authorizeHttpRequests(auth -> auth
					.anyRequest()
					.authenticated()
				);

		return httpSecurity.build();
	}

	// -------
	// PasswordEncoder

	@Bean
	PasswordEncoder passwordEncoder() {
		var dpe = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
		dpe.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
		return dpe;
	}
}
