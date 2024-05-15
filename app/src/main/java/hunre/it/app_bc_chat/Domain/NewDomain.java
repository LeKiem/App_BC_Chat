package hunre.it.app_bc_chat.Domain;

import java.util.ArrayList;

public class NewDomain {

    private String description;
    private ArrayList<String> picUrl;

    public NewDomain(String description, ArrayList<String> picUrl) {
        this.description = description;
        this.picUrl = picUrl;
    }

    public NewDomain() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }
}
