package socket;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {

    private static final byte[] AES_key = "hesloheslohesloh".getBytes();//musi byt 16 byte
    private SecretKey AES_secret_key;
    private Cipher cipher;

    public AES(){
        try {

            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            AES_secret_key =
                    new SecretKeySpec(AES_key, "AES");

        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public String encrypt(String data){

        String b64 = null;
        byte[] msg_byte = data.getBytes();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.AES_secret_key);
            msg_byte = cipher.doFinal(msg_byte);
            b64 = Base64.getEncoder().encodeToString(msg_byte);

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return b64;
    }

    public String decrypt(String data){

        String decoded_string = null;
        byte[] enc_byte = Base64.getDecoder().decode(data.getBytes());
        byte[] dec_byte;

        try {

            cipher.init(Cipher.DECRYPT_MODE, this.AES_secret_key);
            dec_byte = cipher.doFinal(enc_byte);
            decoded_string = new String(dec_byte);

        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return decoded_string;
    }
}
