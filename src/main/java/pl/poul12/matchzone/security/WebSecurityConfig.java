package pl.poul12.matchzone.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.poul12.matchzone.service.UserDetailsServiceImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Date;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {

        System.out.println("Jestem w WebSecurityConfig.authenticationJwtTokenFilter " + new Date());

        return new JwtAuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        System.out.println("Jestem w WebSecurityConfig.passwordEncoder " + new Date());

        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {

        System.out.println("Jestem w WebSecurityConfig.configure (authenticationManagerBuilder) " + new Date());

        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {

        System.out.println("Jestem w WebSecurityConfig.authenticationManagerBean " + new Date());

        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        System.out.println("Jestem w WebSecurityConfig.configure (http)" + new Date());

        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/api/v1/users/login", "/api/v1/users/register", "/").permitAll()
                .antMatchers("/api/v1/users/*").authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        System.out.println("Jestem w WebSecurityConfig.configure (addFilterBefore)" + new Date());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}