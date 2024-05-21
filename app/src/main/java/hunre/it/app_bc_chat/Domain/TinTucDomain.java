package hunre.it.app_bc_chat.Domain;

import java.io.Serializable;

public class TinTucDomain implements Serializable {
    private String maTt;
    private String tenTT, moTa, hinhAnh, timestamp, uid;

    public TinTucDomain() {
    }

    public TinTucDomain(String maTt, String tenTT, String moTa, String hinhAnh, String timestamp, String uid) {
        this.maTt = maTt;
        this.tenTT = tenTT;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getMaTt() {
        return maTt;
    }

    public void setMaTt(String maTt) {
        this.maTt = maTt;
    }

    public String getTenTT() {
        return tenTT;
    }

    public void setTenTT(String tenTT) {
        this.tenTT = tenTT;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
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
}
