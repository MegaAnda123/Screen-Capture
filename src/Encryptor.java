import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Encryption class for encryption with symmetric key and for public/private key encryption
 */
public class Encryptor {

    //Key Vars
    private static SecretKeySpec secretKeySpec;
    private static byte[] key;
    private static SecretKey secretKey = null;


    /**
     * Standard AES Encryption
     * @param input String you want encrypted
     * @param secret shared password/secret
     * @return Encrypted String
     */
    public static String encryptSymmetricWithPassword(String input, String secret,String password){

        try
        {
            setKey(password);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
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
     * @return un encrypted String
     */
    public static String decryptSymmetricWithPassword(String input,String password){

        try
        {
            setKey(password);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
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
            secretKeySpec = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a key pair for Public/private key encryption
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
    public static String publicKeyEncryptor(String plainText, PublicKey publicKey) throws Exception {
        //get cipher
        Cipher encryptCipher = Cipher.getInstance("RSA");
        //initialize as publicKey
        encryptCipher.init(Cipher.PUBLIC_KEY, publicKey);
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
    public static String privateKeyEncryptor(String cipherText, PrivateKey privateKey) throws Exception {

        //Turn string to bytes
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        //Set cipher to RSA and initialize with private key
        Cipher decryptCipher = Cipher.getInstance("RSA");
        //initialize as private key and set private key
        decryptCipher.init(Cipher.PRIVATE_KEY, privateKey);
        //Return decrypted as string
        return new String(decryptCipher.doFinal(bytes), UTF_8);
    }

    /**
     * Generate a 128 keySize SecretKey
     * @return SecretKey
     */
    public static SecretKey generateSymmetricKey(){
        try {
            //init key generator with AES Algorithm
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            //Set keysize to 128 bytes and add random salt
            generator.init(128,new SecureRandom());
            //generate key
            SecretKey key = generator.generateKey();
            //Set static key variable to key for later use
            secretKey = key;
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypt with AES Symmetric key
     * @param input Text for encryption
     * @return
     */
    public static String encryptSymmetricWithSecretKey(String input){
        if (secretKey == null) {
            return null;
        }
        try {
            //get a cipher
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //initialize as Encrypt mode and set secretKey
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //Encrypt and return as string
            return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes("UTF-8")));
        }
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    /**
     * Decrypt AES with Symmetric SecretKey
     * @param cipherText text for decryption
     * @return
     */
    public static String decryptSymmetricWithSecretKey(String cipherText){
        try {
            //get cipher
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            //initialize as Decrypt and set secretKey
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            //Decrypt and return as key
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    /**
     * Set secret key used for Symmetric encryption
     * @param key SecretKey
     */
    public static void setSecretKey(SecretKey key){
        secretKey = key;
    }

    /**
     * Get secretKey used for Symmetric encryption
     * @return SecretKey
     */
    public static SecretKey getSecretKey(){
        return secretKey;
    }

}
