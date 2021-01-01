package file;

import handler.ErrorCode;
import id.Id;
import id.StringId;
import resources.FileUtil;
import resources.SerializeUtil;
import resources.SystemProperties;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @program: CSE_lab1
 * @description: 文件管理实现类
 * @author: Shen Zhengyu
 * @create: 2020-10-03 18:57
 **/
public class FileManagerImpl implements FileManager, Serializable {
    Id fileManagerId;
    public HashMap<String, FileImpl> files = new HashMap<>();

    public FileManagerImpl(Id fileManagerId) {
        this.fileManagerId = fileManagerId;
    }

    public String getStringFileManagerId() {
        if (fileManagerId instanceof StringId) {
            StringId sid = (StringId) fileManagerId;
            return sid.getId();
        } else {
            return "";
        }
    }

    @Override
    public File getFile(Id fileId) {
        String id;
        FileMeta fileMeta;
        if (fileId instanceof StringId) {
            StringId sid = (StringId) fileId;
            id = sid.getId();

        } else {
            throw new ErrorCode(13);
        }

        if (files.containsKey(id)) {
            System.out.println(files.get(id).fileMeta);
            return files.get(id);
        } else {
            try {
                fileMeta = getFileMeta(id);
                System.out.println(fileMeta);
            } catch (RuntimeException e) {
                throw new ErrorCode(12);
            }

            FileImpl file = new FileImpl(fileMeta);
            files.put(id, file);
            return file;
        }
    }

    FileMeta getFileMeta(String filename) {
        byte[] serializedContent;
        String path = SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR + getStringFileManagerId()
                + SystemProperties.PATH_SEPARATOR + filename + SystemProperties.META_SUFFIX;
        if (FileUtil.exists(path)) {
            serializedContent = FileUtil.readFileBytes(path);
            FileMeta fileMeta = SerializeUtil.deserialize(FileMeta.class, serializedContent);
            return fileMeta;
        } else {
            throw new ErrorCode(4);
        }
    }

    public void addFileToList(String filename) {
        String path = SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR + getStringFileManagerId()
                + SystemProperties.PATH_SEPARATOR + SystemProperties.FILE_LIST_NAME;
        try {
            FileUtil.writeFileByLine(path, filename);
        } catch (ErrorCode e) {
            System.out.println("Error occur when adding file to list.");
            throw new ErrorCode(e.getErrorCode());
        }
    }

    private void deleteFromList(String filename) {
        String path = SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR + getStringFileManagerId()
                + SystemProperties.PATH_SEPARATOR + SystemProperties.FILE_LIST_NAME;
        try {
            FileUtil.deleteFileLine(path, filename);
        } catch (ErrorCode e) {
            throw new ErrorCode(4);
        }
    }

    private void deleteFileMeta(String filename) {

        String path = SystemProperties.FM_CWD + SystemProperties.PATH_SEPARATOR + getStringFileManagerId()
                + SystemProperties.PATH_SEPARATOR + filename + SystemProperties.META_SUFFIX;
        try {
            FileUtil.deleteFile(path);
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }

    }

    @Override
    public File newFile(Id fileId) {
        String id;
        StringId sid = (StringId) fileId;
        id = sid.getId();
        FileMeta fileMeta = new FileMeta(fileId, fileManagerId);
        FileImpl file = new FileImpl(fileMeta);//在这里维护一个hashmap 保存所有已经创建对象的File
        files.put(id, file);
        //fm.write(destFilename);
        //fileId即为输入要创建的file的名字 由于没有目录结构，所以不允许同名 fileId是唯一的
        // 1. 检查是否有名为fileId的文件创建了 用FileUtil查询（上层检查？）
        // 2. 根据fmId在对应目录下创建对应的meta文件
        return file;
    }

    public String deleteFileByFileName(String filename) {
        //1. 从fileSet中移除file
        //2. 删除fileList的索引
        //3. 删除对应的fileMeta文件
        String result = "";
        try {
            files.remove(filename);
            deleteFromList(filename);
            deleteFileMeta(filename);

            result = "success in deleting file: " + filename;
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }
        return result;
    }
    public String deleteFile(FileImpl file) {
        //1. 从fileSet中移除file
        //2. 删除fileList的索引
        //3. 删除对应的fileMeta文件
        String result = "";
        String filename = file.getStringFileId();
        try {
            files.remove(filename);
            deleteFromList(filename);
            deleteFileMeta(filename);

            result = "success in deleting file: " + filename;
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }
        return result;
    }
}
