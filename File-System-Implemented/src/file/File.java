package file;

import id.Id;

public interface File {
    //文件中光标的位置、文件开头和文件结尾，其中光标的位置是File需要维护的⼀个指针，1和2给后两者赋值并无实际意义。
    int MOVE_CURR = 0;
    int MOVE_HEAD = 1;
    int MOVE_TAIL = 2;
    Id getFileId();
    FileManager getFileManager();
    byte[] read(int length);
    void write(byte[] b);
    default long pos() {
        return move(0, MOVE_CURR);
    }
    long move(long offset, int where);
    //使用buffer的同学需要实现。close方法表示的是释放资源，在该lab中如果使用了buffer则需要释放资源，不使用buffer的情
    //况下直接实现为空方法即可。
    void close();
    long size();
    void setSize(long newSize);
}
