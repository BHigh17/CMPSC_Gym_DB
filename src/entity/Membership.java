package entity;

public class Membership {
    private int membershipID;
    private int memPrice;
    private String memName;
    private int memberID;




    public Membership(int membershipID, int memPrice, String memName) {
        this.membershipID = membershipID;
        this.memPrice = memPrice;
        this.memName = memName;
        this.memberID = memberID;
    }

    public int getMembershipID() {
        return membershipID;
    }

    public void setMembershipID(int membershipID) {
        this.membershipID = membershipID;
    }

    public int getMemPrice() {
        return memPrice;
    }

    public void setMemPrice(int memPrice) {
        this.memPrice = memPrice;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public int getMemberID() { return memberID; }

    public void setMemberID(int memberID) { this.memberID = memberID; }

    @Override
    public String toString() {
        return "Membership{" +
                "membershipID=" + membershipID +
                ", memPrice=" + memPrice +
                ", memName='" + memName + '\'' +
                ", memberID=" + memberID +
                '}';
    }
}
