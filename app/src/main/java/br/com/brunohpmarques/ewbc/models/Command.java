package br.com.brunohpmarques.ewbc.models;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class Command implements Serializable {
    private ECommandCode code;
    private String param;

    public Command(ECommandCode code) {
        this.code = code;
        this.param = "";
    }

    public String getTitle() {
        return code.getTitle();
    }

    public int getResourceId() {
        return code.getImageId();
    }

    public ECommandCode getCode() {
        return code;
    }

    public String getParam() {
        return param;
    }

    public String setParam(String param) {
        return this.param = param;
    }

    public String getCodeFormatted(){
        return code.getLabel()+"#"+param;
    }

    public String getInfo(Context ctx){
        return ctx.getString(code.getInfoId());
    }
}
