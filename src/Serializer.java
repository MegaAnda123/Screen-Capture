import java.io.*;
import java.util.Base64;

class Serializer {


    /**
     * Converts a serializable object to a string.
     * @param object Object that will be converted to a string.
     * @return Returns the object serialized as a string.
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    static String ObjectToString(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    /**
     * Converts a serialized object string back to a object.
     * @param string The string the method will convert.
     * @return Returns the object.
     * @throws IOException if an I/O error occurs when waiting for a connection.
     * @throws ClassNotFoundException //TODO comment this
     */
    static Object ObjectFromString(String string) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(string);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }

    static class Data implements Serializable {
        private File file;

        Data(File file) {
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }
}
