package br.com.brunohpmarques.ewbc.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.brunohpmarques.ewbc.R;
import br.com.brunohpmarques.ewbc.holders.CommandHolder;
import br.com.brunohpmarques.ewbc.models.Command;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class CommandAdapter extends RecyclerView.Adapter<CommandHolder> {

    private List<Command> list;

    public CommandAdapter(List<Command> Data) {
        list = Data;
    }

    @Override
    public CommandHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_command, parent, false);
        CommandHolder holder = new CommandHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommandHolder holder, int position) {
        holder.commandTitle.setText(list.get(position).getTitle());
        holder.commandImage.setImageResource(list.get(position).getResourceId());
        holder.commandPos.setText(list.size()-position+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
