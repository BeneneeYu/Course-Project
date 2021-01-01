package BPNetwork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @program: IntelligentSystem
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-10-28 18:50
 **/
public class FileUtil {

    public static boolean deleteFile(String sPath) {
        boolean flag;
        File file;
        flag = false;
        file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    public static void createFile(String path) {
        deleteFile(path);
        File file = new File(path);
            //父目录不存在
            try {
                if(file.createNewFile()){
                    System.out.println("创建成功");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //创建失败，返回0

    public static void writeFileByLine(String path, String content) {
        try {
            FileWriter writer = new FileWriter(path, true);
            writer.write(content + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void writeFileByLineWithoutGangN(String path, String content) {
        try {
            FileWriter writer = new FileWriter(path, true);
            writer.write(content + "");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
