package pl.four.software.restapiserver.domain.model.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.four.software.restapiserver.domain.model.configuration.ResponseHeadersSecurityFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String CLINIC_REALM = "CLINIC_REALM";

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterAfter(new ResponseHeadersSecurityFilter(), BasicAuthenticationFilter.class);

        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/api/db/patients/**")
            .hasAnyRole("ADMIN", "USER")
            .antMatchers("/api/memory/patients/**")
            .permitAll()
            .and()
            .httpBasic()
            .realmName(CLINIC_REALM)
            .authenticationEntryPoint(getBasicAuthEntryPoint())
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public ClinicBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
        return new ClinicBasicAuthenticationEntryPoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin")
            .password("{noop}abc123")
            .roles("ADMIN")
            .and()
            .withUser("user")
            .password("{noop}abc123")
            .roles("USER");
    }

}
