package xiaolin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xiaolin.config.jwt.FCMSUserDetailService;
import xiaolin.config.jwt.JwtTokenFilters;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FCMSUserDetailService fcmsUserDetailService;

    @Autowired
    private JwtTokenFilters jwtTokenFilters;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(fcmsUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                .antMatchers(HttpMethod.POST,"/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/sign-in").permitAll()
                .antMatchers(HttpMethod.GET, "/user/detail").permitAll()
                .antMatchers(HttpMethod.POST, "/customer/social-account").permitAll()
                .antMatchers(HttpMethod.GET, "/food-court/about").permitAll()
                .antMatchers(HttpMethod.GET, "/stall/food-stalls").permitAll()
                .antMatchers(HttpMethod.GET, "/stall/food-stalls/{id}/detail").permitAll()
                .antMatchers(HttpMethod.GET, "/stall/search").permitAll()
                .antMatchers(HttpMethod.GET, "/stall/filter/rate").permitAll()
                .antMatchers(HttpMethod.GET, "/stall/filter/{tag}").permitAll()
		.antMatchers(HttpMethod.POST, "/sign-in").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilters, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
