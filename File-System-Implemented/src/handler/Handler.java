package handler;

import block.BlockImpl;
import file.*;
import block.BlockManagerImpl;
import id.Id;
import id.IntegerId;
import id.StringId;
import resources.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @program: CSE_lab1
 * @description: 抽象父类，定义共同操作
 * @author: Shen Zhengyu
 * @create: 2020-10-08 20:36
 **/
public  class Handler {
    public static HashMap<String, FileManagerImpl> fileManagers = new HashMap<>();
    public static HashMap<String, BlockManagerImpl> blockManagers = new HashMap<>();
    public Handler(){
    }

    public void serve(String[] commands) throws Exception{}


    public static FileMeta findFile(String filename,int fileManagerId) {
        StringBuilder path = new StringBuilder();
        byte[] serializedContent;
        if(FileUtil.existsInFileManager(filename + SystemProperties.META_SUFFIX, new StringBuilder(),fileManagerId)) {
            path.append(SystemProperties.FM_CWD ).append("/fm-").append(fileManagerId).append("/").append(filename).append(SystemProperties.META_SUFFIX);
            serializedContent = FileUtil.readFileBytes(path.toString());
            return SerializeUtil.deserialize(FileMeta.class, serializedContent);
        } else {
            throw new ErrorCode(4);
        }
    }


    public static void resetFileBlockSize(String fileName,int fileManagerId,int blockSize){
        String filePath = SystemProperties.FM_CWD + "/fm-" + fileManagerId + "/" + fileName + SystemProperties.META_SUFFIX;
        LinkedList<String> strings = FileUtil.readFileByLine(filePath);
        String result = catFileInString(fileName,fileManagerId);
//        FileUtil.deleteFile(filePath);
//        FileUtil.deleteFileLine(SystemProperties.FM_CWD + "/fm-" + fileManagerId + "/" + SystemProperties.FILE_LIST_NAME,fileName);
        if(fileManagers.containsKey("fm-" + fileManagerId)){
            FileManagerImpl fileManager = fileManagers.get("fm-" + fileManagerId);
            fileManager.deleteFileByFileName(fileName);
        }
        FileUtil.deleteFile("src/resources/blockSize.txt");
        FileUtil.createFile("src/resources/blockSize.txt");
        FileUtil.writeFileByLine("src/resources/blockSize.txt",String.valueOf(blockSize));
        SystemProperties.BLOCK_SIZE = blockSize;
        createFile(fileName,fileManagerId);
        writeFile(fileName,fileManagerId,1,0,result);
    }
    public static BlockManagerImpl allocateBlockManager() {
        LinkedList<String> bms = FileUtil.readFileByLine(SystemProperties.BM_CWD + "/id.txt");
        int length = bms.size();
        if(length == 0){
            throw new ErrorCode(18);
        }
        int bmIndex = (int) (Math.random() * length + 1);
        String bmindex = bms.get(bmIndex-1);
        String sbmId = "bm-" + bmindex;
        BlockManagerImpl bm = null ;
        if(blockManagers.containsKey(sbmId)) {
            bm = blockManagers.get(sbmId);
        } else {
            bm = new BlockManagerImpl(new StringId(sbmId));
            blockManagers.put(sbmId, bm);
        }
        return bm;
    }

//    public static Id allocateFileManager() {
//        int fmIndex = (int) (Math.random() * SystemProperties.FM_COUNT + 1);
//        String sid = "fm-" + fmIndex;
//        FileManagerImpl fm;
//
//        // 查询分配的fm对象是否已创建
//        if (fileManagers.containsKey(sid)) {
//            fm = fileManagers.get(sid);
//        } else {
//            fm = new FileManagerImpl(new StringId(sid));
//            fileManagers.put(sid, fm);
//        }
//        //System.out.println("choose a fm :" + sid);
//        return new StringId(sid);
//    }
    static FileImpl getFileByFileNameAndFileManagerId(String fileName,int fileManagerId){
        FileManager fileManager = getFileManagerById(new StringId("fm-" + fileManagerId));
        return  (FileImpl)fileManager.getFile(new StringId(fileName));
    }
    public static FileManagerImpl getFileManagerById(Id fmId) {
        String sfmId;
        if (fmId instanceof StringId) {
            StringId sid = (StringId) fmId;
            sfmId = sid.getId();
        } else {
            throw new ErrorCode(13);
        }
        FileManagerImpl fm = null;

        if (fileManagers.containsKey(sfmId)) {
            fm = fileManagers.get(sfmId);

        } else {
            fm = new FileManagerImpl(fmId);
            fileManagers.put(sfmId, fm);
        }

        return fm;
    }

