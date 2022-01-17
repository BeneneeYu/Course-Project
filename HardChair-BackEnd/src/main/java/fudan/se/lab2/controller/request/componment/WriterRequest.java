package fudan.se.lab2.controller.request.componment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WriterRequest {
    @JsonProperty("writerName")
    private String writerName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("institution")
    private String institution;
    @JsonProperty("country")
    private String country;

    @Autowired
    public WriterRequest(){

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
}
