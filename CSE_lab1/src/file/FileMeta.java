package file;

import block.BlockImpl;
import handler.ErrorCode;
import id.Id;
import id.IntegerId;
import id.StringId;
import resources.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @program: CSE_lab1
 * @description: 文件的元数据
 * @author: Shen Zhengyu
 * @create: 2020-10-09 10:04
 **/
public class FileMeta implements Serializable {
    long fileSize;
    int blockSize = SystemProperties.BLOCK_SIZE;
    int blockCount = 0;
    Id fileManagerId;
    String path;
    Id fileId;
    HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = new HashMap<>();

    public Id getFileManagerId() {
        return fileManagerId;
    }
    public FileMeta() {
    }
    public FileMeta(Id fileId, Id fileManagerId) {
        this.fileId = fileId;
        this.fileManagerId = fileManagerId;
        this.fileSize = 0;
        this.path = setPath();
        FileUtil.createFile(path);
        try {
            write();
        } catch (ErrorCode e) {
            System.out.println("Write file error.");
            throw new ErrorCode(e.getErrorCode());
        }
        read();
    }

    public int getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }


    public String setPath() {
        String path = "";
        path = SystemProperties.FM_CWD +
                SystemProperties.PATH_SEPARATOR +
                getStringFileManagerId() +
                SystemProperties.PATH_SEPARATOR +
                getStringFileId() +
                SystemProperties.META_SUFFIX;
        return path;
    }

    public String getStringFileId() {
        if (fileId instanceof StringId) {
            StringId sid = (StringId) fileId;
            return sid.getId();
        } else {
            return "";
        }
    }

    public String getStringFileManagerId() {
        if (fileManagerId instanceof StringId) {
            StringId sid = (StringId) fileManagerId;
            return sid.getId();
        } else {
            return "";
        }
    }


    public void setFileId(Id fileId) {
        this.fileId = fileId;
    }

    public HashMap<Integer, LinkedList<BlockImpl>> getLogicBlocks() {
        return logicBlocks;
    }

    public void setLogicBlocks(HashMap<Integer, LinkedList<BlockImpl>> logicBlocks) {
        this.logicBlocks = logicBlocks;
    }

    public void write() {
        this.path = setPath();
        byte[] bytes;
        try {
            bytes = SerializeUtil.object2Bytes(this);
        } catch (Exception e) {
            System.out.println("Error occurs when casting object to byte[].");
            throw new ErrorCode(14);
        }
        try {
            FileUtil.writeFileByBytes(path, bytes);
        } catch (IOException e) {
            System.out.println("Error: FileMeta Line 124");
            throw new ErrorCode(1);
        }
    }

    public void read() {
        FileMeta deserialize = SerializeUtil.deserialize(FileMeta.class, FileUtil.readFileBytes(path));
        System.out.println(deserialize);
    }

    public void addLogicBlocks(int index, LinkedList<BlockImpl> list) {
        logicBlocks.put(index, list);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("FileMeta{" +
                "fileId = " + getStringFileId() +
                ", fileSize = " + fileSize +
                ", blockSize = " + blockSize +
                ", blockCount = " + blockCount +
                ", path = " + path +
                '}');
        str.append("\t").append("Block Information:");
        for (Integer i : logicBlocks.keySet()) {
            LinkedList<BlockImpl> block = logicBlocks.get(i);
            str.append(i).append(":");
            for (BlockImpl value : block) {
                str.append("[\"").append(value.getStringBlockManagerId()).append("\",").append(value.getIntegerBlockId()).append("]");
            }
            str.append(";");
        }

        return str.toString();
    }
}
