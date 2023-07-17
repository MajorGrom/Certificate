//package uz.isystem.Certificate.security;
//
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//
//
//
//    @EnableWebSecurity
//    public class SecurityConfiguration extends WebSecurityConfigurationAdapter {
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.inMemoryAuthentication()
//                    .withUser("admin").password("{noop}root").roles("ADMIN");
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.cors().and().csrf().disable();
//
//            http.authorizeRequests()
//                    .antMatchers("/api/v1/users").hasRole("ADMIN")
//                    .antMatchers("/api/v1/direction").hasRole("ADMIN")
//                    .antMatchers("/api/v1/certificate").hasRole("ADMIN")
//                    .antMatchers("/users").hasRole("ADMIN")
//                    .antMatchers("/direction").hasRole("ADMIN")
//                    .antMatchers("/certificate").hasRole("ADMIN")
//                    .anyRequest().permitAll()
//                    .and().httpBasic();
//        }
//    }
//}
