package fudan.se.lab2.controller;

import fudan.se.lab2.repository.ArticleRepository;
import org.hibernate.annotations.Parameter;	
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-04-30 17:03
 **/
@CrossOrigin()
@Controller
public class PdfController {
    Logger logger = LoggerFactory.getLogger(PdfController.class);

    @Autowired
    ArticleRepository articleRepository;



    @RequestMapping(value = "/preview/{conferenceID}/{title}", method = RequestMethod.GET)
    public void pdfStreamHandler(@PathVariable("conferenceID") Long conferenceID,@PathVariable("title") String title, HttpServletRequest request, HttpServletResponse response) {
        //PDF文件地址
        String fileName = articleRepository.findByTitleAndConferenceID(title,conferenceID).getFilename();
        logger.info("文件名:" + fileName);
        StringBuilder sb = new StringBuilder("/workplace/upload/");
        if (null == conferenceID){
            sb.append("unknownConferenceID/");
        }else{
            sb.append(conferenceID);
            sb.append("/");
        }
        sb.append(fileName);

        File file = new File(sb.toString());
        if (file.exists()) {
            byte[] data = null;
            FileInputStream input=null;
            try {
                input= new FileInputStream(file);
                data = new byte[input.available()];
                int btes = input.read(data);
                response.getOutputStream().write(data);
            } catch (Exception e) {
                System.out.println("pdf文件处理异常：" + e);
            }finally{
                try {
                    if(input!=null){
                        input.close();
                    }
                } catch (IOException e) {
                    logger.debug("失败");
                }
            }
        }
    }
}