    //根据filename从fileList中找出fileManager
//    static String getFileManagerByFileName(String filename) {
//        LinkedList<String> list = FileUtil.readFileByLine("fm/id.txt");
//        for (String s : list) {
//            String path = SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR;
//            String fmName = "fm-";
//            fmName += s;
//            path += fmName + SystemProperties.PATH_SEPARATOR + "fileList.txt";
//            try {
//                list = FileUtil.readFileByLine(path);
//            } catch(RuntimeException e) {
//                throw new ErrorCode(1);
//            }
//            for(int k = 0; k < list.size(); k++) {
//                if(list.get(k).equals(filename)) {
//                    return fmName;
//                }
//            }
//        }
//        //文件未创建
//        throw new ErrorCode(4);
//    }

    static String getBlockManager(String blockId) {
        LinkedList<String> list = FileUtil.readFileByLine(SystemProperties.BM_CWD + "/id.txt");
        for (String s : list) {
            String path = SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR;
            String bmName = "bm-";
            bmName += s;
            path += bmName + SystemProperties.PATH_SEPARATOR + "blockList.txt";
            try {
                list = FileUtil.readFileByLine(path);
            } catch(RuntimeException e) {
                System.out.println(e.getMessage());
            }

            for(int k = 0; k < list.size(); k++) {
                if(list.get(k).equals(blockId)) {
                    return bmName;
                }
            }
        }
        throw new ErrorCode(5);
    }
    public static void createFile(String filename,int fileManagerId){
        FileManagerImpl fm;
        if(!existFileManager(fileManagerId)){
            throw new ErrorCode(17);
        }
        if(FileUtil.existsInFileManager(filename + SystemProperties.META_SUFFIX, new StringBuilder(),fileManagerId)) {
            throw new ErrorCode(3);
        } else {
            String sid = "fm-" + fileManagerId;
            fm = new FileManagerImpl(new StringId(sid));
            try {
                fm.newFile(new StringId(filename));
                fm.addFileToList(filename);
            } catch (ErrorCode e) {
                throw new ErrorCode(e.getErrorCode());
            }
        }
    }
    public static boolean existFileManager(int id){
        LinkedList<String> fileManagerIds = FileUtil.readFileByLine(SystemProperties.FILE_MANAGER_ID_PATH);
        for (String fileManagerId : fileManagerIds) {
            if(fileManagerId.equals(String.valueOf(id))){
                return true;
            }
        }
        return false;
    }
    public static void createFileManager(int id){
        LinkedList<String> fileManagerIds = FileUtil.readFileByLine(SystemProperties.FILE_MANAGER_ID_PATH);
        for (String fileManagerId : fileManagerIds) {
            if(fileManagerId.equals(String.valueOf(id))){
                throw new ErrorCode(17);
            }
            System.out.println("新建FileManager-" + id + "成功");
        }
        FileUtil.writeFileByLine(SystemProperties.FILE_MANAGER_ID_PATH,String.valueOf(id));
        String sid = "fm-" + id;
        FileManagerImpl fm = new FileManagerImpl(new StringId(sid));
        fileManagers.put(sid,fm);
        FileUtil.mkdirs(SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR + sid + SystemProperties.PATH_SEPARATOR );
    }

    public static void createBlockManager(int id){
        LinkedList<String> blockManagerIds = FileUtil.readFileByLine(SystemProperties.BLOCK_MANAGER_ID_PATH);
        for (String blockManagerId : blockManagerIds) {
            if(blockManagerId.equals(String.valueOf(id))){
                throw new ErrorCode(17);
            }
        }
        FileUtil.writeFileByLine(SystemProperties.BLOCK_MANAGER_ID_PATH,String.valueOf(id));
        String sid = "bm-" + id;
        BlockManagerImpl bm = new BlockManagerImpl(new StringId(sid));
        blockManagers.put(sid,bm);
        FileUtil.mkdirs(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + sid + SystemProperties.PATH_SEPARATOR );
        System.out.println("新建BlockManager-" + id + "成功");

    }

