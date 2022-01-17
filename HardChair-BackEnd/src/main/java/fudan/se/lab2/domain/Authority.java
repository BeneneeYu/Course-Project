package fudan.se.lab2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

/**
 * @author LBW
 */
@Entity
public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = -8974777274465208640L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    @Column(unique = true)
    private String authority;

    @ManyToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE},mappedBy = "authorities")
    @JsonIgnore
    private Set<User> users;

    public Authority() {
    }

    public Authority(String username,String authority) {
        this.username=username;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
