
package server;

import common.Torrent;
import common.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {

    public static void main(String[] args) {
        try {
            ArrayList<Torrent> torrents = new ArrayList<Torrent>();

            ServerSocket ss = new ServerSocket(1212);
            while (true) {
                Socket socket = ss.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                int request = ois.readInt();
                System.out.println(request);
                if(request == 1){
                    User user = (User) ois.readObject();
                    Torrent torrent = (Torrent) ois.readObject();
                    ArrayList<User> users = new ArrayList<User>();
                    users.add(user);
                    torrent.setUsers(users);
                    torrents.add(torrent);
                }else if(request == 2){
                    User user = (User) ois.readObject();
                    Torrent torrent = (Torrent) ois.readObject();
                    for(Torrent t : torrents){
                        if(t.equals(torrent)){
                            t.getUsers().add(user);
                            break;
                        }
                    }
                }else if(request == 3){
                    String fileName = (String) ois.readObject();
                    String fileType =(String) ois.readObject();
                    ArrayList<Torrent> matches = new ArrayList<Torrent>();
                    for(Torrent t : torrents){
                        if(t.getFileName().contains(fileName)){
                            if(!fileType.equals(" ")){
                                if(t.getFileType().equals(fileType)){
                                    matches.add(t);
                                }
                            }else{
                                matches.add(t);
                            }
                        }
                    }
                    oos.writeObject(matches);
                }
                socket.close();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
