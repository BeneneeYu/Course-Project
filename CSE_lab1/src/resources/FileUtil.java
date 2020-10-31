package resources;

import handler.ErrorCode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

/**
 * @program: CSE_lab1
 * @description: 处理工具类
 * @author: Shen Zhengyu
 * @create: 2020-10-03 19:01
 **/
public class FileUtil {


    /**
     * @Description: 创建文件
     * @Param: [path]
     * @return: int, 不同状态码代表不同创建结果
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static int createFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            //重复创建
            return -1;
        } else {
            //未以文件结尾，抛出异常
            if (path.endsWith(File.separator)) {
                throw new ErrorCode(1);
            }
            //父目录不存在
            if (!file.getParentFile().exists()) {
                //尝试创建文件夹
                if (!file.getParentFile().mkdirs()) {
                    throw new ErrorCode(1);
                }
            }
            try {
                if (file.createNewFile()) {
                    //创建文件成功，返回1
                    return 1;
                }
            } catch (IOException e) {
                throw new ErrorCode(1);
            }
        }
        //创建失败，返回0
        return 0;
    }
    public static void mkdirs(String path) {
        mkdirs(new File(path));
    }
    public static void mkdirs(File file) {
        if (file.exists() && file.isDirectory()) {
            return;
        }
        if (file.exists()) {
            file.delete();
            file.mkdirs();
        } else {
            file.mkdirs();
        }
    }
    /**
     * @Description: 删除文件
     * @Param: [path]
     * @return: void
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (!file.delete()) {
                throw new ErrorCode(1);
            }
        } else {
            throw new ErrorCode(4);
        }
    }

    /**
     * @Description: 删除指定的内容行
     * @Param: [path, line]
     * @return: void
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static void deleteFileLine(String path, String line) {
        File file = new File(path);
        FileWriter writer = null;
        BufferedReader br = null;
        StringBuilder temp = new StringBuilder();
        String tempLine;
        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                br = new BufferedReader(fr);
                while (br.ready()) {
                    tempLine = br.readLine();
                    //如果相同，则不append，达到删除效果
                    if (!tempLine.equals(line)) {
                        temp.append(tempLine).append("\n");
                    }
                }
                br.close();
                writer = new FileWriter(file);
                writer.write(temp.toString());
                writer.close();
            } catch (Exception e) {
                throw new ErrorCode(1);
            }
        } else {
            throw new ErrorCode(4);
        }
    }

    /**
     * @Description: 用于读取文件内容
     * @Param: [path]
     * @return: java.lang.String
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static String readFile(String path) {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null) {
                stringBuilder.append(tmp);
                stringBuilder.append("\n");
            }
            stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
            bufferedReader.close();
        } catch (Exception e) {
            System.out.println(ErrorCode.getErrorText(1));
        }
        return stringBuilder.toString();
    }

    public static String readFirstLine(String path) {
        File file = new File(path);
        String str = "";
        if(!file.exists()) {
            System.out.println("不存在");
        }
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            // 按行读取字符串
            str = bf.readLine();
            bf.close();
            fr.close();
        } catch (IOException e) {
            throw new ErrorCode(1);
        }
        System.out.println(str);
        return str;
    }
    //以链表形式返回各行内容字符串形式
    public static LinkedList<String> readFileByLine(String path) {
        LinkedList<String> fileList = new LinkedList<String>();
        File file = new File(path);
        if(!file.exists()) {
            return fileList;
        }
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                fileList.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            throw new ErrorCode(1);
        }

        return fileList;
    }

    /**
     * @Description: 获得文件的字节数组形式
     * @Param: [path]
     * @return: byte[]
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static byte[] readFileBytes(String targetFilename) {
        //创建源与目的地
        File src = new File(targetFilename);
        byte[] dest = null;
        //选择流
        InputStream is = null;
        ByteArrayOutputStream baos= null;
        //操作(输入操作)
        try {
            //文件输入流
            is = new FileInputStream(src);
            //字节输出流，不需要指定文件，内存中存在
            baos = new ByteArrayOutputStream();
            byte[] flush = new byte[1024 * 10];
            int len = -1;
            while((len = is.read(flush)) != -1) {
                baos.write(flush,0,len);
            }
            baos.flush();
            dest = baos.toByteArray();
            return dest;
        } catch (FileNotFoundException e) {
            throw new ErrorCode(15);
        } catch (IOException e) {

            throw new ErrorCode(1);
        } finally {
            //释放资源,文件需要关闭,字节数组流无需关闭
            if(null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new ErrorCode(1);
                }
            }

        }
    }

    /**
     * @Description: 将字节流写入文件
     * @Param: [path, contents]
     * @return: void
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static void writeFile(String path, String contents) throws IOException {
        createFile(path);
        File file = new File(path);
        BufferedOutputStream bufferedOutputStream;
        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        bufferedOutputStream.write(contents.getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    public static void writeFileByBytes(String path,byte[] bytes) throws IOException {
        createFile(path);
        File dest = new File(path);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new ByteArrayInputStream(bytes);
            os = new FileOutputStream(dest);

            byte[] flush = new byte[1024*10];
            int len = -1;
            while((len = is.read(flush)) != -1) {
                os.write(flush,0,len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            throw new ErrorCode(4);
        } catch (IOException e) {
            throw new ErrorCode(1);
        }finally {
            //关闭文件流
            if(null != os) {
                try {
                    os.close();
                } catch (IOException e) {

                    throw new ErrorCode(1);
                }
            }
        }
    }

    /**
     * @Description: 逐行写入
     * @Param: [path, content]
     * @return: void
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static void writeFileByLine(String path, String content) {
        try {
            createFile(path);
            FileWriter writer = new FileWriter(path, true);
            writer.write(content + "\n");
            writer.close();
        } catch (IOException e) {
            throw new ErrorCode(1);
        }

    }
    public static void writeFileByLineWithoutN(String path, String content) {
        try {
            createFile(path);
            FileWriter writer = new FileWriter(path, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new ErrorCode(1);
        }

    }

    public static int readIdCount() throws IOException {
        File file = new File(SystemProperties.ID_COUNT_PATH);
        int ch;
        String temp = "";
        if (file.exists()) {
            FileReader input = new FileReader(file);
            while ((ch = input.read()) != -1) {
                temp += (char) ch;
            }
            input.close();

            return Integer.parseInt(temp);
        } else {
            updateIdCount(1);
            return 1;
        }
    }

    public static int updateIdCount(int newValue) throws IOException {
        File file = new File(SystemProperties.ID_COUNT_PATH);
        FileWriter output;
        if (!file.exists()) {

            if (!file.getParentFile().exists()) {
                // if the parent dir is not exist, then create it.
                if (!file.getParentFile().mkdirs()) {
                    throw new ErrorCode(1);
                }
            }
            // create target file.
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new ErrorCode(1);
            }
        }
        output = new FileWriter(file);
        output.write(newValue + "");
        output.flush();
        output.close();
        return 0;
    }

    /**
     * @Description: 判断文件是否存在（基于实际路径）
     * @Param: [path]
     * @return: boolean
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean exists(String target, StringBuilder path) {
        //String target = filename + FileConstant.META_SUFFIX;
        //System.out.println(target);
        File fmFolder = new File(SystemProperties.FM_CWD);
        if (!fmFolder.exists()) {
            new File(SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR).mkdirs();
        }
        File[] files = fmFolder.listFiles();
        if (null != files) {
            for (File f : files) {                //遍历File[]数组
                if (f.isDirectory()) {
                    File[] subFiles = f.listFiles();
                    if (null != subFiles) {
                        for (File sf : subFiles) {

                            String tempFile = sf.getName();
                            //System.out.println(tempFile);
                            if (tempFile.equals(target)) {
                                path.append(sf);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean existsInFileManager(String target, StringBuilder path,int fileManagerId) {
        //String target = filename + FileConstant.META_SUFFIX;
        //System.out.println(target);
        File fmFolder = new File(SystemProperties.FM_CWD );
        if (!fmFolder.exists()) {
            new File(SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR).mkdirs();
        }
        File[] files = fmFolder.listFiles();
        if (null != files) {
            for (File f : files) {                //遍历File[]数组
                if (f.isDirectory() && f.getName().equals("fm-" + String.valueOf(fileManagerId))) {
                    File[] subFiles = f.listFiles();
                    if (null != subFiles) {
                        for (File sf : subFiles) {

                            String tempFile = sf.getName();
                            //System.out.println(tempFile);
                            if (tempFile.equals(target)) {
                                path.append(sf);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean existsInFileManager(String target, StringBuilder path,String fileManagerStringId) {
        //String target = filename + FileConstant.META_SUFFIX;
        //System.out.println(target);
        File fmFolder = new File(SystemProperties.FM_CWD );
        if (!fmFolder.exists()) {
            new File(SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR).mkdirs();
        }
        File[] files = fmFolder.listFiles();
        if (null != files) {
            for (File f : files) {                //遍历File[]数组
                if (f.isDirectory() && f.getName().equals(fileManagerStringId)) {
                    File[] subFiles = f.listFiles();
                    if (null != subFiles) {
                        for (File sf : subFiles) {

                            String tempFile = sf.getName();
                            //System.out.println(tempFile);
                            if (tempFile.equals(target)) {
                                path.append(sf);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * @Description: 通过算法名和文件字节数组来获得checksum
     * @Param: [bytes, checkSumAlgorithm]
     * @return: java.lang.String
     * @Author: Shen Zhengyu
     * @Date: 2020/10/3
     */
    public static String getChecksum(byte[] bytes) {
        try {
            String checkSumAlgorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(checkSumAlgorithm);
            messageDigest.update(bytes);
            byte[] digestBytes = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digestBytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ErrorCode(1);
        }

    }

    public static boolean deleteDir(String path){
        File file = new File(path);
        if(!file.exists()){//判断是否待删除目录是否存在
            System.err.println("The dir are not exists!");
            return false;
        }

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
            File temp = new File(path, name);
            if(temp.isDirectory()){//判断是否是目录
                deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            }else{
                if(!temp.delete()){//直接删除文件
                    System.err.println("Failed to delete " + name);
                }
            }
        }
        return true;
    }
}