    public static BlockImpl getBlock(int id){
        String bmStringId;
        BlockManagerImpl bm;
        BlockImpl block;

        try {
            bmStringId = getBlockManager(String.valueOf(id));
            bm = new BlockManagerImpl(new StringId(bmStringId));
            block = (BlockImpl) bm.getBlock(new IntegerId(id));
            return block;
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }
    }
    public static  void readBlockInHex(String blockId) {
        byte[] blockData;
        String bmStringId;
        BlockManagerImpl bm;
        BlockImpl block;
        int id;

        try {
            id = Integer.parseInt(blockId);
        } catch (Exception e) {
            throw new ErrorCode(8);
        }

        try {
            bmStringId = getBlockManager(blockId);
            bm = new BlockManagerImpl(new StringId(bmStringId));
            block = (BlockImpl) bm.getBlock(new IntegerId(id));
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

        if (block.Validate()) {
            blockData = block.read();
            for (int i = 0; i < blockData.length; i++) {
                String hexS = Integer.toHexString(blockData[i] & 0xFF) + " ";
                if("0 ".equals(hexS)){
                    hexS = "0" + hexS;
                }
                System.out.print("0x" + hexS);
                if ((i + 1) % 16 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        } else {
            throw new ErrorCode(2);
        }
    }


    public static void setFileSize(String fileName,int fileManagerId,long length) {
        String fmStringId = "";
        FileManagerImpl fm;
        FileImpl file;
        long oldPointer;

        if(length < 0) {
            throw new ErrorCode(16);
        }

        try {
            file = getFileByFileNameAndFileManagerId(fileName,fileManagerId);
            oldPointer = file.pos();
            file.move(0, 1);
            file.setSize(length);
            //恢复cat之前的指针位置
            if(length < oldPointer){
                file.move(0,2);
            }else{
                file.move(oldPointer, 1);

            }
        } catch(ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

    }

    public static void copyFile(String srcFilename,String destFilename,int srcFileManagerId,int tgtFileManagerId) {
        String srcFileManagerStringId = "";
        FileManagerImpl destFileManager;
        FileImpl srcFile;
        FileImpl destFile;
        try {
            srcFile = getFileByFileNameAndFileManagerId(srcFilename,srcFileManagerId);
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

        if(!FileUtil.existsInFileManager(destFilename + SystemProperties.META_SUFFIX, new StringBuilder(),tgtFileManagerId)) {
            destFileManager = new FileManagerImpl(new StringId("fm-" + tgtFileManagerId) );
            try {
                destFile = (FileImpl) destFileManager.newFile(new StringId(destFilename));
                destFileManager.addFileToList(destFilename);
                srcFile.copy(destFile);
            } catch (ErrorCode e) {
                throw new ErrorCode(e.getErrorCode());
            }
        } else {
            throw new ErrorCode(3);
        }
    }

    public static void writeFile(String fileName,int fileManagerId,int where,long offset,String content){
        FileImpl file;
        System.out.println("The content you want to write is:" + content);
        try {
            file = getFileByFileNameAndFileManagerId(fileName,fileManagerId);
            file.move(offset, where);
            file.write((content).getBytes(StandardCharsets.UTF_8));
        } catch(ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }
    }

    public static void readFileContent(String filename,int fileManagerId,int where,long offset,int length) {
        byte[] byteContent;
        FileImpl file = null;
        String fileData = "";
        try {
            file = getFileByFileNameAndFileManagerId(filename,fileManagerId);
            file.move(0, where);
            byteContent = file.read(length);
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

        fileData = new String(byteContent, StandardCharsets.UTF_8);
        System.out.println("---------------The next line is content--------------");

        System.out.println(fileData);
    }

    public static void readFileContentBehind(String filename,int fileManagerId) {
        byte[] byteContent;
        FileImpl file = null;
        String fileData = "";
        try {
            file = getFileByFileNameAndFileManagerId(filename,fileManagerId);
            long howMany = file.size() - file.pos();
            byteContent = file.read((int) howMany);
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

        fileData = new String(byteContent, StandardCharsets.UTF_8);
        System.out.println("---------------The next line is content behind--------------");

        System.out.println(fileData);
    }
    public static void catFile(String filename,int fileManagerId) {
        readFileContentBehind(filename,fileManagerId);
        catAllContent(filename,fileManagerId);
    }

    public static void catAllContent(String filename,int fileManagerId){
        FileImpl file = null;
        String fileData = "";
        byte[] byteContent = null;
        long temp;
        file = getFileByFileNameAndFileManagerId(filename,fileManagerId);
//            System.out.println("当前pos:" + file.pos());
        temp = file.pos();
        file.move(0, 1);
        byteContent = file.read((int)file.size());
        file.move(temp, 1);

        if(null != byteContent){
            fileData = new String(byteContent, StandardCharsets.UTF_8);
            System.out.println("------------------The following is the whole content------------------");
            System.out.println(fileData);
//            readFileContentBehind(filename,fileManagerId,0,0);
        }else{
            throw new ErrorCode(15);
        }
    }

    public static String catFileInString(String filename,int fileManagerId) {
        FileImpl file = null;
        String fileData = "";
        byte[] byteContent = null;
        long temp;
        try{
            file = getFileByFileNameAndFileManagerId(filename,fileManagerId);
//            System.out.println("当前pos:" + file.pos());
            temp = file.pos();
            file.move(0, 1);
            byteContent = file.read((int)file.size());
            file.move(temp, 1);
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

        fileData = new String(byteContent, StandardCharsets.UTF_8);
        return fileData;
    }

    public static void clear(){
        FileUtil.deleteFile(SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR + "id.txt");
        FileUtil.deleteFile(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + "id.txt");
        FileUtil.deleteFile(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + "idCount.txt");
        FileUtil.deleteDir(SystemProperties.BM_CWD);
        FileUtil.deleteDir(SystemProperties.FM_CWD);
        FileUtil.createFile(SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR + "id.txt");
        FileUtil.createFile(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + "id.txt");
        FileUtil.createFile(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + "idCount.txt");
        FileUtil.writeFileByLineWithoutN(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + "idCount.txt","1");

    }
}
