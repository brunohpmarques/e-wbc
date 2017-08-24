package br.com.brunohpmarques.ewbc.holders;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.brunohpmarques.ewbc.MainActivity;
import br.com.brunohpmarques.ewbc.R;
import br.com.brunohpmarques.ewbc.models.Command;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class CommandHolder extends RecyclerView.ViewHolder  {
    private static Drawable infoDraw, removeDraw;
    public ImageButton commandImage;
    public TextView commandTitle;
    public ImageView commandInfo;
    public ImageView commandRem;
    public TextView commandPos;

    public CommandHolder(View view) {
        super(view);
        if(infoDraw == null){
            infoDraw = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_info, null).mutate();
        }
        if(removeDraw == null){
            removeDraw = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_cancel, null).mutate();
        }

        commandImage = (ImageButton) view.findViewById(R.id.btnComm);
        commandTitle = (TextView) view.findViewById(R.id.lblComm);
        commandInfo = (ImageView) view.findViewById(R.id.btnInfComm);
        commandRem = (ImageView) view.findViewById(R.id.btnRemComm);
        commandPos = (TextView) view.findViewById(R.id.lblCommPos);

        commandImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, commandTitle.getText(), Snackbar.LENGTH_SHORT).show();
                return false;
            }
        });
        commandInfo.setImageDrawable(infoDraw);
        commandInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(commandPos.getText().toString());
                Log.e("POSICAO", ""+position);
                Command command = MainActivity.getCommandMain(position-1);
                MainActivity.getInstance().showCommandInfo(position, command);
            }
        });
        commandRem.setImageDrawable(removeDraw);
        commandRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, commandTitle.getText()+" "+view.getContext().getString(R.string.removed), Snackbar.LENGTH_SHORT).show();
                int index = Integer.parseInt(commandPos.getText().toString());
                MainActivity.remCommand(index);
            }
        });

    }
}
