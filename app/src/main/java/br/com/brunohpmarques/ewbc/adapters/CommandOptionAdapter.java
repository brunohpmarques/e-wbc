package br.com.brunohpmarques.ewbc.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.brunohpmarques.ewbc.R;
import br.com.brunohpmarques.ewbc.holders.CommandOptionHolder;
import br.com.brunohpmarques.ewbc.models.Command;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class CommandOptionAdapter extends RecyclerView.Adapter<CommandOptionHolder> {

    private List<Command> list;

    public CommandOptionAdapter(List<Command> Data) {
        list = Data;
    }

    @Override
    public CommandOptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_command_option, parent, false);
        CommandOptionHolder holder = new CommandOptionHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommandOptionHolder holder, int position) {
        holder.commandTitle.setText(list.get(position).getTitle());
        holder.commandPosition.setText(position+"");
        holder.commandImage.setImageResource(list.get(position).getResourceId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
