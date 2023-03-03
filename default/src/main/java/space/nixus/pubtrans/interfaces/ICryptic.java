package space.nixus.pubtrans.interfaces;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Cryptic interface for RSA encryption/decryption.
 */
public interface ICryptic {
    
    RSAPublicKey getPubKey();
    RSAPrivateKey getPrivKey();

    String encrypt(String plain);
    String decrypt(String encrypted);

    byte[] encrypt(byte[] plain);
    byte[] decrypt(byte[] encrypted);
}
