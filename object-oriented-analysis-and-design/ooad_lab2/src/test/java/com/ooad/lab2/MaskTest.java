package com.ooad.lab2;

import com.alibaba.fastjson.JSON;
import com.ooad.lab2.domain.*;
import com.ooad.lab2.repository.*;
import com.ooad.lab2.service.StudentService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MaskTest {

    @Autowired
    Mask mask;

    @Autowired
    StudentService service;

    void initialize() {
        mask.initialize();
    }

    @Test
    void testOutput() {
        initialize();
        service.changeStudentMajor("18302010993","Computer Science");
        String s1 = JSON.toJSONString(mask.outputStudent("18302010993", "A"));
        outputJsonFile("./progress_report_bonus_SanZhang_1.json", s1);
        service.changeStudentMajor("18302010996","Computer Science");
        s1 = JSON.toJSONString(mask.outputStudent("18302010996", "A"));
        outputJsonFile("./progress_report_bonus_SiLi_1.json", s1);
        service.changeStudentMajor("18302010993", "Software Engineering");
        mask.initializeMajor("Network Engineering");
        service.changeStudentMajor("18302010996","Network Engineering" );
        s1 = JSON.toJSONString(mask.outputStudent("18302010993", "B"));
        outputJsonFile("./progress_report_bonus_SanZhang_2.json", s1);
        s1 = JSON.toJSONString(mask.outputStudent("18302010996", "B"));
        outputJsonFile("./progress_report_bonus_SiLi_2.json", s1);
    }

    void outputJsonFile(String filePath, String content){
        try {
            File f = new File(filePath);
            FileOutputStream fos1 = new FileOutputStream(f);
            OutputStreamWriter dos1 = new OutputStreamWriter(fos1);
            dos1.write(content);
            dos1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}