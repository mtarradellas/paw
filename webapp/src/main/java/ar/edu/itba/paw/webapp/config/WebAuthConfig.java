package ar.edu.itba.paw.webapp.config;

import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ar.edu.itba.paw.webapp.auth.JwtAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.JwtAuthorizationFilter;
import ar.edu.itba.paw.webapp.auth.PSUserDetailsService;
import ar.edu.itba.paw.webapp.util.ApiUtils;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@EnableWebSecurity
@Configuration
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:rememberMeToken.txt")
    private Resource token;

    @Value("classpath:jwtSecret.txt")
    private Resource secretPath;

    @Autowired
    private PSUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    CORSFilter getCorsFilter(){
        return new CORSFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String jwtAudience = "Pet Society";
        final String jwtIssuer = "Pet Society Inc.";
        final String jwtType = "JWT";

        http.sessionManagement()
            .and().csrf().disable()
            .addFilterBefore(new CORSFilter(), (Class<? extends Filter>) ChannelProcessingFilter.class)
            .addFilter((Filter) new JwtAuthenticationFilter(authenticationManager(), jwtAudience, jwtIssuer, ApiUtils.readToken(secretPath), jwtType))
            .addFilter((Filter) new JwtAuthorizationFilter (authenticationManager(), jwtAudience, jwtIssuer, ApiUtils.readToken(secretPath), jwtType))
            .authorizeRequests()
                .antMatchers("/login", "/register", "/request-password-reset").anonymous()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/questions/**").authenticated()
                .antMatchers("/users/**").authenticated()
                .antMatchers("/reviews/**").authenticated()
                .antMatchers("/pets/upload").authenticated()
                .antMatchers("/pets/*/question", "/pet/*/answer").authenticated()
                .antMatchers("/pets/*/request","/interests/**","/requests/**").authenticated()
                .anyRequest().permitAll()
            .and().rememberMe()
                .rememberMeParameter("rememberme")
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(365))
                .key(ApiUtils.readToken(token))
//            .and().logout() /* TODO uncomment?*/
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")
            .and().exceptionHandling()
                .accessDeniedPage("/403")
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**", "/img/**", "/favicon.ico", "/403");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
