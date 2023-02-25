package space.nixus.pubtrans.component;

import java.util.Base64;
import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
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
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAPublicKey getPubKey() {
        if(publicKey==null) {
            try {
                publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(getKeySpec("pub.der", false));
            } catch(Exception ex) {
                logger.warn("Private key generation.", ex);
            }
        }
        return publicKey;
    }

    public RSAPrivateKey getPrivKey() {
        if(privateKey==null) {
            try {
                privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(getKeySpec("priv.der", true));
            }
            catch(Exception ex) {
                logger.warn("Public key generation.", ex);
            }
        }
        return privateKey;
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
            logger.error("Cryptic.getKeySpec()", ex);
        }
        return null;
    }

    public String encrypt(String plain) {
        return Base64.getEncoder().encodeToString(encrypt(plain.getBytes(StandardCharsets.UTF_8)));
    }

    public byte[] encrypt(byte[] plain) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, getPubKey());
            return cipher.doFinal(plain);
        } catch(Exception ex) {
            logger.error("Cryptic.encrypt()", ex);
        }
        return null;
    }

    public String decrypt(String encrypted) {
        var crypted = Base64.getDecoder().decode(encrypted);
        return new String(decrypt(crypted),StandardCharsets.UTF_8);
    }

    public byte[] decrypt(byte[] encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, getPrivKey());
            return cipher.doFinal(encrypted);
        } catch(Exception ex) {
            logger.error("Cryptic.decrypt()", ex);
        }
        return null;
    }
}
