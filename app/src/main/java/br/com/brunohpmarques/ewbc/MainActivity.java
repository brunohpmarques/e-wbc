package br.com.brunohpmarques.ewbc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.brunohpmarques.ewbc.adapters.CommandAdapter;
import br.com.brunohpmarques.ewbc.adapters.CommandOptionAdapter;
import br.com.brunohpmarques.ewbc.models.CommandOption;

public class MainActivity extends AppCompatActivity {

    /**Lista de comandos disponiveis*/
    private List<CommandOption> commandOptionList;
    /**Lista de comandos a serem enviados*/
    private List<CommandOption> mainList;
    private LinearLayoutManager horizontalLayoutManager, verticalLayoutManager;
    private RecyclerView horizontalList;
    private RecyclerView verticalList;

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
        this.mainList = new ArrayList<>();
        this.mainList.add(commandOptionList.get(0));
        this.mainList.add(commandOptionList.get(3));
        this.mainList.add(commandOptionList.get(4));
        this.mainList.add(commandOptionList.get(0));
        this.mainList.add(commandOptionList.get(1));
        this.mainList.add(commandOptionList.get(0));
        this.mainList.add(commandOptionList.get(3));
        this.mainList.add(commandOptionList.get(0));
        this.mainList.add(commandOptionList.get(5));

        // Layout
        this.horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.horizontalList = (RecyclerView) findViewById(R.id.horizontalList);
        if (commandOptionList.size() > 0 & horizontalList != null) {
            horizontalList.setAdapter(new CommandOptionAdapter(commandOptionList));
        }
        horizontalList.setLayoutManager(horizontalLayoutManager);

        this.verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.verticalList = (RecyclerView) findViewById(R.id.verticalList);
        if (this.mainList.size() > 0 & verticalList != null) {
            Collections.reverse(this.mainList);
            verticalList.setAdapter(new CommandAdapter(this.mainList));
        }
        verticalList.setLayoutManager(verticalLayoutManager);
        //
    }


}
