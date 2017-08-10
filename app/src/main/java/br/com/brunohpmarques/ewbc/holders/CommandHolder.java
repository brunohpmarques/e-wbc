package br.com.brunohpmarques.ewbc.holders;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.brunohpmarques.ewbc.R;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class CommandHolder extends RecyclerView.ViewHolder  {

    public ImageButton commandImage;
    public TextView commandTitle;
    public ImageView commandInfo;
    public ImageView commandRem;
    public TextView commandPos;

    public CommandHolder(View view) {
        super(view);
        commandImage = (ImageButton) view.findViewById(R.id.btnComm);
        commandTitle = (TextView) view.findViewById(R.id.lblComm);
        commandInfo = (ImageView) view.findViewById(R.id.btnInfComm);
        commandRem = (ImageView) view.findViewById(R.id.btnRemComm);
        commandPos = (TextView) view.findViewById(R.id.lblCommPos);

        commandImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), commandTitle.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        commandInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "INFO: "+commandTitle.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        commandRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "REM: "+commandTitle.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
