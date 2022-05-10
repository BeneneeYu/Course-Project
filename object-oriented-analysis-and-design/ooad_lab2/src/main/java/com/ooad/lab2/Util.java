package com.ooad.lab2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @program: lab2
 * @description:
 * @author: Shen Zhengyu
 * @create: 2022-01-07 21:43
 **/
public class Util {
    public List<String> readFile(String filePath) {
        File file = new File(filePath);
        List<String> fileLines = new ArrayList<>();
        if (!file.exists()) {
            return null;
        }

        FileInputStream fileInputStream = null;
        Scanner scanner = null;
        try {
            fileInputStream = new FileInputStream(file);
            scanner = new Scanner(fileInputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                fileLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (scanner != null) {
                scanner.close();
            }
        }
        return fileLines;
    }

    public String[] splitStringByComma(String str){
        return str.trim().split(",");
    }

}
