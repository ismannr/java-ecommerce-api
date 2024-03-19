package com.example.projectv1.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// Repository is a generic interface that takes <T, ID>
// T is the class (User) and ID is the id of the User Class
public interface UserRepository extends JpaRepository<User, Integer> {
//    add method as necessary, there's a lot of method that provided by JpaRepository

//    a method that retrieve user by email
    Optional<User> findByEmail(String email); // 'email' as what instantiated on User class

    boolean existsUserByEmail(String email);

    public User findByResetPasswordToken(String token);

}
