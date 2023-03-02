package space.nixus.pubtrans.component;

import java.util.Base64;
import javax.crypto.Cipher;
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

import space.nixus.pubtrans.interfaces.IResourceRetriever;
import space.nixus.pubtrans.interfaces.ICryptic;

@Component
public final class Cryptic implements ICryptic {
    
    @Autowired
    private Logger logger;
    @Autowired
    private IResourceRetriever resourceRetriever;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    /**
     * 
     * @return
     */
    public RSAPublicKey getPubKey() {
        if(publicKey==null) {
            try {
                publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(getKeySpec("pub.der", false));
            } catch(Exception ex) {
                logger.warn("Private key generation error.", ex);
            }
        }
        return publicKey;
    }

    /**
     * 
     * @return
     */
    public RSAPrivateKey getPrivKey() {
        if(privateKey==null) {
            try {
                privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(getKeySpec("priv.der", true));
            }
            catch(Exception ex) {
                logger.warn("Public key generation error.", ex);
            }
        }
        return privateKey;
    }
        
    /**
     * 
     * @param resourceName
     * @param pk8
     * @return
     */
    private KeySpec getKeySpec(String resourceName, boolean pk8) {
        try {
            var bytes = resourceRetriever.getResourceAsBytes(resourceName);
            KeySpec spec;
            if (pk8)
                spec = new PKCS8EncodedKeySpec(bytes, "RSA");
            else
                spec = new X509EncodedKeySpec(bytes, "RSA");
            return spec;
        } catch (Exception ex) {
            logger.error("Error in Cryptic.getKeySpec()", ex);
        }
        return null;
    }

    /**
     * 
     */
    public String encrypt(String plain) {
        return Base64.getEncoder().encodeToString(encrypt(plain.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 
     * @param plain
     * @return
     */
    public byte[] encrypt(byte[] plain) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, getPubKey());
            return cipher.doFinal(plain);
        } catch(Exception ex) {
            logger.error("Error in Cryptic.encrypt()", ex);
        }
        return null;
    }

    /**
     * 
     * @param encrypted
     * @return
     */
    public String decrypt(String encrypted) {
        try {
            var crypted = Base64.getDecoder().decode(encrypted.getBytes(StandardCharsets.UTF_8));
            return new String(decrypt(crypted), StandardCharsets.UTF_8);
        } catch(Exception ex) {
            logger.error("Cryptic.decrypt(String)", ex);
        }
        return null;
    }

    /**
     * 
     * @param encrypted
     * @return
     */
    public byte[] decrypt(byte[] encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, getPrivKey());
            return cipher.doFinal(encrypted);
        } catch(Exception ex) {
            logger.error("Error in Cryptic.decrypt()", ex);
        }
        return null;
    }
}
