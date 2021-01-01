package resources;

/**
 * @program: CSE_lab1
 * @description: 各种常量配置，实现解耦
 * @author: Shen Zhengyu
 * @create: 2020-10-08 20:43
 **/
public class SystemProperties {
    public static final String CWD = "data/";
    //fileManager维护文件List，是它管理的文件
    public static final String FM_CWD = "data/fm";
    public static final String BM_CWD = "data/bm";
    //将BlockMeta，FileMeta序列化后存入文本
    public static final String META_SUFFIX = ".meta";
    public static final String DATA_SUFFIX = ".data";
    //适应linux和windows不同的文件系统
    public static final String PATH_SEPARATOR = "/";
    //灵活的块大小
    static String p = FileUtil.readFirstLine("src/resources/blockSize.txt");
    public static int BLOCK_SIZE = Integer.parseInt(p);
    //存放维护id数目文件的地址，代替数据库自增条目功能
    public static final String ID_COUNT_PATH = "data/bm/idCount.txt";
    public static final String BLOCK_MANAGER_ID_PATH = "data/bm/id.txt";
    public static final String FILE_MANAGER_ID_PATH = "data/fm/id.txt";

    public static final String FILE_LIST_NAME = "fileList.txt";
    public static final int DUPLICATION_COUNT = 3;
}
