import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author hua
 * @date 2019/07/27 14:41
 **/
public class MDTest {

    @Test
    public void testMd() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String text = "123456";
        byte[] bytes = md.digest(text.getBytes());
        for (byte aByte : text.getBytes()) {
            System.out.println(aByte & 255);
        }
    }
}
