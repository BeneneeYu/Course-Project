package com.photoSharing.servlet;

import com.photoSharing.dao.GeoCountriesRegionsDao;
import com.photoSharing.dao.GeocitiesDao;
import com.photoSharing.entity.geocities;
import com.photoSharing.entity.geocountries_regions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @program: Project
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-15 21:27
 **/
@WebServlet(name = "UtilServlet", urlPatterns = "/UtilServlet")
public class UtilServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (null != action && "getCountry".equals(action)){
            GeoCountriesRegionsDao geoCountriesRegionsDao = new GeoCountriesRegionsDao();
            List<geocountries_regions> geocountriesRegionsList = geoCountriesRegionsDao.findAll();
            StringBuilder sb = new StringBuilder();
            for (geocountries_regions geocountries_regions : geocountriesRegionsList) {
                sb.append(geocountries_regions.getCountry_RegionName());
                sb.append(",");
            }
            PrintWriter writer = resp.getWriter();
            writer.write(sb.toString());
        }else if ("getCity".equals(action)){
            GeocitiesDao geocitiesDao = new GeocitiesDao();
            List<geocities> list = geocitiesDao.findAllByCountryRegionName(req.getParameter("country"));
            StringBuilder sb = new StringBuilder();
            for (geocities geocities : list) {
                sb.append(geocities.getAsciiName());
                sb.append(",");
            }
            PrintWriter writer = resp.getWriter();
            writer.write(sb.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
