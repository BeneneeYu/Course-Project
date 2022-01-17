package fudan.se.lab2.domain;

import javax.persistence.*;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-04-26 20:30
 **/
@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String topic;

    public Topic(){}
    
    public Topic(String topics) {
        this.topic = topics;
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topics) {
        this.topic = topics;
    }

}
