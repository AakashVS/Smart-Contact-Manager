package com.scm20.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scm20.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {
  @Autowired
  private OAuthAuthenticationSucessHandler handler;

  @Autowired   
  private SecurityCustomUserDetailService userDetailService;
  
  @Autowired
  private AuthFailureHandler authFailureHandler;

    // @SuppressWarnings("deprecation")
    // UserDetails user1 = User
    // .withDefaultPasswordEncoder()
    // .username("admin123")
    // .password("admin123")  // Use {noop} if password encoding is not needed
    // .roles("ADMIN","USER")              // Assign a role, e.g., ADMIN
    // .build();

       
    // @SuppressWarnings("deprecation")
    // UserDetails user2 = User
    // .withDefaultPasswordEncoder()
    // .username("user123")
    // .password("user123")  // Use {noop} if password encoding is not needed
    // //.roles("ADMIN")              // Assign a role, e.g., ADMIN
    // .build();


    // public UserDetailsService userDetailsService(){
    //    var inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
    //    return inMemoryUserDetailsManager;
    // }

       @Bean
       public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
         @Bean       
         public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
          // configuration
              
          // urls configure who urls are public or private 
            httpSecurity.authorizeHttpRequests(authorize -> {
                // authorize.requestMatchers("/home", "/signup", "/service").permitAll();
                authorize.requestMatchers("/user/**").authenticated();
                authorize.anyRequest().permitAll();
            });
    
              httpSecurity.formLogin(formLogin->{
              formLogin.loginPage("/login");
              formLogin.loginProcessingUrl("/authenticate");
              formLogin.successForwardUrl("/user/profile");
              //formLogin.failureForwardUrl("/login?error=true");
              // formLogin.defaultSuccessUrl("/home");
              formLogin.usernameParameter("email");
              formLogin.passwordParameter("password");
              //failure handler
              formLogin.failureHandler(authFailureHandler);

            });
            httpSecurity.csrf(AbstractHttpConfigurer::disable);
            //Oauth configurations
            httpSecurity.logout(logoutForm -> {
              logoutForm.logoutUrl("/logout");
              logoutForm.logoutSuccessUrl("/login?logout=true");
             });

             //OAuth configuration
             httpSecurity.oauth2Login(oauth ->{
              oauth.loginPage("/login");
              oauth.successHandler(handler);
             });
               return httpSecurity.build();

             }

                 @Bean
                 public PasswordEncoder passwordEncoder(){
                  return new BCryptPasswordEncoder();
             }

}