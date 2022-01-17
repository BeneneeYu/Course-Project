package fudan.se.lab2.controller.response;

public class SearchResponse {
    private String fullName;
    private String username;
    private String email;
    private String institution;
    private String country;
    private Integer status;

    public SearchResponse(String fullName,String username,String email,String institution,String country,Integer status){
        this.username=username;
        this.fullName=fullName;
        this.email=email;
        this.institution=institution;
        this.country=country;
        this.status=status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
