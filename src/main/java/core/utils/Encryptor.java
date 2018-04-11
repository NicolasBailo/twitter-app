package core.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class Encryptor {



    public String aesEncryptor(String data, String aesKey)
             {
                 try {
                     SecretKeySpec skeySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
                     Cipher cipher = Cipher.getInstance("AES");

                     cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

                     byte[] original = Base64.encode(cipher.doFinal(data.getBytes()));
                     return new String(original);
                 } catch(Exception e){
                     e.printStackTrace();
                     return null;
                 }
    }


}