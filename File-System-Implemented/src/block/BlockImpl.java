package block;

import handler.ErrorCode;
import id.Id;
import id.IntegerId;
import id.StringId;
import resources.FileUtil;
import resources.SystemProperties;

import java.io.IOException;
import java.io.Serializable;

/**
 * @program: CSE_lab1
 * @description: Block接口的实现类
 * @author: Shen Zhengyu
 * @create: 2020-10-03 18:53
 **/
public class BlockImpl implements Block, Serializable {
    Id blockId;
    Id blockManagerId;
    BlockMeta blockMeta;
    byte[] blockData;
    String path;
    public BlockImpl(){}

    public BlockImpl(Id blockId, Id blockManagerId,  byte[] blockData,BlockMeta blockMeta) {
        this.blockId = blockId;
        this.blockManagerId = blockManagerId;
        if(null != blockMeta) {
            this.blockMeta = blockMeta;
        }else{
            this.blockMeta = new BlockMeta(FileUtil.getChecksum(blockData));
        }
        this.blockData = blockData;
        //path不带后缀
        this.path = SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + getStringBlockManagerId() + SystemProperties.PATH_SEPARATOR + getIntegerBlockId();
    }
    //封装两部分write
    public void write(){
        writeBlockData();
        writeBlockMeta();
    }
    void writeBlockData() {
        try {
            //将block的数据写入
            FileUtil.writeFileByBytes(path + SystemProperties.DATA_SUFFIX,blockData);
        } catch (IOException e) {
            throw new ErrorCode(1);
        }
    }
    void writeBlockMeta() {
        try {
            //将block的meta文件写入
            blockMeta.write(path + SystemProperties.META_SUFFIX);
        } catch (Exception e) {
            throw new ErrorCode(1);
        }
    }
    public Integer getIntegerBlockId() {
        if(blockId instanceof IntegerId) {
            IntegerId sid = (IntegerId) blockId;
            return sid.getId();
        } else {
            return 0;
        }
    }
    public String getStringBlockManagerId() {
        if(blockManagerId instanceof StringId) {
            StringId sid = (StringId) blockManagerId;
            return sid.getId();
        } else {
            return "";
        }
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Id getIndexId() {
        return blockId;
    }

    @Override
    public BlockManager getBlockManager() {
        return new BlockManagerImpl(blockManagerId);
    }

    @Override
    public byte[] read() {
        return FileUtil.readFileBytes(path + SystemProperties.DATA_SUFFIX);
    }

    @Override
    public int blockSize() {
        return SystemProperties.BLOCK_SIZE;
    }

    public String getCheckSum(){
        return  blockMeta.getCheckSum();
    }
    /**
    * @Description: 利用checksum验证文件完整性
    * @Param: []
    * @return: boolean
    * @Author: Shen Zhengyu
    * @Date: 2020/10/9
    */
    public boolean Validate(){
        //path不带后缀格式
        String dataPath = path + SystemProperties.DATA_SUFFIX;
        String metaPath = path + SystemProperties.META_SUFFIX;
        //检查元数据是否存在
        if(!FileUtil.exists(metaPath)) {
            return false;
//            throw new ErrorCode(15);
        }
        byte[] contents = null;
        try{
            contents = FileUtil.readFileBytes(dataPath);
        }catch(RuntimeException e) {
            return false;
//            throw new ErrorCode(15);
        }
        //开始校验
        return blockMeta.getCheckSum().equals(FileUtil.getChecksum(contents));
    }
}
