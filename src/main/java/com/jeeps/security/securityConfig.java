package com.jeeps.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class securityConfig {
    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsSrvc();
    }

    @Bean
    PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }
    
	@SuppressWarnings("removal")
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> 
		requests.requestMatchers("/","/public/**","/public/article/**","/css/**","/js/**","/fonts/**"
				,"/vendor/**","/webfonts/**","/image/**","/sendEmail/**").permitAll()
        		)
        .authorizeHttpRequests((requests) -> 
		requests.requestMatchers("/login/**","/signup/**").anonymous()
        		)
        	.authorizeHttpRequests((requests) -> 
        		requests.anyRequest().authenticated()
			)
        	.formLogin((form) -> form
    				.loginPage("/login")
    				.defaultSuccessUrl("/")
    				.permitAll()
    			)
        	.logout()
        	.logoutUrl("/logout")
        	.invalidateHttpSession(true)
        	.deleteCookies("JSESSIONID")
        	.logoutSuccessUrl("/public/accueil");
        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
