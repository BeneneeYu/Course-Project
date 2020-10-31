package id;

import java.io.Serializable;

/**
 * @program: CSE_lab1
 * @description: 整数ID
 * @author: Shen Zhengyu
 * @create: 2020-10-08 20:46
 **/
public class IntegerId implements Id, Serializable {
    private int Id;
    public IntegerId(int Id) {
        this.Id = Id;
    }

    public Integer getId() {
        return Id;
    }
}
