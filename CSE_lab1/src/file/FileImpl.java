package file;

import block.BlockImpl;
import block.BlockManagerImpl;
import handler.ErrorCode;
import handler.Handler;
import id.Id;
import id.StringId;
import resources.Buffer;
import resources.FileUtil;
import resources.SystemProperties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @program: CSE_lab1
 * @description: 文件实现类
 * @author: Shen Zhengyu
 * @create: 2020-10-03 18:56
 **/
public class FileImpl implements File {
    FileMeta fileMeta;
    String fileData;
    Id fileId;
    long ptr = 0;
    public FileImpl(FileMeta fileMeta) {
        this.fileMeta = fileMeta;
        this.fileId = fileMeta.fileId;
    }
    @Override
    public Id getFileId() {
        return fileMeta.fileId;
    }
    public String getStringFileId(){
        if(fileId instanceof StringId) {
            StringId sid = (StringId) fileId;
            return sid.getId();
        } else {
            return "";
        }
    }
    public FileMeta getFileMeta() {
        return fileMeta;
    }
    @Override
    public FileManager getFileManager() {
        return new FileManagerImpl(fileMeta.getFileManagerId());
    }

    /**
    * @Description:
     * 先边界检查，然后从元数据处得到所有的逻辑块，拿到每一个块的多副本，利用checkSum验证
     * 不合法则丢弃，合法则取它的所有字节内容。如果副本被丢弃完了，则一个块完全损坏，报错。
     * 拿到一个块后，拼接到后。最后，将pointer开始的length字节作为结果返回，同时向后移动指针length位。
     * 最后将fileMeta重写，来更新logicBlocks的最新情况
    * @Param: [length]
    * @return: byte[]
    * @Author: Shen Zhengyu
    * @Date: 2020/10/13
    */
    @Override
    public byte[] read(int length) {
        //利用当前的block和file，实现读取
        //首先判断边界
        if(length + ptr > size()) {
            throw new ErrorCode(9);
        }
        //文件的所有字节
        byte[] total = new byte[(int)size()];
        //用户想要读取的字节
        byte[] result = new byte[length];
        HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = fileMeta.getLogicBlocks();
        for(Integer i : logicBlocks.keySet()) {
            LinkedList<BlockImpl> duplicates = logicBlocks.get(i);
            byte[] tempContent = null;
            BlockImpl block = getValidBlock(duplicates);
            String path = block.getPath() + SystemProperties.DATA_SUFFIX;
            if(Buffer.exist(path)){
                System.out.println("Buffer生效");
                tempContent = Buffer.getBlock(path);
            }else {
                tempContent = FileUtil.readFileBytes(path);
                Buffer.putBlock(path,tempContent);
            }
            //如果该logicBlock下的所有block都无效，那么为文件空洞
//            if(duplicates.size() == 0) {
//                tempContent = new byte[SystemProperties.BLOCK_SIZE];
//                try {
//                    throw new ErrorCode(16);
//                } catch(RuntimeException e) {
//                    e.printStackTrace();
//                    System.out.println(e.getMessage());
//                }
//            }
            //拼接每个block的内容
            try {
//                System.out.println("total length:" + total.length);
//                System.out.println("tempContent :" + tempContent.length);
//                System.out.println("i * SystemProperties.BLOCK_SIZE:" + i * SystemProperties.BLOCK_SIZE);
                System.arraycopy(tempContent, 0, total, i * SystemProperties.BLOCK_SIZE, tempContent.length);
            }catch (Exception e){
                e.printStackTrace();
            }

            }
        System.arraycopy(total, (int) ptr, result, 0, length);

        try {
            move(length, 0);
        } catch(ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

        try {
            fileMeta.write();
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }
        return result;
    }

    /**
    * @Description:
     * 接受一个byte数组，准备将其写入一个文件。先将pointer前的内容写入，
     * 再从上一步的末尾开始写入新内容，如果新的内容的size < （之前内容的size - pointer），
     * 那么将剩余的这部分接着写到新的block中，将之前filedata读出来，然后掐头去尾进行拼接，
     * 再写。如果pointer大于filesize，那么pointer-filesize为需要填充0x00的部分，
     * 其中第一部分为最后一个block的空白size；第二部分为整个block为空，
     * 第三部分为一个block前面一定的size为空，然后开始写内容。如果pointer小于filesize，
     * 么如果其不为blocksize的整数倍，那么从第一个block的中部开始写，先将数据读出来，
     * 从指定位置开始改变blockdata再写回；在这种情况下，如果content.size<filesize-pointer,
     * 那么还将剩余部分复制到content的末尾（相当于覆盖掉了）
    * @Param: [bytes]
    * @return: void
    * @Author: Shen Zhengyu
    * @Date: 2020/10/13
    */
    @Override
    public void write(byte[] bytes) {
        int byteLength = bytes.length;
        long fileSize = fileMeta.fileSize;
        HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = fileMeta.getLogicBlocks();
        int blockCount = fileMeta.blockCount;
        if(fileSize == 0){
            writeBlock(bytes, 0, bytes.length);
            try {
                move(byteLength, 0);
            } catch (ErrorCode e) {
                throw new ErrorCode(e.getErrorCode());
            }
        }else {
            if (ptr > fileMeta.fileSize) {
                throw new ErrorCode(6);
            } else {
                int preBlockCount = (int) (ptr / SystemProperties.BLOCK_SIZE);
                int behindBlockCount = blockCount - preBlockCount - 1;
                long newSize = 0;
                byte[] large;//容纳之前可能的残留和要输入的
                byte[] larger;
                int leftInBlock = (int) (ptr % SystemProperties.BLOCK_SIZE);
                //以下是指针指在不存在的块上的情况，即在末尾append
                if(preBlockCount >= blockCount){
                    fileSize += bytes.length;
                    writeBlock(bytes, preBlockCount, fileSize);
                }else {
                    BlockImpl blockBeforeWrite = getValidBlock(logicBlocks.get(preBlockCount));
                    byte[] temp = blockBeforeWrite.read();
                    if (leftInBlock != 0) {
                        newSize = leftInBlock + bytes.length;
                        large = new byte[(int) newSize];
                        System.arraycopy(temp, 0, large, 0, leftInBlock);
                        System.arraycopy(bytes, 0, large, leftInBlock, bytes.length);
                    } else {
                        newSize = bytes.length;
                        large = new byte[(int) newSize];
                        System.arraycopy(bytes, 0, large, 0, bytes.length);
                    }
                    larger = new byte[(int) (bytes.length + fileSize - preBlockCount*SystemProperties.BLOCK_SIZE)];
                    System.arraycopy(large, 0, larger, 0, large.length);
                    //要把原本插入块中指针的有部分也写入
                    //那一块剩了多少可写
                    int writeSize = Math.min(temp.length, SystemProperties.BLOCK_SIZE);
                    //把右边的写入
                    System.arraycopy(temp, leftInBlock, larger, large.length, (writeSize - leftInBlock));
                    int nowSize = large.length + (writeSize - leftInBlock);
                    //把之后的完整块的内容写入
                    for (int j = 1; j <= behindBlockCount; j++) {
                        byte[] newBlock = getValidBlock(logicBlocks.get(preBlockCount + j)).read();
                        writeSize = Math.min(newBlock.length, SystemProperties.BLOCK_SIZE);
                        System.arraycopy(newBlock, 0, larger, nowSize + (j - 1) * SystemProperties.BLOCK_SIZE, writeSize);
                    }
                    fileSize = larger.length + preBlockCount*SystemProperties.BLOCK_SIZE;
                    writeBlock(larger, preBlockCount, fileSize);
                }
                try {
                    move(bytes.length, 0);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
    }
    //bytes是要写在beginBlock个完整个块之后的数据
    //beginBlock是完整块数目
    //totalFileSize是文件总大小
    void writeBlock(byte[] bytes, int beginBlock, long totalFileSize) {
        BlockManagerImpl bm;

        int blockCountToAdd = (bytes.length % SystemProperties.BLOCK_SIZE) == 0
                ? (bytes.length / SystemProperties.BLOCK_SIZE)
                : (bytes.length / SystemProperties.BLOCK_SIZE) + 1;

        for(int i = 0; i < blockCountToAdd; i++) {
            LinkedList<BlockImpl> duplicates = new LinkedList<BlockImpl>();

            int begin = i * SystemProperties.BLOCK_SIZE;
            int end = (i + 1) * SystemProperties.BLOCK_SIZE;
            //System.out.println("copy begins at: " + begin + "\n ends at: " + end);

            //创建多个相同的副本
            for(int j = 0; j < SystemProperties.DUPLICATION_COUNT; j++) {
                byte[] temp;
                if(end > bytes.length) {
                    temp = Arrays.copyOfRange(bytes, begin, bytes.length);
                } else {
                    temp = Arrays.copyOfRange(bytes, begin, end);
                }
                //System.out.println("写入的字节: " + new String(temp));
                bm = Handler.allocateBlockManager();
                BlockImpl block = (BlockImpl) bm.newBlock(temp);

                duplicates.add(block);
            }
            fileMeta.addLogicBlocks(i + beginBlock, duplicates);
        }

        fileMeta.setFileSize(totalFileSize);
        fileMeta.setBlockCount((int) ((totalFileSize % SystemProperties.BLOCK_SIZE) == 0
                ? (totalFileSize / SystemProperties.BLOCK_SIZE)
                : (totalFileSize / SystemProperties.BLOCK_SIZE) + 1));
        try {
            fileMeta.write();
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

        try {
            fileMeta.read();
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

    }

    public void copy(FileImpl destFile) {
        FileMeta destFileMeta = destFile.getFileMeta();
        destFileMeta.setBlockCount(fileMeta.getBlockCount());
        destFileMeta.setFileSize(fileMeta.getFileSize());
        destFileMeta.setLogicBlocks(fileMeta.getLogicBlocks());
        try {
            destFileMeta.write();
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }
    }

    private BlockImpl getValidBlock(LinkedList<BlockImpl> duplicates){
        BlockImpl block = new BlockImpl();
        while (duplicates.size() != 0){
            block = duplicates.get(0);
            if (block.Validate()) {
                break;
            } else {
                System.out.println("error code '2' \"Inconsistent checksum value.\"");
                duplicates.remove(0);
                block = null;
            }
        }
        if(null == block){
            throw new ErrorCode(15);
        }
        return block;
    }
    @Override
    public long pos() {
        return ptr;
    }

    @Override
    public long move(long offset, int where) {
        switch(where) {
            case MOVE_HEAD:
                if(offset > size()){
                    System.out.println("向右越界,size:" + size());
                    throw new ErrorCode(11);

                }
                ptr = offset;
                System.out.println("当前ptr:" + ptr);
                break;
            case MOVE_CURR:
                if(ptr + offset > size()){
                    System.out.println("向右越界");
                    throw new ErrorCode(11);
                }
                ptr += offset;
                System.out.println("当前ptr:" + ptr);
                break;
            case MOVE_TAIL:
                if(size() - offset < 0){
                    throw new ErrorCode(11);
                }
                ptr = size() - offset;
                System.out.println("当前ptr:" + ptr);
                break;
            default:
                throw new ErrorCode(11);
        }
        if(ptr < 0) {
            throw new ErrorCode(11);
        }
        return ptr;
    }

    @Override
    public void close() {

    }

    @Override
    public long size() {
        return fileMeta.fileSize;
    }

    @Override
    public void setSize(long newSize) {
        byte[] originalBytes = read((int)fileMeta.fileSize);
        byte[] newBytes = new byte[(int) newSize];
        long originalSize = originalBytes.length;
        int dif = (int) (newSize - originalSize);
        if (dif >= 0){
            System.arraycopy(originalBytes, 0, newBytes, 0, (int) originalSize);
            for(int i = 0; i < dif; i++) {
                newBytes[(int) (originalSize + i)] = 0x00;
            }
        }else {
            System.arraycopy(originalBytes, 0, newBytes, 0, (int) newSize);
        }
        fileMeta.logicBlocks.clear();
        writeBlock(newBytes, 0, newSize);
    }
}
