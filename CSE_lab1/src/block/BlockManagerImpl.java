package block;

import handler.ErrorCode;
import id.Id;
import id.IntegerId;
import id.StringId;
import resources.FileUtil;
import resources.SerializeUtil;
import resources.SystemProperties;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @program: CSE_lab1
 * @description: 块管理实现类
 * @author: Shen Zhengyu
 * @create: 2020-10-03 18:54
 **/
public class BlockManagerImpl implements BlockManager, Serializable {
    Id blockManagerId;
    HashMap<Id, BlockImpl> blocks = new HashMap<Id, BlockImpl>();

    public BlockManagerImpl(Id blockManagerId) {
        this.blockManagerId = blockManagerId;
    }

    //根据指定id，获取block
    @Override
    public Block getBlock(Id indexId) {
        int id;
        BlockMeta blockMeta;
        BlockImpl block;
        byte[] blockData;
        if (indexId instanceof IntegerId) {
            IntegerId iid = (IntegerId) indexId;
            id = iid.getId();
        } else {
            throw new ErrorCode(13);
        }
        try {
            blockData = getBlockData(id);
            blockMeta = getBlockMeta(id);
        } catch (RuntimeException e) {
            throw new ErrorCode(15);
        }
        block = new BlockImpl(indexId, blockManagerId, blockData, blockMeta);

        return block;
    }

    public String getStringBlockManagerId() {
        if (blockManagerId instanceof StringId) {
            StringId sid = (StringId) blockManagerId;
            return sid.getId();
        } else {
            return "";
        }
    }

    //利用字节数据生成新的块
    @Override
    public Block newBlock(byte[] b) {
        //count读自文件，表示总共的块count数目
        int count;
        BlockImpl block;
        try {
            count = FileUtil.readIdCount();
            //新的块，没有元数据
            block = new BlockImpl(new IntegerId(count), blockManagerId, b, null);
            block.write();
            blocks.put(new IntegerId(count), block);
            addBlockToList(count);
            FileUtil.updateIdCount(++count);
        } catch (IOException e1) {
            throw new ErrorCode(1);
        }
        return block;
    }

    @Override
    public Block newEmptyBlock(int blockSize) {
        return null;
    }


    private void addBlockToList(int blockId) {
        //在每个manager文件夹下维护记录块号的文件
        String path = SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + getStringBlockManagerId()
                + SystemProperties.PATH_SEPARATOR + "blockList.txt";
        try {
            FileUtil.writeFileByLine(path, blockId + "");
        } catch (RuntimeException e) {
            throw new ErrorCode(1);
        }
    }

    //在本blockManager下，拿到指定id块数据
    private byte[] getBlockData(int id) {
        String path = SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR
                + getStringBlockManagerId()
                + SystemProperties.PATH_SEPARATOR + id + SystemProperties.DATA_SUFFIX;
        if (FileUtil.exists(path)) {
            return FileUtil.readFileBytes(path);
        } else {
            throw new ErrorCode(4);
        }
    }

    //利用id找到meta文件，再反序列化成对象
    private BlockMeta getBlockMeta(int id) {
        byte[] bytes;
        BlockMeta blockMeta;

        String path = SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR
                + getStringBlockManagerId()
                + SystemProperties.PATH_SEPARATOR + id + SystemProperties.META_SUFFIX;

        if (FileUtil.exists(path)) {
            bytes = FileUtil.readFileBytes(path);
            blockMeta = SerializeUtil.deserialize(BlockMeta.class, bytes);
            return blockMeta;
        } else {
            throw new ErrorCode(4);
        }
    }
}
