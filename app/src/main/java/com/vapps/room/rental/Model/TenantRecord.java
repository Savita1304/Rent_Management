package com.vapps.room.rental.Model;

public class TenantRecord {
    String tid,tname,timg,tsdate,tadd,tedate,tmob,roomno,buildingname;
    float trent,tele,twater;


    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getBuildingname() {
        return buildingname;
    }

    public void setBuildingname(String buildingname) {
        this.buildingname = buildingname;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTimg() {
        return timg;
    }

    public void setTimg(String timg) {
        this.timg = timg;
    }

    public String getTsdate() {
        return tsdate;
    }

    public void setTsdate(String tsdate) {
        this.tsdate = tsdate;
    }

    public String getTadd() {
        return tadd;
    }

    public void setTadd(String tadd) {
        this.tadd = tadd;
    }

    public String getTedate() {
        return tedate;
    }

    public void setTedate(String tedate) {
        this.tedate = tedate;
    }

    public float getTrent() {
        return trent;
    }

    public void setTrent(float trent) {
        this.trent = trent;
    }

    public String getTmob() {
        return tmob;
    }

    public void setTmob(String tmob) {
        this.tmob = tmob;
    }


    public float getTele() {
        return tele;
    }

    public void setTele(float tele) {
        this.tele = tele;
    }

    public float getTwater() {
        return twater;
    }

    public void setTwater(float twater) {
        this.twater = twater;
    }
}
