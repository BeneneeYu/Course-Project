package fudan.se.lab2.domain;
import javax.persistence.*;
import java.util.*;

/**
 * @program: lab2
 * @description: 投稿文章的作者
 * @author: Shen Zhengyu
 * @create: 2020-04-26 20:16
 **/
@Entity
public class Writer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String writerName;
    private String email;
    private String institution;
    private String country;

    @ManyToMany(cascade ={CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Article> articles=new HashSet<>();

    public Writer(){

    }
    public Writer(String writerName, String email, String institution, String country) {
        this.writerName = writerName;
        this.email = email;
        this.institution = institution;
        this.country = country;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Writer)){
            return false;
        }
        Writer writer=(Writer) object;
        return writer.getWriterName().equals(this.getWriterName())&&
                writer.getEmail().equals(this.getEmail());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
