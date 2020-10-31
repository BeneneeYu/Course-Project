package test;
import file.FileMeta;
import handler.ErrorCode;
import id.Id;
import id.StringId;
import org.junit.Test;
import resources.SerializeUtil;
import java.util.Arrays;
import static org.junit.Assert.*;
public class SerializeUtilTest {
    @Test
    public void object2Bytes2object() {
        try {
            Id fileId = new StringId("sadas.txt");
            Id fileManagerId = new StringId("fm-123");
            FileMeta fileMeta = new FileMeta(fileId,fileManagerId);
            byte[] bytes = SerializeUtil.object2Bytes(fileMeta);
            FileMeta fileMeta2 = SerializeUtil.deserialize(FileMeta.class,bytes);
            System.out.println(fileMeta);
            System.out.println(fileMeta2);
            assertEquals(fileMeta.getStringFileId(),fileMeta2.getStringFileId());
        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }
    }
    @Test
    public void test(){
        int[] a = {1,2,3};
        int[] b = {4,5,6,7,8,9};
        System.arraycopy(a,0,b,a.length,1);
        System.out.println(Arrays.toString(b));
    }
    @Test
    public void deserialize() {
    }
}