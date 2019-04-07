package ru.digitalsuperhero.dshapi.dao.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table
@RestResource(rel = "contractors", path = "contractors")
public class Contractor implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String companyName;
    private String email;
    private String password;
    @OneToMany(targetEntity = WorkRequest.class)
    private List<WorkRequest> workRequests;
    @OneToMany
    List<Review> reviews;
    /**
     * Contractor rating stars from 1.0 to 5.0 based on proposed and executed commercial offers.
     */
    private Double rating;

    public Contractor(Long id, String companyName, String email, String password) {
        this.id = id;
        this.companyName = companyName;
        this.email = email;
        this.password = password;
    }

    public List<String> getRoles() {
        List<String> roles = new LinkedList<>();
        getAuthorities().stream().forEach(authority -> roles.add(((GrantedAuthority) authority).toString()));
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_CONTRACTOR"));
    }


    @Override
    public String getPassword() {
        return this.password;
    }

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
        return true;
    }
}
