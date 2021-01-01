package com.photoSharing.dao;

import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.Base64Code;
import com.photoSharing.utils.JdbcUtils;
import com.photoSharing.utils.WebUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @program: Project
 * @description: 用户Dao
 * @author: Shen Zhengyu
 * @create: 2020-07-13 22:46
 **/
public class UserDao {
    public List<traveluser> findAll(){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from traveluser";
            List<traveluser> list = (List<traveluser>)qr.query(sql,new BeanListHandler<traveluser>(traveluser.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public traveluser findById(int id){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from traveluser where UID=?";
            traveluser user = (traveluser)qr.query(sql,new BeanHandler<>(traveluser.class),new Object[]{id});
            return user;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public traveluser findByUsername(String username){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql0 = "update traveluser set Pass=? where Pass=?";
            qr.execute(sql0, "YWJjZDEyMzQ=","abcd1234");
            String sql1 = "update traveluser set Pass=? where Pass=?";
            qr.execute(sql1, "MTIzNDU2","123456");


            String sql = "select * from traveluser where UserName=?";
            traveluser user = (traveluser)qr.query(sql,new BeanHandler<>(traveluser.class),new Object[]{username});
            return user;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Boolean insert(traveluser user) {
        if (null != findByUsername(user.getUserName())){
            return false;
        }
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            WebUtils webUtils = new WebUtils();
            Date date = webUtils.getBJTime();
            String sql = "insert into traveluser (UID,Email,UserName,Pass,State,DateJoined,DateLastModified) values (?,?,?,?,?,?,?)";
            int num = qr.update(sql,  new Object[]{0, user.getEmail(), user.getUserName(), user.getPass(), 1, date, date});
            if (num > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean update(traveluser user){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            WebUtils webUtils = new WebUtils();
            Date date = webUtils.getBJTime();
            String sql = "update traveluser Email=?,UserName=?,Pass=?,DateLastModified=? where UID=?";
            int num = qr.update(sql,  new Object[]{user.getEmail(), user.getUserName(), user.getPass(), date,user.getUID()});
            if (num > 0) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public Boolean delete(int userid){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "delete FROM traveluser where id=?";
            int num = qr.update(sql,userid);
            if (num > 0) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Boolean updateState(traveluser tu){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            traveluser tu1 = findById(tu.getUID());
            String sql = "update traveluser set State=? where UID=?";
            int num = qr.update(sql,-tu1.getState(),tu1.getUID());
            System.out.println(tu1.getState());
            return num > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;    }

    }

