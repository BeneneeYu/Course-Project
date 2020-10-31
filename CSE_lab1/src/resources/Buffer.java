package resources;
import block.BlockImpl;
import handler.ErrorCode;
import handler.Handler;

import java.util.HashMap;
/**
 * @program: CSE_lab1
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-10-16 10:18
 **/
public class Buffer {
    public static HashMap<String, byte[]> blockBuffer = new HashMap<>();
    public static void putBlock(String path, byte[] content) {
        blockBuffer.put(path, content);
    }
    public static byte[] getBlock(String path) {


        if (blockBuffer.containsKey(path)) {

                return blockBuffer.get(path);

        } else {
            throw new ErrorCode(4);
        }
    }
    public static boolean exist(String path) {
        String[] a = path.split("/");
        String tmp = a[a.length-1];
        BlockImpl block = Handler.getBlock(Integer.parseInt(tmp.replace(".data","")));
        return block.Validate() && blockBuffer.containsKey(path);
    }
}
