package test;
import org.junit.Test;
import resources.FileUtil;
import java.nio.file.Files;
import java.nio.file.Paths;
public class FileUtilTest {
    /**
     * Method: getChecksum(byte[] bytes)
     */
    @Test
    public void testGetChecksum() throws Exception {
        String checksum1 = FileUtil.getChecksum(Files.readAllBytes(Paths.get("D:\\Programming\\JAVA_programming\\CSE_lab1\\src\\test\\resources\\1.txt")));
        String checksum2 = FileUtil.getChecksum(Files.readAllBytes(Paths.get("D:\\Programming\\JAVA_programming\\CSE_lab1\\src\\test\\resources\\1.txt")));
        String checksum3 = FileUtil.getChecksum(Files.readAllBytes(Paths.get("D:\\Programming\\JAVA_programming\\CSE_lab1\\src\\test\\resources\\2.txt")));
        assert (checksum1.equals(checksum2));
        assert (!checksum1.equals(checksum3));
    }

    @Test
    public void testStringBuilder(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        System.out.println(sb.length());
    }
} 
