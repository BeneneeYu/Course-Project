package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SearchUserRequest {
    private String search_key;
    private String full_name;


    @Autowired
    public SearchUserRequest(){}

    public SearchUserRequest(String search_key,String full_name){
        this.search_key=search_key;
        this.full_name=full_name;
    }

    public String getSearch_key() {
        return search_key;
    }

    public void setSearch_key(String search_key) {
        this.search_key = search_key;
    }

    public String getFull_Name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
