package fudan.se.lab2.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.*;

/**
 * @author LBW
 */
@Entity
public class User implements UserDetails {

    private static final long serialVersionUID = -6140085056226164016L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String fullName;
    private String email;
    private String institution;
    private String country;

    @ManyToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();


    public User() {}
    public User(String username, String password, String fullName, String email,String institution,String country, Set<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.institution=institution;
        this.country=country;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName(){return this.fullName;}

    public void setFullName(String fullName){this.fullName=fullName;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }



    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
