package boldair.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    private SimpleUrlAuthenticationSuccessHandler adminSuccessHandler = 
        new SimpleUrlAuthenticationSuccessHandler("/compte/admin");
        
    private SimpleUrlAuthenticationSuccessHandler userSuccessHandler = 
        new SimpleUrlAuthenticationSuccessHandler("/compte/utilisateur");
    
    private SimpleUrlAuthenticationSuccessHandler benevolSuccessHandler = 
        new SimpleUrlAuthenticationSuccessHandler("/compte/benevol");
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
            Authentication authentication) throws IOException, ServletException {
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                adminSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }
            
            if (authority.getAuthority().equals("ROLE_BENEVOL")) {
                benevolSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }
        }
        
        // Default to user page if not admin or benevol
        userSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }
} 