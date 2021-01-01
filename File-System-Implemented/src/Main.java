import file.FileImpl;
import file.FileManager;
import file.FileMeta;
import handler.*;
import id.StringId;
import resources.Buffer;
import resources.FileUtil;
import resources.SystemProperties;

import java.util.Scanner;

/**
 * @program: CSE_lab1
 * @description: 主类，运行点
 * @author: Shen Zhengyu
 * @create: 2020-10-08 20:27
 **/
public class Main {
    public static void main(String[] args) {
        int fmId1 = 1;
        int fmId2 = 3;
        int bmId1 = 2;
        int bmId2 = 5;
        int bmId3 = 8;

        try {
            System.out.println("Welcome to Smart File System.");




            Handler.clear();
//
            log("新建2个fileManager和3个blockManager");
            Handler.createFileManager(fmId1);
            Handler.createFileManager(fmId2);
            Handler.createBlockManager(bmId1);
            Handler.createBlockManager(bmId2);
            Handler.createBlockManager(bmId3);
            log("在其中一个fm中新建文件，写入abcdefg，文件命名为file1");
            Handler.createFile("file2",fmId1);
            Handler.writeFile("file2",fmId1,1,0,"ooooooppppppqqqqqqtttttt");
            Handler.catFile("file2",fmId1);
            //清空block所在的文件夹
            FileUtil.deleteDir(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + "bm" + bmId1);
            FileUtil.deleteDir(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + "bm" + bmId3);
            FileUtil.deleteDir(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + "bm" + bmId2);
            Handler.catFile("file2",fmId1);

//
//            log("修改file的block的size为3，重新运行，查看meta、data信息");
//            Handler.resetFileBlockSize("file2",fmId1,6);
//            Handler.writeFile("file2",fmId1,1,0,"ooooooppppppqqqqqqtttttt");
//            Handler.catFile("file2",fmId1);
//                        log("根据file1中的block的信息，使用smart-hex读取其中一个block的内容");
//            FileMeta fileMeta = Handler.findFile("file2",fmId1);
//            for (Integer integer : fileMeta.getLogicBlocks().keySet()) {
//                int blockNum = fileMeta.getLogicBlocks().get(integer).get(0).getIntegerBlockId();
//                Handler.readBlockInHex(String.valueOf(blockNum));
//            }
//            Handler.writeFile("file1",fmId1,1,0,"abcdefghij");
//            log("smart-cat读取file1的内容");
//            Handler.catFile("file1",fmId1);
//            log("setSize为5，读取file的大小");
//            Handler.setFileSize("file1",fmId1,5);
//            Handler.catFile("file1",fmId1);
//            Handler.setFileSize("file1",fmId1,15);
//            Handler.catFile("file1",fmId1);
//            log("在file的第2位置写入abcdefg");
//            Handler.writeFile("file1",fmId1,1,2,"xxx");
//            Handler.catFile("file1",fmId1);
//

//            log("根据file1中的block的信息，使用smart-hex读取其中一个block的内容");
//            FileMeta fileMeta = Handler.findFile("file1",fmId1);
//            for (Integer integer : fileMeta.getLogicBlocks().keySet()) {
//                int blockNum = fileMeta.getLogicBlocks().get(integer).get(0).getIntegerBlockId();
//                Handler.readBlockInHex(String.valueOf(blockNum));
//            }
//            log("修改file的block的size为3，重新运行，查看meta、data信息");
//            Handler.resetFileBlockSize("file1",fmId1,3);
//            Handler.catFile("file1",fmId1);
//            log("另一个fm新建file2，使用smart-copy，将file1的内容复制到file2");
////        Handler.createFile("file2",fmId2);
//            Handler.copyFile("file1","file2",fmId1,fmId2);
//            log("在file2的第10个位置插入opq");
//            Handler.writeFile("file2",fmId2,1,10,"opq");
//            Handler.catFile("file2",fmId2);
//            log("手动修改file1一个block data中的信息，使块出错");
//            FileMeta fmt = Handler.findFile("file2",fmId2);
//            //blockManager中第几个
//            int length1 =(int)( fmt.getBlockCount() * Math.random() + 1) - 1;
//            int length2 = 0;
////        int length2 = (int)(SystemProperties.BLOCK_SIZE* Math.random() + 1) - 1;
//            String blockManager = fmt.getLogicBlocks().get(length1).get(length2).getStringBlockManagerId();
//            int blockNum = fmt.getLogicBlocks().get(length1).get(length2).getIntegerBlockId();
//            System.out.println("准备破坏" + blockManager + ":" + blockNum);
//            FileUtil.writeFileByLine(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + blockManager + SystemProperties.PATH_SEPARATOR + blockNum + SystemProperties.DATA_SUFFIX,"1\n1\n1\n");
//            Handler.catFile("file2",fmId2);



            //因为直接破坏会影响后续，所以此方法单独测试
//            log("将file1某一个的logic block对应的所有block内容都修改");
//            int tmpSize = fmt.getLogicBlocks().get(length1).size();
//            for (int i = 0; i < tmpSize; i++) {
//                blockManager = fmt.getLogicBlocks().get(length1).get(i).getStringBlockManagerId();
//                blockNum = fmt.getLogicBlocks().get(length1).get(i).getIntegerBlockId();
//                System.out.println("准备破坏" + blockManager + ":" + blockNum);
//                FileUtil.writeFileByLine(SystemProperties.BM_CWD + SystemProperties.PATH_SEPARATOR + blockManager + SystemProperties.PATH_SEPARATOR + blockNum + SystemProperties.DATA_SUFFIX,"1\n1\n1\n");
//            }
//            Handler.catFile("file2",fmId2);




//            log("将file2setSize为7");
//            Handler.setFileSize("file2",fmId2,7);
//            Handler.catFile("file2",fmId2);
//            log("通过fileManager的getFile获取file2并且smart-cat");
//            FileManager fm = Handler.getFileManagerById(new StringId("fm-"+ fmId2) );
//            FileImpl tmpFile = (FileImpl)fm.getFile(new StringId("file2"));
//            Handler.catFile(tmpFile.getStringFileId(),fmId2);
//            log("移动file的光标位置，并且读取该光标之后的内容");
//            int offsetTo = (int)(Math.random() * tmpFile.size() + 1) - 1;
//            System.out.println("从头向后移动file2:" + offsetTo);
//            tmpFile.move(offsetTo,1);
//            Handler.readFileContent("file2",fmId2,0,0, (int) (tmpFile.size()-offsetTo));
        } catch (ErrorCode e) {
//            System.out.println(e.getMessage());
        }
    }
    private  static void log(String s){
        System.out.println("---------------------------" + s + "---------------------------");
    }
}
