package com.example.nawoo.secretaryproject;

/**
 * Created by nawoo on 2017-12-17.
 */

public class FriendsItem {
    private String requester;
    private String fID;
    private String u1Name;
    private String u2Name;
    private String Status;

    public String getRequester() {return requester;}
    public String getID(){
        return fID;
    }
    public String getU1Name() {return u1Name;}
    public String getU2Name() {return u2Name;}
    public String getStatus(){
        return Status;
    }

    public FriendsItem(String requester, String fID, String u1Name,String u2Name,String Status){
        this.requester = requester;
        this.fID = fID;
        this.u1Name = u1Name;
        this.u2Name = u2Name;
        this.Status = Status;
    }
}
