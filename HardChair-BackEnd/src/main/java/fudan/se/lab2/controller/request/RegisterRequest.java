package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

/**
 * @author LBW
 */
@Controller
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String institution;
    private String country;
    private Set<String> authorities;

    @Autowired
    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String fullName,String email,String institution,String country, Set<String> authorities) {
        this.username = username;
        this.password = password;
        this.fullName=fullName;
        this.email = email;
        this.institution=institution;
        this.country=country;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
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

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}

