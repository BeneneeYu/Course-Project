package id;

import java.io.Serializable;

/**
 * @program: CSE_lab1
 * @description: 字符串ID
 * @author: Shen Zhengyu
 * @create: 2020-10-08 20:49
 **/
public class StringId implements Id, Serializable {
    private String Id;
    public StringId(String Id){
        this.Id = Id;
    }
    public String getId(){
        return Id;
    }
}
