
package client;

import common.PackageOfBytes;
import common.Torrent;
import common.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DownloadThreadPerSeeder extends Thread {

    ArrayList<PackageOfBytes> packages;
    User user;
    Torrent torrent;
    boolean done;

    public DownloadThreadPerSeeder(ArrayList<PackageOfBytes> allPackages, User user, Torrent torrent) {
        this.packages = new ArrayList<PackageOfBytes>();
        this.torrent = torrent;
        for (PackageOfBytes packageOfBytes : allPackages) {
            if (packageOfBytes.getUser().equals(user)) {
                packages.add(packageOfBytes);
            }
        }
        this.user = user;
        this.done = false;
    }

    @Override
    public void run() {
        for (PackageOfBytes packageOfBytes : packages) {
            try {
                Socket socket = new Socket(user.getAddress(), user.getPort());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                oos.writeObject(torrent.getFileName());
                oos.writeObject(packageOfBytes);
                PackageOfBytes fullPackageOfBytes = (PackageOfBytes) ois.readObject();
                packageOfBytes.setBytes(fullPackageOfBytes.getBytes());
                socket.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DownloadThreadPerSeeder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DownloadThreadPerSeeder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        done = true;
    }

    public ArrayList<PackageOfBytes> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<PackageOfBytes> packages) {
        this.packages = packages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDone() {
        return done;
    }
}
