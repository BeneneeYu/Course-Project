package block;

import handler.ErrorCode;
import resources.FileUtil;
import resources.SerializeUtil;
import resources.SystemProperties;

import java.io.Serializable;

/**
 * @program: CSE_lab1
 * @description: 记录块的元信息
 * @author: Shen Zhengyu
 * @create: 2020-10-09 19:13
 **/
public class BlockMeta implements Serializable {
    String checkSum;
    int blockSize = SystemProperties.BLOCK_SIZE;
    public BlockMeta(String checkSum) {
        this.checkSum = checkSum;
    }
    public String getCheckSum() {
        return checkSum;
    }
    public void write(String path) throws Exception {
        byte[] bytes = SerializeUtil.object2Bytes(this);
        try {
            FileUtil.writeFileByBytes(path, bytes);
        } catch (RuntimeException e) {
            throw new ErrorCode(1);
        }
    }

}
