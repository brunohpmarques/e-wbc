package br.com.brunohpmarques.ewbc.models;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class CommandOption {
    private String title;
    private String code;
    private int resourceId;

    public CommandOption(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
