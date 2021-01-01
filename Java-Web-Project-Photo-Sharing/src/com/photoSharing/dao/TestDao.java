package com.photoSharing.dao;

import com.photoSharing.entity.geocountries_regions;
import com.photoSharing.entity.travelimage;
import com.photoSharing.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: Project
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-13 15:43
 **/
public class TestDao {
    public List<geocountries_regions> getT()  {
        try {
            QueryRunner qr = JdbcUtils.getQueryRunner();
            ArrayList<geocountries_regions> arrayList = new ArrayList<>(qr.query("SELECT * FROM geocountries_regions", new BeanListHandler<geocountries_regions>(geocountries_regions.class)));
            return arrayList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
