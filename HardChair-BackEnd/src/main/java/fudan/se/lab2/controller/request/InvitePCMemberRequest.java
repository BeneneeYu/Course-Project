package fudan.se.lab2.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.*;

/**
 * @description : 接收PCNumberRequest请求
 */
@Controller
public class InvitePCMemberRequest {
    private String fullName;
    private List<String> users;
    @Autowired
    public InvitePCMemberRequest(){

    }


    public InvitePCMemberRequest(List<String> users, String fullName){
        this.fullName=fullName;
        this.users=users;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
