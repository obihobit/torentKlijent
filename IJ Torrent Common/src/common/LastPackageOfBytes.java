
package common;

public class LastPackageOfBytes extends PackageOfBytes {

    int numberOfBytes;

    public LastPackageOfBytes(int numberOfBytes, User user) {
        super(user);
        this.numberOfBytes = numberOfBytes;
        packageId = Integer.MAX_VALUE;
    }

    public int getNumberOfBytes() {
        return numberOfBytes;
    }

    public void setNumberOfBytes(int numberOfBytes) {
        this.numberOfBytes = numberOfBytes;
    }

}
