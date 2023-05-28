package at.ac.tuwien.inso.sepm.ticketline.server.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;


public class FileUtil {
    private static String NAME = "ticketlinefiles";
    private static String DIR = System.getProperty("user.home") + File.separator + NAME + File.separator;

    static {
        File file = new File(DIR);
        if(!file.exists()){
            file.mkdir();
        }
    }

    public static void copy(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    public static File file(String name){
        if(name.contains("..")){
            return null;
        }
        return new File(DIR + name);
    }

    public static byte[] readAllBytes(String name) throws IOException {
        return Files.readAllBytes(file(name).toPath());
    }

    public static void write(String name, byte[] bytes) throws IOException{
        Files.write(file(name).toPath(),bytes);
    }

    public static String getExtension(String filename){
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i+1).toLowerCase();
        }
        return extension;
    }


}
