package br.com.brunohpmarques.ewbc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.brunohpmarques.ewbc.adapters.CommandOptionAdapter;
import br.com.brunohpmarques.ewbc.models.CommandOption;

public class MainActivity extends AppCompatActivity {

    private List<CommandOption> commandOptionList;
    private LinearLayoutManager layoutManager;
    private RecyclerView horizontalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String commOps[] = {"Frente","Direita", "Ré","Esquerda","Sugar","Soltar"};
        int commRes[] = {R.drawable.ic_arrow_up, R.drawable.ic_arrow_forward,
                R.drawable.ic_arrow_down, R.drawable.ic_arrow_back, R.drawable.ic_publish,
                R.drawable.ic_file_download};

        this.commandOptionList = new ArrayList<>();
        CommandOption commandOption;
        for(int i =0;i<commOps.length;i++){
            commandOption = new CommandOption(commOps[i], commRes[i]);
            commandOptionList.add(commandOption);
        }

        // Layout
        this.layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.horizontalList = (RecyclerView) findViewById(R.id.horizontalList);
        if (commandOptionList.size() > 0 & horizontalList != null) {
            horizontalList.setAdapter(new CommandOptionAdapter(commandOptionList));
        }
        horizontalList.setLayoutManager(layoutManager);
        //
    }


}
