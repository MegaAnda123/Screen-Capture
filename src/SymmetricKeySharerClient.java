import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.PublicKey;

public class SymmetricKeySharerClient {

    /**
     * Generic secret key sharer for AES encryption and public/private Key to get to key with server
     * Client side protocol
     * @param socket Socket used to connect to server
     * @return SecretKey used for symmetric AES encryption
     */
    public static SecretKey clientSideProtocol(Socket socket){
        try {
            //Writers and readers
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            //Generate SecretKey
            SecretKey symmetricKey = Encryptor.generateSymmetricKey();

            //Serialize SecretKey
            String serializedKey = Serializer.ObjectToString(symmetricKey);

            //Receive public key
            String keyString = br.readLine();

            //Deserialize
            PublicKey publicKey = (PublicKey) Serializer.ObjectFromString(keyString);

            //Encrypt secretKey
            String encryptedSerializeKey = Encryptor.publicKeyEncryptor(serializedKey,publicKey);

            //Send Encrypted Key to server
            pw.println(encryptedSerializeKey);
            pw.flush();

            //receive confirm and check if decrypted correctly
            String encryptedConformation = br.readLine();
            Encryptor.setSecretKey(symmetricKey);
            String confirmation = Encryptor.decryptSymmetricWithSecretKey(encryptedConformation);

            //Check confirm and return key
            if(confirmation.equals("200")){
                //Send to server that it was correct
                pw.println("200");
                pw.flush();

                //return key
                return symmetricKey;
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unexpected Error");
        }
        return null;
    }

}
