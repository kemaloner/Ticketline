package at.ac.tuwien.inso.sepm.ticketline.server.configuration;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties.H2ConsoleConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.server.security.HeaderTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    public SecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public static PasswordEncoder configureDefaultPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes((WebRequest) requestAttributes, includeStackTrace);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, List<AuthenticationProvider> providerList) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("SELECT username, password, (ISACTIVE AND ACTIVETIME<CURRENT_TIMESTAMP()) as enabled from users where username=?")
            .authoritiesByUsernameQuery("SELECT username, (CASE WHEN Role = 1 THEN 'ADMIN' ELSE 'USER' END) as authority FROM USERS where username=?")
            .passwordEncoder(passwordEncoder);
        providerList.forEach(auth::authenticationProvider);
    }

    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    private static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final String h2ConsolePath;
        private final String h2AccessMatcher;

        @Autowired
        private AuthenticationManager authenticationManager;

        public WebSecurityConfiguration(
            H2ConsoleConfigurationProperties h2ConsoleConfigurationProperties
        ) {
            h2ConsolePath = h2ConsoleConfigurationProperties.getPath();
            h2AccessMatcher = h2ConsoleConfigurationProperties.getAccessMatcher();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .headers().frameOptions().sameOrigin().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint((req, res, aE) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST, "/authentication").permitAll()
                .antMatchers(HttpMethod.GET,
                    "/v2/api-docs",
                    "/swagger-resources/**",
                    "/webjars/springfox-swagger-ui/**",
                    "/swagger-ui.html")
                .permitAll()
            .antMatchers(HttpMethod.GET, "/information").permitAll();

            if (h2ConsolePath != null && h2AccessMatcher != null) {
                http
                    .authorizeRequests()
                    .antMatchers(h2ConsolePath + "/**").access(h2AccessMatcher);
            }
            http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .addFilterBefore(new HeaderTokenAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
        }

           @Bean
          @Override
            public AuthenticationManager authenticationManagerBean() throws Exception {
              return super.authenticationManagerBean();
          }

    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins("*");
            }
        };
    }

}
