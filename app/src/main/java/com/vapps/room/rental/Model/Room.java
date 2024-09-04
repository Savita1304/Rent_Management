package com.vapps.room.rental.Model;

public class Room {
    String RoomNo,Size,BuildingName;
    float Rent,due;
    int Book,Interior,Furniture;


    public float getDue() {
        return due;
    }

    public void setDue(float due) {
        this.due = due;
    }

    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getBuildingName() {
        return BuildingName;
    }

    public void setBuildingName(String buildingName) {
        BuildingName = buildingName;
    }

    public float getRent() {
        return Rent;
    }

    public void setRent(float rent) {
        Rent = rent;
    }

    public int getBook() {
        return Book;
    }

    public void setBook(int book) {
        Book = book;
    }

    public int getInterior() {
        return Interior;
    }

    public void setInterior(int interior) {
        Interior = interior;
    }

    public int getFurniture() {
        return Furniture;
    }

    public void setFurniture(int furniture) {
        Furniture = furniture;
    }
}
