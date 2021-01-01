package resources;

import handler.ErrorCode;

import java.io.*;

/**
 * @program: CSE_lab1
 * @description: 实现序列化
 * @author: Shen Zhengyu
 * @create: 2020-10-09 20:28
 **/
public class SerializeUtil {
    /**
    * @Description: 将对象序列化输出
    * @Param: [out]
    * @return: byte[]
    * @Author: Shen Zhengyu
    * @Date: 2020/10/9
    */
    public static byte[] object2Bytes(Object out){
        byte[] bytes = new byte[]{};
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(out);
            bytes = baos.toByteArray();
            oos.flush();
            oos.close();
            return bytes;
        } catch (IOException e) {
            throw new ErrorCode(1);
        } }


    /**
    * @Description: 利用反射，反序列化字节数组到对象
    * @Param: [clazz, content]
    * @return: T
    * @Author: Shen Zhengyu
    * @Date: 2020/10/9
    */
    public static <T> T deserialize(Class<T> clazz, byte[] content) {

        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(content)));
            Object obj = ois.readObject();
            return (T)obj;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error occurs when casting byte[] to (T)obj.");
            throw new ErrorCode(14);
        }
//        ByteArrayInputStream byteArrayInputStream = null;
//        try {
//            //将 得到的序列化字节输入缓冲区
//            byteArrayInputStream = new ByteArrayInputStream(content);
//            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            return (T) objectInputStream.readObject();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new ErrorCode(14);
//        }
    }
}
