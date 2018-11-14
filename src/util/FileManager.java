package util;

import java.io.*;

public class FileManager {

    public static String read(String path) {
        File file = new File(path);
        String result = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tmp;
            //  一次读入一行，直到读入null为文件结束  
            while ((tmp = reader.readLine()) != null) {
                result += tmp + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void write(String path, String content) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(path);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
