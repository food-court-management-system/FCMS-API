package xiaolin.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilters extends OncePerRequestFilter {

    @Autowired
    private FCMSUserDetailService fcmsUserDetailService;

    @Autowired
    private Environment environment;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String username, prefix = null, jwt = null;
        String[] s;
        if (authorizationHeader != null) {
            s = authorizationHeader.split(" ");
            prefix = s[0];
            jwt = s[1];
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (prefix.equals("Google") || prefix.equals("Facebook")) {
            String email, id, link;

            if (prefix.startsWith("Google")) {
                link = String.format(environment.getProperty("google.link.get.profile"), jwt);
            } else {
                link = String.format(environment.getProperty("facebook.link.get.profile"), jwt);
            }
            String response = Request.Get(link).execute().returnContent().asString();
            ObjectMapper mapper = new ObjectMapper();
            email = mapper.readTree(response).get("email").textValue();
            id = mapper.readTree(response).get("id").textValue();
            int indexOfAt = email.indexOf("@");
            username = email.substring(0, indexOfAt + 1) + id;
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.fcmsUserDetailService.loadUserByUsername(username);
                if (jwtUtil.validateCustomerToken(username, userDetails, indexOfAt)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        else if (prefix.equals("Bearer")) {
            username = jwtUtil.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.fcmsUserDetailService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
    }
}
