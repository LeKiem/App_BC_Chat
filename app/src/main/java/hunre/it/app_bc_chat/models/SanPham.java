package hunre.it.app_bc_chat.models;

import androidx.annotation.NonNull;

public class SanPham {

    private String maSp;
    private String title, moTa, picUrl, giaGoc, giaGiam, tiLeGiam, coGiamGia, timestamp, uid, sl;

    public SanPham() {
    }

    public SanPham(String maSp, String title, String moTa, String picUrl, String giaGoc, String giaGiam, String tiLeGiam, String coGiamGia, String timestamp, String uid, String sl) {
        this.maSp = maSp;
        this.title = title;
        this.moTa = moTa;
        this.picUrl = picUrl;
        this.giaGoc = giaGoc;
        this.giaGiam = giaGiam;
        this.tiLeGiam = tiLeGiam;
        this.coGiamGia = coGiamGia;
        this.timestamp = timestamp;
        this.uid = uid;
        this.sl = sl;
    }

    public String getMaSp() {
        return maSp;
    }

    public void setMaSp(String maSp) {
        this.maSp = maSp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(String giaGoc) {
        this.giaGoc = giaGoc;
    }

    public String getGiaGiam() {
        return giaGiam;
    }

    public void setGiaGiam(String giaGiam) {
        this.giaGiam = giaGiam;
    }

    public String getTiLeGiam() {
        return tiLeGiam;
    }

    public void setTiLeGiam(String tiLeGiam) {
        this.tiLeGiam = tiLeGiam;
    }

    public String getCoGiamGia() {
        return coGiamGia;
    }

    public void setCoGiamGia(String coGiamGia) {
        this.coGiamGia = coGiamGia;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }
}
