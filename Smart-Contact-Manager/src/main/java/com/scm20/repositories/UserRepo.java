package com.scm20.repositories;
import com.scm20.entities.User;

import java.util.Optional;

//import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
   

    // custom finder method
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email,String password);

    default User getUserByEmail(String email) {
    return findByEmail(email).orElse(null);
    
     }

    Optional<User> findByEmailToken(String id);
   
    
}
