package entity;

import java.util.Date;

public class classMember {
    private int registrationID;
    private int classID;
    private int memberID;

    public classMember(int registrationID, int classID, int memberID) {
        this.registrationID = registrationID;
        this.classID = classID;
        this.memberID = memberID;
    }

    public int getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }


}
