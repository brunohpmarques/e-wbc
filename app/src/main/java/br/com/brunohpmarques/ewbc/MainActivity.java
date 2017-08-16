package br.com.brunohpmarques.ewbc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import br.com.brunohpmarques.ewbc.adapters.CommandAdapter;
import br.com.brunohpmarques.ewbc.adapters.CommandOptionAdapter;
import br.com.brunohpmarques.ewbc.models.Command;
import br.com.brunohpmarques.ewbc.models.ECommandCode;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 543;

    /**Lista de comandos disponiveis*/
    private static final List<Command> commandOptionList = new ArrayList<>();
    /**Lista de comandos a serem enviados*/
    private static final List<Command> mainList = new ArrayList<>();
    private static boolean isSending;
    private static boolean isShowingDialog;

    private static MainActivity mainInstance;
    private static LinearLayoutManager horizontalLayoutManager, verticalLayoutManager;
    private static RecyclerView horizontalList;
    private static RecyclerView verticalList;
    private static ProgressDialog progressDialog;

    private Menu menu;
    private Button btnStart;

    public static MainActivity getInstance(){
        return mainInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainToolbar);
//        setSupportActionBar(myToolbar);

        // Comandos
        if(commandOptionList != null) commandOptionList.clear();
        if(mainList != null) mainList.clear();

        ECommandCode commOps[] = ECommandCode.values();
        Command commandOption;
        for(int i =0;i<commOps.length;i++){
            commandOption = new Command(commOps[i]);
            commandOptionList.add(commandOption);
        }
        //

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

        this.btnStart = (Button) findViewById(R.id.btnStart);
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommands();
            }
        });
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnBltDisabled:
                // conectar bluetooth
                enableBlt();
                return true;
            case R.id.btnBltConnected:
                // desconectar bluetooth
                disableBlt();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Ativar bluetooth e busca o robo **/
    private void enableBlt(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (menu != null) {
                    MenuItem bltDisabled = menu.findItem(R.id.btnBltDisabled);
                    MenuItem bltConnecting = menu.findItem(R.id.btnBltConnecting);
                    if (bltDisabled != null && bltConnecting != null) {
                        bltDisabled.setVisible(false);
                        bltConnecting.setVisible(true);
                        Snackbar.make(verticalList, getString(R.string.connecting), Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                MainActivity.this.connectBlt();
                            }
                        }, 3000);
                    }
                }
            }
        });
    }

    /** Desativa bluetooth **/
    private void disableBlt(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (menu != null) {
                    MenuItem bltDisabled = menu.findItem(R.id.btnBltDisabled);
                    MenuItem bltConnecting = menu.findItem(R.id.btnBltConnecting);
                    MenuItem bltConnected = menu.findItem(R.id.btnBltConnected);
                    if (bltDisabled != null && bltConnecting != null && bltConnected != null) {
                        bltDisabled.setVisible(true);
                        bltConnecting.setVisible(false);
                        bltConnected.setVisible(false);
                        Snackbar.make(verticalList, getString(R.string.disabled), Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /** Conecta bluetooth com robo **/
    private void connectBlt(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (menu != null) {
                    MenuItem bltConnecting = menu.findItem(R.id.btnBltConnecting);
                    MenuItem bltConnected = menu.findItem(R.id.btnBltConnected);
                    if (bltConnecting != null && bltConnected != null) {
                        bltConnecting.setVisible(false);
                        bltConnected.setVisible(true);
                        Snackbar.make(verticalList, getString(R.string.connected), Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public static void checkCoarsePermissions(Activity activity){
        boolean isPermission = false;
        if(Build.VERSION.SDK_INT >= 23){
            if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                // Se o usuario ja negou a permissao
                if(ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION)){
                    // Explicar o uso da permissao com um dialog
                }
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS);

            }else{
                // ja tem permissao
                isPermission = true;
            }
        }else{
            // eh menor do que a API 23, ou seja, ja tem permissao desde a instalacao
            isPermission = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.mainInstance = this;
        checkCoarsePermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showCommandInfo(int position, Command command){
        Context ctx = MainActivity.getInstance();
        if(!MainActivity.isShowingDialog && ctx != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.adapter_string);
            adapter.add(command.getInfo(ctx));

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx, R.style.DialogTheme);
            alertDialogBuilder.setCancelable(true)
                    .setPositiveButton(ctx.getString(R.string.ok),
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.dismiss();
                                    MainActivity.isShowingDialog = false;
                                    /// TODO
                                }
                            })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            MainActivity.isShowingDialog = false;
                            /// TODO
                        }
                    });
            alertDialogBuilder.setAdapter(adapter, null);
            alertDialogBuilder.setTitle(position+": "+command.getTitle());
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
            isShowingDialog = true;
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

    /** Retorna lista de comandos adicionados **/
    public static Command getCommandMain(int index){
        Command command = null;
        if(index >= 0 && index < MainActivity.mainList.size()){
            command = MainActivity.mainList.get(index);
        }
        return command;
    }

    /** Retorna lista de comandos disponiveis **/
    public static Command getCommandOption(int index){
        Command command = null;
        if(index >= 0 && index < MainActivity.commandOptionList.size()){
            command = MainActivity.commandOptionList.get(index);
        }
        return command;
    }

    /** Retorna lista de comandos disponiveis **/
    public static void sendCommands(){
        if(MainActivity.mainList != null && !MainActivity.mainList.isEmpty()) {
            if (!MainActivity.isSending) {
                MainActivity.isSending = true;
                showProgress(R.string.sending);

                Command comm;
                String message;
                int total = MainActivity.mainList.size();
                for (int i = total-1; i>=0; i--) {
                    comm = MainActivity.mainList.get(i);
                    message = comm.getCode()+"#"+comm.getParam();
                    Log.i("sendCommands", (total-i)+"/"+total+": "+message);
                    // TODO enviar comandos via bluetooth
                    MainActivity.progressDialog.setMessage((total-i)+"/"+total+": "+message);
//                    MainActivity.closeProgress();
//                    MainActivity.isSending = false;
                }


                // Apagar depois
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.closeProgress();
                        MainActivity.isSending = false;
                    }
                }, 3000);
                //


            }else{
                Snackbar.make(verticalList, verticalList.getContext().getString(R.string.alreadySending), Snackbar.LENGTH_SHORT).show();
            }
        }else{
            Snackbar.make(verticalList, verticalList.getContext().getString(R.string.listEmpty), Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showProgress(int stringId){
        if(MainActivity.progressDialog == null) {
            MainActivity.progressDialog = ProgressDialog.show(MainActivity.getInstance(), verticalList.getContext().getString(stringId), "", true);
        }
    }

    public static void closeProgress(){
        if(MainActivity.progressDialog != null){
            MainActivity.progressDialog.dismiss();
            MainActivity.progressDialog = null;
        }
    }
}
