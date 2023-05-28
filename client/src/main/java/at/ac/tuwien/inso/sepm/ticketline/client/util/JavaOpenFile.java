package at.ac.tuwien.inso.sepm.ticketline.client.util;

import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

@Component
public class JavaOpenFile {

    public void open(String dest) throws IOException {
        if(!Desktop.isDesktopSupported()){
            throw new IOException("Desktop not supported!");
        }

        Desktop desktop = Desktop.getDesktop();
        File file = new File(dest);
        if(file.exists()){
            desktop.open(file);
        } else{
            throw new IOException("File not found");
        }
    }

}