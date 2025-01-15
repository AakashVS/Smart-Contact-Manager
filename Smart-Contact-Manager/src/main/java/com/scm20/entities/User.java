package com.scm20.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
 import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String userId;

    @Column(name = "user_name",nullable = false) 
    private String name;

    @Column(unique = true, nullable = false , length = 150) 
    private String email;
    
    @Getter(AccessLevel.NONE)
    private String password;

    @Column(columnDefinition = "TEXT") 
    private String about;

    @Column(columnDefinition = "TEXT") 
    private String profilePic;
    
    private String phoneNumber;
   
    // information
    @Getter(AccessLevel.NONE)
    private boolean enabled = false;

    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    // self ,google,facebook,twitter,linkedin,github
    @Enumerated(value = EnumType.STRING)
    private Providers  provider = Providers.SELF;
    private String providerUserId;

     // add more field if  needed
      @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch =FetchType.LAZY ,orphanRemoval = true)
     private List<Contact>contacts = new ArrayList<>();
      
     @ElementCollection(fetch = FetchType.EAGER)
     private List<String> roleList=new ArrayList<>();

     private String emailToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       
        //list of roles[user,admin]
        //collection of SimpleGrantedAuthority[roles{admin,user}]
        Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
            return roles;
    }
    
    // for this project our email and username both are same
    @Override
    public String getUsername() {
        
        return this.email;
        
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
      
     }

     @Override
     public boolean isAccountNonLocked() {
         return true;
     }

     @Override
     public boolean isCredentialsNonExpired() {
         return true;
     }
    
     @Override
     public boolean isEnabled() {
         return this.enabled;
     }

    @Override
    public String getPassword() {
        return this.password;
    }
  
    @PrePersist
    private void prePersist() {
        if (this.userId == null) {
            this.userId = UUID.randomUUID().toString();
        }
    }

        


}


