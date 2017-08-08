package br.com.brunohpmarques.ewbc.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import br.com.brunohpmarques.ewbc.R;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class CommandOptionHolder extends RecyclerView.ViewHolder  {

    public ImageButton commandImage;
    public TextView commandTitle;

    public CommandOptionHolder(View view) {
        super(view);
        commandImage = (ImageButton) view.findViewById(R.id.btnAddCommOp);
        commandTitle = (TextView) view.findViewById(R.id.lblCommOp);
        commandImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), commandTitle.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
