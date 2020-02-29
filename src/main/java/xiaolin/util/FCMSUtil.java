package xiaolin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FCMSUtil {

    private static final byte[] salt = {-57, -50, -17, -40, -112, 81, -126, 13, -21, 62, -127, -92, -14, 88, -96, -114};

    public String encodePassword(String password) {
        String generatedPassword = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);

            byte[] bytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e){
          e.printStackTrace();
        }
        return generatedPassword;
    }
}
