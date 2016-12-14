

package common;

import java.io.Serializable;
import java.util.ArrayList;


public class Torrent implements Serializable {

    public String fileName;
    public String fileType;
    long fileSize;
    public ArrayList<User> users;

    public Torrent(String fileName, String fileType, long fileSize, ArrayList<User> users) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.users = users;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Torrent other = (Torrent) obj;
        if ((this.fileName == null) ? (other.fileName != null) : !this.fileName.equals(other.fileName)) {
            return false;
        }
        if ((this.fileType == null) ? (other.fileType != null) : !this.fileType.equals(other.fileType)) {
            return false;
        }
        if (this.fileSize != other.fileSize) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
        hash = 37 * hash + (this.fileType != null ? this.fileType.hashCode() : 0);
        hash = 37 * hash + (int) (this.fileSize ^ (this.fileSize >>> 32));
        return hash;
    }

   
    
}
