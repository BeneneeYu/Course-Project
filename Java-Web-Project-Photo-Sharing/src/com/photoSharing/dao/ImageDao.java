package com.photoSharing.dao;

import com.photoSharing.entity.travelimage;
import com.photoSharing.entity.travelimagefavor;
import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.JdbcUtils;
import com.photoSharing.utils.WebUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: Project
 * @description: 操作图片的Dao
 * @author: Shen Zhengyu
 * @create: 2020-07-14 11:07
 **/
public class ImageDao {
    /**
    * @Description: 显示所有图片
    * @Param: []
    * @return: java.util.List<com.photoSharing.entity.travelimage>
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public List<travelimage> findAll(){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimage";
            List<travelimage> list = (List<travelimage>)qr.query(sql,new BeanListHandler<travelimage>(travelimage.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<travelimage> findAccordingToRules(String choose,String order,String keyword){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            StringBuilder sb = new StringBuilder();
            sb.append("'%");
            sb.append(keyword);
            sb.append("%'");

            String sql = "select * from travelimage WHERE "+ choose+" LIKE "+sb.toString()+" ORDER BY " + order + " DESC";
            System.out.println(sql);
            List<travelimage> list = qr.query(sql,new BeanListHandler<>(travelimage.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
    * @Description: 用于首页
    * @Param: []
    * @return: java.util.List<com.photoSharing.entity.travelimage>
    * @Author: Shen Zhengyu
    * @Date: 2020/7/16
    */
    public List<travelimage> findHottestPhotos(){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimage ORDER BY Heat DESC LIMIT 3;";
            List<travelimage> list = qr.query(sql,new BeanListHandler<>(travelimage.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
    * @Description: 用于首页
    * @Param: []
    * @return: java.util.List<com.photoSharing.entity.travelimage>
    * @Author: Shen Zhengyu
    * @Date: 2020/7/16
    */
    public List<travelimage> findLatestPhotos(){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimage ORDER BY DateUpdated DESC LIMIT 3;";
            List<travelimage> list = (List<travelimage>)qr.query(sql,new BeanListHandler<travelimage>(travelimage.class));
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    /**
    * @Description: 根据图片的ID查找图片
    * @Param: [id]
    * @return: com.photoSharing.entity.travelimage
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public travelimage findById(int id){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimage where ImageID=?";
            travelimage image = (travelimage)qr.query(sql,new BeanHandler<>(travelimage.class),new Object[]{id});
            return image;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
    * @Description: 获得我上传的所有图片
    * @Param: [UID]
    * @return: java.util.List<com.photoSharing.entity.travelimage>
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public List<travelimage> findAllByUID(int UID){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimage where UID=?";
            List<travelimage> imageList = (List<travelimage>)qr.query(sql,new BeanListHandler<>(travelimage.class),new Object[]{UID});
            return imageList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
    * @Description: 测试是否路径可用，可用，返回true，要传全名
    * @Param: [path]
    * @return: boolean
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public boolean findIfPathIsFree(String path){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimage where PATH=?";
            travelimage image = (travelimage)qr.query(sql,new BeanHandler<>(travelimage.class),new Object[]{path});
            if(null == image){
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Boolean insert(travelimage image) {
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            WebUtils webUtils = new WebUtils();
            Date date = webUtils.getBJTime();
            int i = (int)(Math.random()*9000 + 1000);
            while (true){
                if (null == findById(i)){
                    i = (int)(Math.random()*9000 + 1000);
                    break;
                }
            }
            //标题、作者、主题、简介、国家（代码）、城市、经度、维度由其他表获得
            String sql = "insert into travelimage (" +
                    "ImageID,Title,Content,Description,UID,Country_RegionCodeISO,CityCode,Latitude,Longitude,Path,Heat,DateUpdated,Author) " +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            //未完成
            int num = qr.update(sql, i,image.getTitle(),image.getContent(),image.getDescription(),
                    image.getUID(),image.getCountry_RegionCodeISO(),image.getCityCode(),image.getLatitude(),image.getLongitude(),image.getPATH(),0,date,image.getAuthor());
            return num > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<travelimage> listImageIFavor(int UID){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            ImageFavorDao imageFavorDao = new ImageFavorDao();
            List<travelimagefavor> travelimagefavors = imageFavorDao.findAllMyFavorImages(UID);
            List<travelimage> travelimagesIFavor = new ArrayList<>();
            
            String sql = "select * from travelimage where ImageID=?";
            for (travelimagefavor tif : travelimagefavors) {
                travelimage image = qr.query(sql,new BeanHandler<>(travelimage.class),tif.getImageID());
                travelimagesIFavor.add(image);
            }
            return travelimagesIFavor;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public List<travelimage> listImageIUpload(int UID){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from travelimage where UID=?";
            List<travelimage> imageList = qr.query(sql,new BeanListHandler<>(travelimage.class),UID);
            return imageList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public Boolean update(travelimage image) {
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            WebUtils webUtils = new WebUtils();
            Date date = webUtils.getBJTime();

            //标题、作者、主题、简介、国家（代码）、城市、经度、维度由其他表获得
//            String sql = "UPDATE travelimage set Title=?,Content=?,Description=?,"
            String sql = "update travelimage set" +
                    " Title=?,Content=?,Description=?,Country_RegionCodeISO=?,CityCode=?,Latitude=?,Longitude=?,Path=?,Heat=?,DateUpdated=?,Author=?" +
                    " where ImageID=?";
            //未完成
            int num = qr.update(sql, image.getTitle(),image.getContent(),image.getDescription()
                    ,image.getCountry_RegionCodeISO(),image.getCityCode(),image.getLatitude(),image.getLongitude(),image.getPATH(),image.getHeat(),date,image.getAuthor(),image.getImageID());
            return num > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean delete(int ImageID,int UID) {
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            //标题、作者、主题、简介、国家（代码）、城市、经度、维度由其他表获得
            String sql = "DELETE FROM travelimage WHERE ImageID=? AND UID=?";
            //未完成
            int num = qr.update(sql,ImageID,UID);
            if(num > 0){
                ImageFavorDao ifd = new ImageFavorDao();

                ifd.deleteFavorAboutOne(ImageID);
            }
            return num > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addHeat(int ImageID){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            //标题、作者、主题、简介、国家（代码）、城市、经度、维度由其他表获得
            String sql = "update travelimage set Heat=? WHERE ImageID=?";
            //未完成
            travelimage ti = findById(ImageID);
            int num = qr.update(sql,ti.getHeat()+1,ImageID);
            return num > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean reduceHeat(int ImageID){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            //标题、作者、主题、简介、国家（代码）、城市、经度、维度由其他表获得
            String sql = "update travelimage set Heat=? WHERE ImageID=?";
            //未完成
            travelimage ti = findById(ImageID);
            int num = qr.update(sql,ti.getHeat()-1,ImageID);
            return num > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
