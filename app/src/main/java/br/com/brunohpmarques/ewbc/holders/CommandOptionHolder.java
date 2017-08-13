package br.com.brunohpmarques.ewbc.holders;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.brunohpmarques.ewbc.MainActivity;
import br.com.brunohpmarques.ewbc.R;
import br.com.brunohpmarques.ewbc.models.Command;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class CommandOptionHolder extends RecyclerView.ViewHolder  {

    public ImageButton commandImage;
    public TextView commandTitle;
    public TextView commandPosition;

    public CommandOptionHolder(View view) {
        super(view);
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
