import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Encryptor {

    //Key Vars
    private static SecretKeySpec secretKey;
    private static byte[] key;


    /**
     * Standard AES Encryption
     * @param input String you want encrypted
     * @param secret shared password/secret
     * @return Encrypted String
     */
    public static String encryptSymmetric(String input, String secret){

        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }


    /**
     * Standard AES Decryption
     * @param input String of encrypted message
     * @param secret secret password used to encrypt message
     * @return un encrypted String
     */
    public static String decryptSymmetric(String input,String secret){

        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(input)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;

    }


    /**
     * Sets key used for encryption and decryption and take it from a string to a SecretKeySpec stored statically in the class
      * @param keyIn Wanted Secret
     */
    private static void setKey(String keyIn){
        MessageDigest sha = null;

        try {
            key = keyIn.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a key pari for Public/private key encryption
     * @return KeyPair
     * @throws Exception
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    /**
     * Encrypts plainText with a public key
     * @param plainText String message that you want encrypted
     * @param publicKey Public key from KeyPair
     * @return encrypted String
     * @throws Exception
     */
    public static String encryptWithPublicKey(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Decrypt with Private key, turns message encrypted with its public key back into readable string
     * @param cipherText Encrypted text from public key encryption
     * @param privateKey Private key from key pair
     * @return  Decrypted string
     * @throws Exception
     */
    public static String decryptWithPrivateKey(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), UTF_8);
    }

}
