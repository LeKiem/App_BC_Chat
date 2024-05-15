package hunre.it.app_bc_chat.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class LikesDomain implements Serializable {
    private String title;
    private ArrayList<String> picUrl;

    public LikesDomain() {
    }

    public LikesDomain(String title, ArrayList<String> picUrl) {
        this.title = title;
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }
}
