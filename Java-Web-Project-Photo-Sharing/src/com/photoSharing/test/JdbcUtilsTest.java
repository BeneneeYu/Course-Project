package com.photoSharing.test;

import com.photoSharing.entity.geocountries_regions;
import com.photoSharing.entity.travelimage;
import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @program: Project
 * @description: test
 * @author: Shen Zhengyu
 * @create: 2020-07-13 14:32
 **/
public class JdbcUtilsTest {
    @Test
    public void testGetConnection() throws SQLException{
        Connection connection = JdbcUtils.getConnection();
        System.out.println(connection);
    }


    /**
    * @Description: 测试获取数据库的图片
    * @Param: []
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/7/13
    */
    @Test
    public void testPhoto() throws SQLException{
        QueryRunner qr = JdbcUtils.getQueryRunner();
        ArrayList<travelimage> arrayList = new ArrayList<>(qr.query("SELECT * FROM travelimage", new BeanListHandler<travelimage>(travelimage.class)));
        for (travelimage travelimage : arrayList) {
            System.out.println(travelimage.getCityCode());
        }
    }

    @Test
    public void testUser() throws SQLException{
        QueryRunner qr = JdbcUtils.getQueryRunner();
        ArrayList<traveluser> arrayList = new ArrayList<>(qr.query("SELECT * FROM traveluser", new BeanListHandler<traveluser>(traveluser.class)));
        for (traveluser traveluser : arrayList) {
            System.out.println(traveluser.toString());
        }
    }
    @Test
    public void bigTest() throws SQLException {

        QueryRunner qr = JdbcUtils.getQueryRunner();
        ArrayList<geocountries_regions> arrayList = new ArrayList<>(qr.query("SELECT * FROM geocountries_regions", new BeanListHandler<geocountries_regions>(geocountries_regions.class)));
        for (geocountries_regions traveluser : arrayList) {
            System.out.println(traveluser.toString());
        }
    }

    @Test
    public void show() throws InterruptedException {
        for (int i = 0;i< 10;i++){

            Random random = new Random();
            String result="";
            for (int j=0;j<10;j++)
            {
                result+=random.nextInt(10);
            }
            System.out.println(result);        }
    }
}
