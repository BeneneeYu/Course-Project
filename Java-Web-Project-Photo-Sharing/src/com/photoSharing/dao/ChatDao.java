package com.photoSharing.dao;

import com.photoSharing.entity.chathistory;
import com.photoSharing.entity.travelimage;
import com.photoSharing.utils.JdbcUtils;
import com.photoSharing.utils.WebUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.Date;
import java.util.List;

/**
 * @program: Project
 * @description: 聊天
 * @author: Shen Zhengyu
 * @create: 2020-07-17 09:03
 **/
public class ChatDao {
    public List<chathistory> findAllByUID(int UID1,int UID2){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from chathistory where (SenderID=? AND ReceiverID=?) OR (SenderID=? AND ReceiverID=?) ORDER BY Date";
            List<chathistory> list = (List<chathistory>)qr.query(sql,new BeanListHandler<chathistory>(chathistory.class),UID1,UID2,UID2,UID1);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertChathistory(int senderUID,int receiverID,String content){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            WebUtils webUtils = new WebUtils();
            System.out.println(content);
            String sql = "insert into chathistory (SenderID,ReceiverID,Date,Content) VALUES (?,?,?,?)";
            qr.execute(sql,senderUID,receiverID,webUtils.getBJTime(),content);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
