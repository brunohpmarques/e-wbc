package br.com.brunohpmarques.ewbc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import br.com.brunohpmarques.ewbc.adapters.CommandAdapter;
import br.com.brunohpmarques.ewbc.adapters.CommandOptionAdapter;
import br.com.brunohpmarques.ewbc.models.Command;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class MainActivity extends AppCompatActivity {

    /**Lista de comandos disponiveis*/
    private static final List<Command> commandOptionList = new ArrayList<>();
    /**Lista de comandos a serem enviados*/
    private static final List<Command> mainList = new ArrayList<>();

    private static LinearLayoutManager horizontalLayoutManager, verticalLayoutManager;
    private static RecyclerView horizontalList;
    private static RecyclerView verticalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainToolbar);
//        setSupportActionBar(myToolbar);

        if(commandOptionList != null) commandOptionList.clear();
        if(mainList != null) mainList.clear();

        String commOps[] = {"Acelerar","Direita","Ré","Esquerda","Sugar","Soltar"};
        int commRes[] = {R.drawable.ic_arrow_up, R.drawable.ic_arrow_forward,
                R.drawable.ic_arrow_down, R.drawable.ic_arrow_back, R.drawable.ic_publish,
                R.drawable.ic_file_download};

        Command commandOption;
        for(int i =0;i<commOps.length;i++){
            commandOption = new Command(commOps[i], commRes[i]);
            commandOptionList.add(commandOption);
        }

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
            verticalList.setAdapter(new CommandAdapter(this.mainList));
        }
        verticalList.setLayoutManager(verticalLayoutManager);
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnBltConnect:
                // conectar bluetooth
                return true;
            case R.id.btnBltConnected:
                // desconectar bluetooth
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Adiciona comando na lista a ser enviada para o robo **/
    public static void addCommand(Command command){
        if(command != null){
            Log.i("addCommand",command.getTitle());
            MainActivity.mainList.add(0, command);
            MainActivity.verticalList.setAdapter(new CommandAdapter(MainActivity.mainList));
            MainActivity.verticalList.scrollTo(0, 0);
        }
    }

    /** Remove comando na lista a ser enviada para o robo **/
    public static void remCommand(int index){
        if(index >= 0 && index <= MainActivity.mainList.size()){
            index = MainActivity.mainList.size()-index;
            Log.i("remCommand", index+"");
            MainActivity.mainList.remove(index);
            MainActivity.verticalList.setAdapter(new CommandAdapter(MainActivity.mainList));
        }
    }

    /** Retorna lista de comandos disponiveis **/
    public static Command getCommandOption(int index){
        Command command = null;
        if(index >= 0 && index < MainActivity.commandOptionList.size()){
            command = MainActivity.commandOptionList.get(index);
        }
        return command;
    }
}
