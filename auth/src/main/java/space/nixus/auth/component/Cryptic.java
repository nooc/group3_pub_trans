package space.nixus.auth.component;

import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class Cryptic {
    
    @Autowired
    private Logger logger;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Cryptic() {
        try {
            var factory = KeyFactory.getInstance("RSA");
            this.privateKey = factory.generatePrivate(getKeySpec("priv.der", true));
            this.publicKey = factory.generatePublic(getKeySpec("pub.der", false));
        } catch (Exception e) {
            this.publicKey = null;
            this.privateKey = null;
        }
    }
        
    private KeySpec getKeySpec(String resourceName, boolean pk8) {
        try {
            var file = ResourceUtils.getFile("classpath:" + resourceName);
            try (var strm = new FileInputStream(file)) {
                var bytes = strm.readAllBytes();
                KeySpec spec;
                if (pk8)
                    spec = new PKCS8EncodedKeySpec(bytes, "RSA");
                else
                    spec = new X509EncodedKeySpec(bytes, "RSA");
                return spec;
            }
        } catch (Exception ex) {
            logger.error("getKeySpec", ex);
            ex.printStackTrace();
        }
        return null;
    }

    public RSAPublicKey getPubKey() {
        return (RSAPublicKey)publicKey;
    }

    public RSAPrivateKey getPrivKey() {
        return (RSAPrivateKey)privateKey;
    }

    public String encryptToString(String plain) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(encrypt(plain.getBytes()));
    }

    public byte[] encrypt(byte[] plain) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plain);
    }

    public String decryptToString(String enctypted) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        var crypted = Base64.getDecoder().decode(enctypted);
        return new String(decrypt(crypted));
    }

    public byte[] decrypt(byte[] enctypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(enctypted);
    }
}
