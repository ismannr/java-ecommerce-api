package com.example.projectv1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data   //it will generate setter, getter, to string
@Builder    //build object
@NoArgsConstructor
@AllArgsConstructor //the constructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @OneToOne(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "profile_picture_id") // Name of the foreign key column
    private ProfilePicture profilePicture;

    @Id
    @GeneratedValue //it will set the strategy to AUTO
    private Integer id;

    @Getter
    private String firstName;
    @Getter
    private String lastName;

    private LocalDate dob;

    private String country;

    private String state;

    private String city;

    private String address;

    private String gender;

    private String email;

    private String password;

    private String resetPasswordToken;

    private LocalDateTime resetPasswordTokenExpiry;

//    after implementing UserDetails, add role => class type = ENUM, also add the annotation
    @Enumerated(EnumType.ORDINAL) //let JPA return value
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); // it will return role name (ADMIN and USER) ordered by number because it was ordinal
    }

    @Override
    public String getUsername() {
        return email; // set email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // should be true so the user not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // samee
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // same
    }

    @Override
    public boolean isEnabled() {
        return true; // same
    }

}
