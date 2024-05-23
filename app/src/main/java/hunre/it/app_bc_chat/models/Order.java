package hunre.it.app_bc_chat.models;

import java.util.HashMap;

public class Order {
    private String maHd;
    private String ngayDat;
    private String tongHd;
    private String uid_khachHang;
    private String sdtKhachHang;
//    private HashMap<String, Product> sanPham;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String maHd, String ngayDat, String tongHd, String uid_khachHang, String sdtKhachHang) {
        this.maHd = maHd;
        this.ngayDat = ngayDat;
        this.tongHd = tongHd;
        this.uid_khachHang = uid_khachHang;
        this.sdtKhachHang = sdtKhachHang;
    }

    public String getMaHd() {
        return maHd;
    }

    public void setMaHd(String maHd) {
        this.maHd = maHd;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getTongHd() {
        return tongHd;
    }

    public void setTongHd(String tongHd) {
        this.tongHd = tongHd;
    }

    public String getUid_khachHang() {
        return uid_khachHang;
    }

    public void setUid_khachHang(String uid_khachHang) {
        this.uid_khachHang = uid_khachHang;
    }

    public String getSdtKhachHang() {
        return sdtKhachHang;
    }

    public void setSdtKhachHang(String sdtKhachHang) {
        this.sdtKhachHang = sdtKhachHang;
    }

}
