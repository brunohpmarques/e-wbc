package br.com.brunohpmarques.ewbc.models;

import java.io.Serializable;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class Command implements Serializable {
    private String title;
    private String code;
    private String param;
    private int resourceId;

    public Command(String title, int resourceId) {
        this.title = title;
        this.code = title.replace(" ", "").toUpperCase();
        this.param = "";
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

    public String getParam() {
        return param;
    }

    public String setParam(String param) {
        return this.param = param;
    }
}
