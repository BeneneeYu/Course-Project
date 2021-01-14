package com.fudan.se.database;

import com.fudan.se.database.domain.NucleicAcidTest;
import com.fudan.se.database.repository.NucleicAcidTestRepository;
import com.fudan.se.database.request.NucleicAcidTestRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class DatabaseApplicationTests {




    @Test
    void test1(){
        ArrayList<NucleicAcidTest> acidTests = new ArrayList<>();
        for (NucleicAcidTest acidTest : acidTests) {
            Date date1 = new Date(acidTest.getTestDate().getTime()-86400000);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 设置日期格式
            String strTime = simpleDateFormat.format(date1);  // 格式转换
            System.out.println("----------------");
            System.out.println(strTime.split(" ")[0]);
            System.out.println(acidTest.getTestDate().toString().split(" ")[0]);

        }
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 设置日期格式
        String strTime = simpleDateFormat.format(today);  // 格式转换
        System.out.println(strTime);

    }
    @Test
    void kk(){

    }

}
