package com.example.nawoo.secretaryproject;

/**
 * Created by nawoo on 2017-12-17.
 */

public class FriendsItem {
    private String fID;
    private String Status;

    public String getID(){
        return fID;
    }
    public String getStatus(){
        return Status;
    }

    public FriendsItem(String fID, String Status){
        this.fID = fID;
        this.Status = Status;
    }
}
