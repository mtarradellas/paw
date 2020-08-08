package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JwtAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.JwtAuthorizationFilter;
import ar.edu.itba.paw.webapp.auth.PSUserDetailsService;
import ar.edu.itba.paw.webapp.util.ApiUtils;
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
import javax.servlet.Filter;

@EnableWebSecurity
@Configuration
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:rememberMeToken.txt")
    private Resource token;

    @Value("classpath:jwtSecret.txt")
    private Resource secretPath;

    private final String jwtAudience = "Pet Society";
    private final String jwtIssuer = "Pet Society Inc.";
    private final String jwtType = "JWT";

    @Autowired
    private PSUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .addFilter((Filter) new JwtAuthenticationFilter(authenticationManager(), jwtAudience, jwtIssuer, ApiUtils.readToken(secretPath), jwtType))
            .addFilter((Filter) new JwtAuthorizationFilter(authenticationManager(), jwtAudience, jwtIssuer, ApiUtils.readToken(secretPath), jwtType))
            .authorizeRequests()
//                .antMatchers("/users/**").authenticated()
                .anyRequest().permitAll()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        http.sessionManagement()
//                .invalidSessionUrl("/")
//            .and().authorizeRequests()
//                .antMatchers("/login", "/register").anonymous()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").authenticated()
//                .antMatchers("/upload-pet").authenticated()
//                .antMatchers("/pet/*/request","/interests/**","/requests/**").authenticated()
//                .antMatchers("/pet/*/question", "/pet/*/answer").authenticated()
//                .antMatchers("/**").permitAll()
//            .and().formLogin()
//                .loginPage("/login")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .failureUrl("/login/error")
//                .defaultSuccessUrl("/", false)
//            .and().rememberMe()
//                .rememberMeParameter("rememberme")
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(365))
//                .key(ApiUtils.readToken(token))
//            .and().logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")
//            .and().exceptionHandling()
//                .accessDeniedPage("/403")
//            .and().csrf().disable();
//    }

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
