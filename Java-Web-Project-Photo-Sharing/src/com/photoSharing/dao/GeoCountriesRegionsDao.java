package com.photoSharing.dao;

import com.photoSharing.entity.geocities;
import com.photoSharing.entity.geocountries_regions;
import com.photoSharing.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

/**
 * @program: Project
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-14 14:49
 **/
public class GeoCountriesRegionsDao {
    /**
    * @Description: 通过国家名获得国家
    * @Param: [Name]
    * @return: com.photoSharing.entity.geocountries_regions
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public geocountries_regions findByCountryByName(String Name){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from geocountries_regions where Country_RegionName=?";
            geocountries_regions gr = qr.query(sql,new BeanHandler<>(geocountries_regions.class),new Object[]{Name});
            return gr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public geocountries_regions findByCountryByISO(String ISO){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from geocountries_regions where ISO=?";
            geocountries_regions gr = qr.query(sql,new BeanHandler<>(geocountries_regions.class),new Object[]{ISO});
            return gr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<geocountries_regions> findAll(){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from geocountries_regions";
            List<geocountries_regions> gr = qr.query(sql,new BeanListHandler<>(geocountries_regions.class));
            return gr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
