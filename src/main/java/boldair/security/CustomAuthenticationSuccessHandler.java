package boldair.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
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