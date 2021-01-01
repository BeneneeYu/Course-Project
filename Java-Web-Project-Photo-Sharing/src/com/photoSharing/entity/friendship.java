package com.photoSharing.entity;

/**
 * @program: Project
 * @description:
 * @author: Shen Zhengyu
 * @create: 2020-07-16 11:59
 **/
public class friendship {
    private int FriendshipID;
    private int FriendOne;
    private int FriendTwo;

    public friendship(){}
    public int getFriendshipID() {
        return FriendshipID;
    }

    public void setFriendshipID(int friendshipID) {
        FriendshipID = friendshipID;
    }

    public int getFriendOne() {
        return FriendOne;
    }

    public void setFriendOne(int friendOne) {
        FriendOne = friendOne;
    }

    public int getFriendTwo() {
        return FriendTwo;
    }

    public void setFriendTwo(int friendTwo) {
        FriendTwo = friendTwo;
    }
}
