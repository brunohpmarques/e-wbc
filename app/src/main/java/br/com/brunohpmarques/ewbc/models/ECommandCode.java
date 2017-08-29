package br.com.brunohpmarques.ewbc.models;

import br.com.brunohpmarques.ewbc.R;

/**
 * Created by Bruno Marques on 15/08/2017.
 */

public enum ECommandCode {
    ACELERAR("W","Acelerar", R.drawable.ic_arrow_up, R.string.commandAC),
//    RE("S","Ré",R.drawable.ic_arrow_down, R.string.commandRE),
    DIREITA("D","Direita",R.drawable.ic_arrow_forward, R.string.commandDI),
    ESQUERDA("A","Esquerda",R.drawable.ic_arrow_back, R.string.commandES),
    CHUTAR("K","Chutar",R.drawable.ic_gavel, R.string.commandCH);
//    SUGAR("SU","Sugar",R.drawable.ic_publish, R.string.commandSU),
//    SOLTAR("SO","Soltar",R.drawable.ic_file_download, R.string.commandSO);

    private String label;
    private String title;
    private int imageId;
    private int infoId;
    ECommandCode(String label, String title, int imageId, int infoId){
        this.label = label;
        this.title = title;
        this.imageId = imageId;
        this.infoId = infoId;
    }

    public String getLabel() {
        return label;
    }

    public String getTitle() {
        return title;
    }

    public int getImageId() {
        return imageId;
    }

    public int getInfoId() {
        return infoId;
    }
}
