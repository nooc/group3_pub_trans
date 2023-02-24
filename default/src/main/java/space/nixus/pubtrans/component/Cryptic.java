package space.nixus.pubtrans.component;

import java.util.Base64;
import java.util.Base64.*;
import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
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

    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final Encoder e64;
    private final Decoder d64;

    public Cryptic() {
        PublicKey _publicKey = null;
        PrivateKey _privateKey = null;
        try {
            var factory = KeyFactory.getInstance("RSA");
            try { _privateKey = factory.generatePrivate(getKeySpec("priv.der", true)); }
            catch(Exception ex) { logger.warn("Private key generation.", ex); }
            try { _publicKey = factory.generatePublic(getKeySpec("pub.der", false)); }
            catch(Exception ex) { logger.warn("Public key generation.", ex); }
        } catch(Exception ex) { logger.error("KeyFactory instanciation.", ex); }
        this.privateKey = _privateKey;
        this.publicKey = _publicKey;
        this.e64 = Base64.getEncoder();
        this.d64 = Base64.getDecoder();
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

    public RSAPublicKey getPubKey() {
        return (RSAPublicKey)publicKey;
    }

    public RSAPrivateKey getPrivKey() {
        return (RSAPrivateKey)privateKey;
    }

    public String encrypt(String plain) {
        return e64.encodeToString(encrypt(plain.getBytes(StandardCharsets.UTF_8)));
    }

    public byte[] encrypt(byte[] plain) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plain);
        } catch(Exception ex) {
            logger.error("Cryptic.encrypt()", ex);
        }
        return null;
    }

    public String decrypt(String encrypted) {
        var crypted = d64.decode(encrypted);
        return new String(decrypt(crypted),StandardCharsets.UTF_8);
    }

    public byte[] decrypt(byte[] encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encrypted);
        } catch(Exception ex) {
            logger.error("Cryptic.decrypt()", ex);
        }
        return null;
    }
}
