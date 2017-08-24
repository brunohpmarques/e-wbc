package br.com.brunohpmarques.ewbc.holders;

import android.graphics.drawable.Drawable;
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

public class CommandOptionHolder extends RecyclerView.ViewHolder  {

    private static Drawable draw;
    public ImageButton commandImage;
    public TextView commandTitle;
    public TextView commandPosition;

    public CommandOptionHolder(View view) {
        super(view);
        if(draw == null){
            draw = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_add_circle, null).mutate();
        }
        ((ImageView) view.findViewById(R.id.btnIcAddCommOp)).setImageDrawable(draw);
        commandImage = (ImageButton) view.findViewById(R.id.btnAddCommOp);
        commandTitle = (TextView) view.findViewById(R.id.lblCommOp);
        commandPosition = (TextView) view.findViewById(R.id.lblCommOpPos);
        commandImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Command command = MainActivity.getCommandOption(Integer.parseInt(commandPosition.getText().toString()));
                MainActivity.addCommand(command);
                Snackbar.make(view, commandTitle.getText()+" "+view.getContext().getString(R.string.added), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
