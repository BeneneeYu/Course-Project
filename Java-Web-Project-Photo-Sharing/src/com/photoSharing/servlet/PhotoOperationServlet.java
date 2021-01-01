package com.photoSharing.servlet;

import com.photoSharing.dao.GeoCountriesRegionsDao;
import com.photoSharing.dao.GeocitiesDao;
import com.photoSharing.dao.ImageDao;
import com.photoSharing.entity.geocities;
import com.photoSharing.entity.travelimage;
import com.photoSharing.entity.traveluser;
import com.photoSharing.utils.WebUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: Project
 * @description: 上传图片用的Servlet
 * @author: Shen Zhengyu
 * @create: 2020-07-15 15:48
 **/
@WebServlet(name = "PhotoOperationServlet", urlPatterns = "/PhotoOperationServlet")
public class PhotoOperationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String method = req.getParameter("method");
        ImageDao imageDao = new ImageDao();
        traveluser tu = (traveluser) req.getSession().getAttribute("traveluser");
        if ("upload".equals(method)) {
            //上传图片，直接重定向到上传页面
            resp.sendRedirect("uploadPhoto.jsp");
        } else if ("update".equals(method)) {
            //修改图片，携带属性上传到修改页面
            String ImageID = req.getParameter("ImageID");
            travelimage ti = imageDao.findById(Integer.parseInt(ImageID));
            req.setAttribute("travelimage", ti);
            GeoCountriesRegionsDao gd = new GeoCountriesRegionsDao();
            GeocitiesDao gcd = new GeocitiesDao();
            if (null != gd.findByCountryByISO(ti.getCountry_RegionCodeISO())) {
                req.setAttribute("countryName", gd.findByCountryByISO(ti.getCountry_RegionCodeISO()).getCountry_RegionName());
            } else {
                req.setAttribute("countryName", "unknown");

            }
            if (null != gcd.findByCityCode(ti.getCityCode())) {
                req.setAttribute("cityName", gcd.findByCityCode(ti.getCityCode()).getAsciiName());
            } else {
                req.setAttribute("cityName", "unknown");

            }
            req.getRequestDispatcher("updatePhoto.jsp").forward(req, resp);
        } else if ("delete".equals(method)) {
            //删除我上传的图片，重定向到查询我的上传
            String ImageID = req.getParameter("ImageID");
            imageDao.delete(Integer.parseInt(ImageID), tu.getUID());
            resp.sendRedirect("user?method=retrieve");
        } else {
            //不能通过普通的方法获取post参数
            String ImageID = "";
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("utf-8");
            factory.setSizeThreshold(1024 * 1024 * 10);
            upload.setFileSizeMax(1024 * 1024 * 10);
            File uploadTemp = new File("/usr/local/SZYTomcat/tomcat/webapps/resources/travel-images/temp");
            factory.setRepository(uploadTemp);
            uploadTemp.mkdirs();
            //获取方法参数
            List<FileItem> list = new ArrayList<>();
            try {
                list = upload.parseRequest(req);
                for (FileItem fileItem : list) {
                    if (fileItem.isFormField()) {
                        //如果是普通表单字段
                        String name = fileItem.getFieldName();
                        String value = fileItem.getString("UTF-8");
                        if ("method".equals(name)) {
                            method = value;
                        } else if ("ImageID".equals(name)) {
                            ImageID = value;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ("uploadPhoto".equals(method)) {
                String suffix = "";
                String tg = "";
                try {
                    GeocitiesDao geocitiesDao = new GeocitiesDao();
                    GeoCountriesRegionsDao geoCountriesRegionsDao = new GeoCountriesRegionsDao();
                    Date DateUpdated = (new WebUtils()).getBJTime();
                    travelimage ti = new travelimage();
                    ti.setHeat(0);
                    ti.setDateUpdated(DateUpdated);
                    ti.setUID(tu.getUID());
                    for (FileItem fileItem : list) {
                        //文件
                        if (!fileItem.isFormField() && fileItem.getName() != null && !"".equals(fileItem.getName())) {
                            String filName = fileItem.getName();
                            WebUtils webUtils = new WebUtils();
                            //获取文件后缀名
                            suffix = filName.substring(filName.lastIndexOf("."));
                            tg = webUtils.tenDigitPathGetter();
                            while (true) {
                                if (imageDao.findIfPathIsFree(tg + suffix)) {
                                    break;
                                } else {
                                    tg = webUtils.tenDigitPathGetter();
                                }
                            }
                            //获取文件上传目录路径，在项目部署路径下的upload目录里。若想让浏览器不能直接访问到图片，可以放在WEB-INF下
                            String uploadPath = "/usr/local/SZYTomcat/tomcat/webapps/resources/travel-images/medium";

                            File file = new File(uploadPath);
                            file.mkdirs();
                            //写入文件到磁盘，该行执行完毕后，若有该临时文件，将会自动删除
                            fileItem.write(new File(uploadPath, tg + suffix));
                            ti.setPATH(tg + suffix);

                        } else {
                            String variableName = fileItem.getFieldName();
                            System.out.println(variableName + ":" + fileItem.getString("UTF-8"));
                            if ("country".equals(variableName)) {
                                ti.setCountry_RegionCodeISO(geoCountriesRegionsDao.findByCountryByName(fileItem.getString("UTF-8")).getISO());
                            } else if ("city".equals(variableName)) {
                                geocities geociti = geocitiesDao.findByAsciiName(fileItem.getString("UTF-8"));
                                ti.setCityCode(geociti.getGeoNameID());
                                ti.setLatitude(geociti.getLatitude());
                                ti.setLongitude(geociti.getLongitude());
                            } else if ("Author".equals(variableName)) {
                                ti.setAuthor(fileItem.getString("UTF-8"));
                            } else if ("Description".equals(variableName)) {
                                ti.setDescription(fileItem.getString("UTF-8"));
                            } else if ("Title".equals(variableName)) {
                                ti.setTitle(fileItem.getString("UTF-8"));
                            } else if ("Content".equals(variableName)) {
                                ti.setContent(fileItem.getString("UTF-8"));

                            }
                        }
                    }
                    //完成取值
                    imageDao.insert(ti);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                resp.sendRedirect("user?method=retrieve");
            } else if ("updatePhoto".equals(method)) {

                String suffix = "";
                String tg = "";
                try {
                    GeocitiesDao geocitiesDao = new GeocitiesDao();
                    GeoCountriesRegionsDao geoCountriesRegionsDao = new GeoCountriesRegionsDao();
                    Date DateUpdated = (new WebUtils()).getBJTime();
                    travelimage ti = imageDao.findById(Integer.parseInt(ImageID));
                    ti.setDateUpdated(DateUpdated);
                    boolean hasModifiesPhoto = false;
                    for (FileItem fileItem : list) {
                        //修改了文件
                        if (!fileItem.isFormField() && fileItem.getName() != null && !"".equals(fileItem.getName())) {
                            hasModifiesPhoto = true;
                            String filName = fileItem.getName();
                            WebUtils webUtils = new WebUtils();
                            //获取文件后缀名
                            suffix = filName.substring(filName.lastIndexOf("."));
                            tg = webUtils.tenDigitPathGetter();
                            while (true) {
                                if (imageDao.findIfPathIsFree(tg + suffix)) {
                                    break;
                                } else {
                                    tg = webUtils.tenDigitPathGetter();
                                }
                            }
                            //获取文件上传目录路径，在项目部署路径下的upload目录里。若想让浏览器不能直接访问到图片，可以放在WEB-INF下
                            String uploadPath = "/usr/local/SZYTomcat/tomcat/webapps/resources/travel-images/medium";

                            File file = new File(uploadPath);
                            file.mkdirs();
                            //写入文件到磁盘，该行执行完毕后，若有该临时文件，将会自动删除
                            fileItem.write(new File(uploadPath, tg + suffix));
                            ti.setPATH(tg + suffix);

                        } else {
                            String variableName = fileItem.getFieldName();
//                            System.out.println(variableName + ":" + fileItem.getString("UTF-8"));
                            if ("country".equals(variableName)) {
                                ti.setCountry_RegionCodeISO(geoCountriesRegionsDao.findByCountryByName(fileItem.getString("UTF-8")).getISO());
                            } else if ("city".equals(variableName)) {
                                geocities geociti = geocitiesDao.findByAsciiName(fileItem.getString("UTF-8"));
                                if (null != geociti) {
                                    ti.setCityCode(geociti.getGeoNameID());
                                    ti.setLatitude(geociti.getLatitude());
                                    ti.setLongitude(geociti.getLongitude());
                                }
                            } else if ("Author".equals(variableName)) {
                                ti.setAuthor(fileItem.getString("UTF-8"));
                            } else if ("Description".equals(variableName)) {
                                ti.setDescription(fileItem.getString("UTF-8"));
                            } else if ("Title".equals(variableName)) {
                                ti.setTitle(fileItem.getString("UTF-8"));
                            } else if ("Content".equals(variableName)) {
                                ti.setContent(fileItem.getString("UTF-8"));

                            }
                        }
                    }
                    //完成取值
                    imageDao.update(ti);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                resp.sendRedirect("user?method=retrieve");

            }
        }
    }
}
