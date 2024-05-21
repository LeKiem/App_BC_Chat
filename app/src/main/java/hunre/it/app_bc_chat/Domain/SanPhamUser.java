package hunre.it.app_bc_chat.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class SanPhamUser implements Serializable {

    private int NumberinCart;

    private String maSp;
    private String title, description, picUrl, giaGoc, giaGiam, tiLeGiam, coGiamGia, timestamp, uid, sl;

    public SanPhamUser() {
    }

    public SanPhamUser( int numberinCart, String maSp, String title, String description, String picUrl, String giaGoc, String giaGiam, String tiLeGiam, String coGiamGia, String timestamp, String uid, String sl) {

        NumberinCart = numberinCart;
        this.maSp = maSp;
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.giaGoc = giaGoc;
        this.giaGiam = giaGiam;
        this.tiLeGiam = tiLeGiam;
        this.coGiamGia = coGiamGia;
        this.timestamp = timestamp;
        this.uid = uid;
        this.sl = sl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getMaSp() {
        return maSp;
    }

    public void setMaSp(String maSp) {
        this.maSp = maSp;
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


    public int getNumberinCart() {
        return NumberinCart;
    }

    public void setNumberinCart(int numberinCart) {
        this.NumberinCart = numberinCart;
    }
}
