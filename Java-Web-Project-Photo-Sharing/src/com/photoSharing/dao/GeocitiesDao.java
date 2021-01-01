package com.photoSharing.dao;

import com.photoSharing.entity.geocities;
import com.photoSharing.entity.geocountries_regions;
import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

/**
 * @program: Project
 * @description: 城市的各种数据访问
 * @author: Shen Zhengyu
 * @create: 2020-07-14 11:21
 **/
public class GeocitiesDao {

    /**
    * @Description: 通过国家代号获得国家下的所有城市
    * @Param: [CountryRegionCodeISO]
    * @return: java.util.List<com.photoSharing.entity.geocities>
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public List<geocities> findAllByCountryRegionCodeISO(String CountryRegionCodeISO){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from geocities where Country_RegionCodeISO=?";
            List<geocities> list = qr.query(sql,new BeanListHandler<geocities>(geocities.class),CountryRegionCodeISO);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<geocities> findAllByCountryRegionName(String Country_RegionName){
        try {

            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from geocountries_regions where Country_RegionName=?";
            geocountries_regions gr = qr.query(sql,new BeanHandler<geocountries_regions>(geocountries_regions.class),Country_RegionName);

            List<geocities> list = findAllByCountryRegionCodeISO(gr.getISO());
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
    * @Description: 通过名字获得一个城市
    * @Param: [AsciiName]
    * @return: com.photoSharing.entity.geocities
    * @Author: Shen Zhengyu
    * @Date: 2020/7/14
    */
    public geocities findByAsciiName(String AsciiName){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from geocities where AsciiName=?";
            geocities city = qr.query(sql,new BeanHandler<>(geocities.class),AsciiName);
            return city;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public geocities findByCityCode(int CityCode){
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            String sql = "select * from geocities where GeoNameID=?";
            geocities city = qr.query(sql,new BeanHandler<>(geocities.class),CityCode);
            return city;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
