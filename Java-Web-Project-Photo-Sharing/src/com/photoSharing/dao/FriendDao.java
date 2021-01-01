package com.photoSharing.dao;

import com.photoSharing.entity.friendship;
import com.photoSharing.entity.geocities;
import com.photoSharing.entity.travelimage;
import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

/**
 * @program: Project
 * @description: 好友相关功能
 * @author: Shen Zhengyu
 * @create: 2020-07-16 10:40
 **/
public class FriendDao {
    public boolean addFriend(int UID1,int UID2){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "INSERT INTO friendship (FriendshipID,FriendOne,FriendTwo) VALUES(?,?,?)";
            int num = qr.update(sql,0,UID1,UID2);
            String sql2 = "INSERT INTO friendship (FriendshipID,FriendOne,FriendTwo) VALUES(?,?,?)";
            int num2 = qr.update(sql2,0,UID2,UID1);
            return (num > 0 && num2 > 0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean isFriend(int UID1,int UID2){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "SELECT * FROM friendship where FriendOne=? AND FriendTwo=?";
            friendship fr = qr.query(sql,new BeanHandler<>(friendship.class),UID1,UID2);
            return !(null == fr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }


    public List<friendship> findFriendsByUID(int UID){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "SELECT * FROM friendship where FriendOne=?";
            List<friendship> friendships = qr.query(sql,new BeanListHandler<>(friendship.class),UID);
            return friendships;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
