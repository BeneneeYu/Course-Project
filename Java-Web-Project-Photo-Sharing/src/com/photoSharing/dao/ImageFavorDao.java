package com.photoSharing.dao;

import com.photoSharing.entity.travelimage;
import com.photoSharing.entity.travelimagefavor;
import com.photoSharing.utils.JdbcUtils;
import com.photoSharing.utils.WebUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.Date;
import java.util.List;

/**
 * @program: Project
 * @description: 处理新增收藏等
 * @author: Shen Zhengyu
 * @create: 2020-07-14 15:24
 **/
public class ImageFavorDao {

    /**
    * @Description: 增加收藏
    * @Param: [favor]
    * @return: java.lang.Boolean
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public Boolean addFavor(travelimagefavor favor){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            //标题、作者、主题、简介、国家（代码）、城市、经度、维度由其他表获得
            String sql = "insert into travelimagefavor (" +
                    "FavorID,UID,ImageID) " +
                    "values (?,?,?)";
            int num = qr.update(sql, new Object[]{0,favor.getUID(),favor.getImageID()});
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

    /**
    * @Description: 取消收藏
    * @Param: [favor]
    * @return: java.lang.Boolean
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public Boolean deleteFavor(travelimagefavor favor){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            //标题、作者、主题、简介、国家（代码）、城市、经度、维度由其他表获得
            String sql = "delete from travelimagefavor where UID=? and ImageID=? ";
            int num = qr.update(sql, new Object[]{favor.getUID(),favor.getImageID()});
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

    public Boolean deleteFavorAboutOne(int ImageID){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            //标题、作者、主题、简介、国家（代码）、城市、经度、维度由其他表获得
            String sql = "delete from travelimagefavor where ImageID=? ";
            int num = qr.update(sql, ImageID);
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

    /**
    * @Description: 根据UID获得我收藏的图片
    * @Param: [UID]
    * @return: java.util.List<com.photoSharing.entity.travelimagefavor>
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public List<travelimagefavor> findAllMyFavorImages(int UID ){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimagefavor where UID=?";
            List<travelimagefavor> list = (List<travelimagefavor>)qr.query(sql,new BeanListHandler<travelimagefavor>(travelimagefavor.class),UID);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean isFavor(int UID,int ImageID){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimagefavor where UID=? AND ImageID=?";
            travelimagefavor tif = qr.query(sql,new BeanHandler<travelimagefavor>(travelimagefavor.class),UID,ImageID);
           if (null == tif){
               return false;
           }else{
               return true;
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
