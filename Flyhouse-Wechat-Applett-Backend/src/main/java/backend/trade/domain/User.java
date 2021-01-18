package backend.trade.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 128)
    @Column(unique = true)
    private String openid; // WeChat Openid

    private int notification = 0;

    private boolean lottery = false;

    private Integer creditValue = 100;


}
