import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SymmetricKeySharerServer {

    /**
     * Generic secret key sharer for AES encryption and public/private Key to get to key with client
     * Server side protocol
     * @param socket Socket used to connect to server
     * @return SecretKey used for symmetric AES encryption
     */
    public static SecretKey serverSideProtocol(Socket socket, PrivateKey privateKey, PublicKey publicKey){
        try {
            //Writers and readers
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            //serialize publicKey
            String publicKeyString = Serializer.ObjectToString(publicKey);

            //Send public key
            pw.println(publicKeyString);

            //Receive Symmetric key
            String encryptedSerializedSymmetricKey = br.readLine();

            //Decrypt SymmetricKey String
            String serializedSymmetricKey = Encryptor.privateKeyEncryptor(encryptedSerializedSymmetricKey,privateKey);

            //Deserialize
            SecretKey secretKey = (SecretKey) Serializer.ObjectFromString(serializedSymmetricKey);

            //Send confirm
            pw.println("200");

            //return key
            return secretKey;


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error: "+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unknown Error");
        }
        return null;
    }

}
