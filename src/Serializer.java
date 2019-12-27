import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;

public class Serializer {

    public static String objectToText(PublicKey myObject) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(myObject);
            oos.flush();
            byte[] binary = baos.toByteArray();
            String text = Base64.getEncoder().encodeToString(binary); // Base64 is in the apache commons codec library
            return text;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Public key adapted deserializer
     * @param objectText
     * @return
     */
    public static PublicKey textToPublicKey(String objectText){

        try {
            byte[] b = Base64.getDecoder().decode(objectText);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            PublicKey puKey = (PublicKey) si.readObject();
            return puKey;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }
}
