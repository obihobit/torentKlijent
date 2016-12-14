
package client;

import common.LastPackageOfBytes;
import common.PackageOfBytes;
import common.Torrent;
import common.User;
import common.WholePackageOfBytes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DownloadThread extends Thread {

    Torrent torrent;
    User user;
    HashMap<String, File> seededFiles;

    public DownloadThread(Torrent torrent, User user, HashMap<String, File> seededFiles) {
        this.torrent = torrent;
        this.user = user;
        this.seededFiles = seededFiles;
    }

    public Torrent getTorrent() {
        return torrent;
    }

    public void setTorrent(Torrent torrent) {
        this.torrent = torrent;
    }

    @Override
    public void run() {
        try {
            int numberOfWholePackages = ((int) torrent.getFileSize()) / 1024;
            int numberOfBytesInLastPackage = ((int) torrent.getFileSize()) % 1024;

            ArrayList<PackageOfBytes> packages = new ArrayList<PackageOfBytes>();
            for (int i = 0; i < numberOfWholePackages; i++) {
                WholePackageOfBytes wpob = new WholePackageOfBytes(i, torrent.getUsers().get(i % torrent.getUsers().size()));
                packages.add(wpob);
            }
            LastPackageOfBytes lpob = new LastPackageOfBytes(numberOfBytesInLastPackage, torrent.getUsers().get(0));
            packages.add(lpob);

            ArrayList<DownloadThreadPerSeeder> downloadThreads = new ArrayList<DownloadThreadPerSeeder>();
            for (User seeder : torrent.getUsers()) {
                DownloadThreadPerSeeder downloadThreadPerSeeder = new DownloadThreadPerSeeder(packages, seeder, torrent);
                downloadThreadPerSeeder.start();
                downloadThreads.add(downloadThreadPerSeeder);
            }

            while (true) {
                boolean allDone = true;
                for (DownloadThreadPerSeeder downloadThreadPerSeeder : downloadThreads) {
                    if (!downloadThreadPerSeeder.isDone()) {
                        allDone = false;
                        break;
                    }
                }
                if (allDone) {
                    break;
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DownloadThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            ArrayList<PackageOfBytes> fullPackages = new ArrayList<PackageOfBytes>();
            for (DownloadThreadPerSeeder downloadThreadPerSeeder : downloadThreads) {
                fullPackages.addAll(downloadThreadPerSeeder.getPackages());
            }

            int currentPackageId = 0;
            int packagesWritten = 0;
            File targetFile = new File("e:/" + torrent.getFileName() + "." + torrent.getFileType());
            FileOutputStream fos = new FileOutputStream(targetFile);
            while (true) {
                for (PackageOfBytes packageOfBytes : fullPackages) {
                    if (packageOfBytes.getPackageId() == currentPackageId) {
                        fos.write(packageOfBytes.getBytes());
                        currentPackageId++;
                        packagesWritten++;
                    }
                }
                if (packagesWritten == fullPackages.size() - 1) {
                    for (PackageOfBytes packageOfBytes : fullPackages) {
                        if (packageOfBytes.getPackageId() == Integer.MAX_VALUE) {
                            fos.write(packageOfBytes.getBytes());
                            currentPackageId++;
                            packagesWritten++;
                        }
                    }
                }
                if (packagesWritten == fullPackages.size()) {
                    break;
                }
            }
            fos.close();

            Socket socket = new Socket("10.0.0.8", 1212);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeInt(2);
            oos.writeObject(user);
            oos.writeObject(torrent);
            socket.close();
            seededFiles.put(torrent.getFileName(), targetFile);

        } catch (IOException ex) {
            Logger.getLogger(TorrentGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
