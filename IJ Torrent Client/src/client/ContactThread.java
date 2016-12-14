

package client;

import common.LastPackageOfBytes;
import common.PackageOfBytes;
import common.WholePackageOfBytes;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ContactThread extends Thread {

    ServerSocket ss;
    HashMap<String, File> seededFiles;

    public ContactThread(ServerSocket ss, HashMap<String, File> seededFiles) {
        this.ss = ss;
        this.seededFiles = seededFiles;
    }

    @Override
    public void run(){
        while(true){
            try {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                String fileName = (String) ois.readObject();
                PackageOfBytes packageOfBytes = (PackageOfBytes) ois.readObject();
                File file = seededFiles.get(fileName);
                FileInputStream fis = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                fis.read(bytes);
                fis.close();
                if(packageOfBytes instanceof WholePackageOfBytes){
                    WholePackageOfBytes wpob = (WholePackageOfBytes) packageOfBytes;
                    int packageId = wpob.getPackageId();
                    byte[] bytesForPackage = Arrays.copyOfRange(bytes, packageId*1024, (packageId+1)*1024);
                    wpob.setBytes(bytesForPackage);
                    oos.writeObject(wpob);
                }else{
                    LastPackageOfBytes lpob = (LastPackageOfBytes) packageOfBytes;
                    int numberOfBytesInLastPackage = lpob.getNumberOfBytes();
                    byte[] bytesForPackage = Arrays.copyOfRange(bytes, (int)(file.length()-numberOfBytesInLastPackage), (int)file.length());
                    lpob.setBytes(bytesForPackage);
                    oos.writeObject(lpob);
                }
                socket.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ContactThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ContactThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
