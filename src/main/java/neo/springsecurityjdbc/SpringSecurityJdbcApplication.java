package neo.springsecurityjdbc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.security.Principal;

@SpringBootApplication
public class SpringSecurityJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityJdbcApplication.class, args);
    }

    @Bean
    @DependsOnDatabaseInitialization
    UserDetailsManager memory(DataSource ds) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(ds);
        return jdbcUserDetailsManager;
    }

    @Bean
    InitializingBean initializer(UserDetailsManager manager) {
        return () -> {
            UserDetails uRahul = User.withDefaultPasswordEncoder()
                    .username("rahul")
                    .password("pass")
                    .roles("USER")
                    .build();
            manager.createUser(uRahul);
            UserDetails uPawan = User
                    .withUserDetails(uRahul)
                    .username("pawan")
                    .build();

            manager.createUser(uPawan);
        };
    }

}

@RestController
class GreetingRestController {

    @GetMapping("/greeting")
    String greeting(Principal principal) {
        return "Hello," + principal.getName();
    }
}

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic();
        http
                .authorizeHttpRequests().anyRequest().authenticated();
    }
}
