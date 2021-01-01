package com.photoSharing.test;

import com.photoSharing.dao.ImageFavorDao;
import com.photoSharing.dao.UserDao;
import com.photoSharing.entity.travelimagefavor;
import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.WebUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: Project
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-14 08:51
 **/
public class UserDaoTest {
    @Test
    public void testGetUsers(){
        UserDao userDao = new UserDao();
            ArrayList<traveluser> travelusers = new ArrayList<>(userDao.findAll());
            for (traveluser traveluser : travelusers) {
                System.out.println(traveluser.toString());
            }
    }
    @Test
    public void testFavor(){
        ImageFavorDao imageFavorDao = new ImageFavorDao();
        List<travelimagefavor> travelimagefavorList = imageFavorDao.findAllMyFavorImages(15);
        for (travelimagefavor travelimagefavor : travelimagefavorList) {
            System.out.println(travelimagefavor.toString());
        }
    }
    @Test
    public void testTime(){
        WebUtils webUtils = new WebUtils();
        Date date = webUtils.getBJTime();
        System.out.println(date);
    }
    @Test
    public void insert(){
        traveluser traveluser = new traveluser();
        traveluser.setEmail("tester2@qq.com");
        traveluser.setPass("123456");
        traveluser.setUserName("name");
        UserDao userDao = new UserDao();
        boolean i = userDao.insert(traveluser);
        System.out.println(traveluser.toString());
        System.out.println(i);
        testGetUsers();
    }
}
