

package common;

import java.io.Serializable;


public class PackageOfBytes implements Serializable {

    byte[] bytes;
    User user;
    int packageId;

    public PackageOfBytes(User user) {
        this.user = user;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    
 
}
