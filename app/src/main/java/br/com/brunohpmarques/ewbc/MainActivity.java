package br.com.brunohpmarques.ewbc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.admin.DeviceAdminInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import br.com.brunohpmarques.ewbc.adapters.CommandAdapter;
import br.com.brunohpmarques.ewbc.adapters.CommandOptionAdapter;
import br.com.brunohpmarques.ewbc.bluetooth.BluetoothHC;
import br.com.brunohpmarques.ewbc.bluetooth.BluetoothReceive;
import br.com.brunohpmarques.ewbc.models.Command;
import br.com.brunohpmarques.ewbc.models.ECommandCode;

/**
 * Created by Bruno Marques on 08/08/2017.
 */

public class MainActivity extends AppCompatActivity {
    public static BluetoothHC bt;
    public static BluetoothReceive bluetoothReceive;
    public static Menu menu;
    public static final String TAG = Command.class.getSimpleName().toUpperCase() + "_TAG";
    private static final int REQUEST_PERMISSIONS = 543;
    /**
     * Lista de comandos disponiveis
     */
    private static final List<Command> commandOptionList = new ArrayList<>();
    /**
     * Lista de comandos a serem enviados
     */
    private static final List<Command> mainList = new ArrayList<>();
    private static boolean isSending;

    private static MainActivity mainInstance;
    private static LinearLayoutManager horizontalLayoutManager, verticalLayoutManager;
    private static RecyclerView horizontalList;
    private static RecyclerView verticalList;
    private static ProgressDialog progressDialog;
    private static Button btnStart;
    private static RelativeLayout mainLayout;
    private static LinearLayout emptyLayout;

    //////////////////////////////////////////////
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothHC.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case BluetoothHC.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case BluetoothHC.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ ");
                    break;
                case BluetoothHC.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME " + msg);
                    break;
                case BluetoothHC.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST " + msg);
                    break;
            }
        }
    };

    public static MainActivity getInstance() {
        return mainInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        MainActivity.bt = new BluetoothHC(this, mHandler, BluetoothAdapter.getDefaultAdapter());
        MainActivity.bluetoothReceive = new BluetoothReceive();

        // Comandos
        if (commandOptionList != null) commandOptionList.clear();
        if (mainList != null) mainList.clear();

        ECommandCode commOps[] = ECommandCode.values();
        Command commandOption;
        for (int i = 0; i < commOps.length; i++) {
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

        this.mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        this.emptyLayout = (LinearLayout) findViewById(R.id.emptyLayout);
        Drawable draw = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_circle, null).mutate();
        ((ImageView) findViewById(R.id.btnEmptyLayout)).setImageDrawable(draw);

        this.btnStart = (Button) findViewById(R.id.btnStart);
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommands();
            }
        });
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            Drawable roundDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.btnstart, null).mutate();
            roundDrawable.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.green, null), PorterDuff.Mode.SRC_ATOP);
            this.btnStart.setBackground(roundDrawable);
        }
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        this.menu = menu;

        MenuItem bltDisabled = this.menu.findItem(R.id.btnBltDisabled);
        MenuItem bltActivated = this.menu.findItem(R.id.btnBltActivated);
        MenuItem bltConnecting = this.menu.findItem(R.id.btnBltConnecting);
        MenuItem bltConnected = this.menu.findItem(R.id.btnBltConnected);

        Drawable draw = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bluetooth_disabled, null).mutate();
        bltDisabled.setIcon(draw);
        draw = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bluetooth, null).mutate();
        bltActivated.setIcon(draw);
        draw = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bluetooth_searching, null).mutate();
        bltConnecting.setIcon(draw);
        draw = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bluetooth_connected, null).mutate();
        bltConnected.setIcon(draw);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            switch (item.getItemId()) {
                case R.id.btnBltDisabled:
                    // bluetooth desativado
                    bt.on();
                    return true;
                case R.id.btnBltActivated:
                    // bluetooth ativado
                    bt.findDevices(MainActivity.this);
                    return true;
                case R.id.btnBltConnecting:
                    // conectando com o robo
                    bt.off();
                    return true;
                case R.id.btnBltConnected:
                    // bluetooth conectado com o robo
                    bt.off();
                    return true;
                default:
                    break;
            }
        } else {
            Snackbar.make(verticalList, getString(R.string.bluetoothNotFound), Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void setIcBluetooth(final int btnBltId){
        if (menu != null) {
            final MainActivity activity = MainActivity.getInstance();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MenuItem bltDisabled = menu.findItem(R.id.btnBltDisabled);
                    MenuItem bltActivated = menu.findItem(R.id.btnBltActivated);
                    MenuItem bltConnecting = menu.findItem(R.id.btnBltConnecting);
                    MenuItem bltConnected = menu.findItem(R.id.btnBltConnected);
                    if (bltDisabled != null && bltActivated != null && bltConnecting != null && bltConnected != null) {
                        switch (btnBltId) {
                            case R.id.btnBltDisabled:
                                // bluetooth desativado
                                bltDisabled.setVisible(true);
                                bltActivated.setVisible(false);
                                bltConnecting.setVisible(false);
                                bltConnected.setVisible(false);
                                Snackbar.make(verticalList, activity.getApplicationContext()
                                        .getString(R.string.disabled), Snackbar.LENGTH_SHORT).show();
                                return;
                            case R.id.btnBltActivated:
                                // bluetooth ativado
                                bltDisabled.setVisible(false);
                                bltActivated.setVisible(true);
                                bltConnecting.setVisible(false);
                                bltConnected.setVisible(false);
                                Snackbar.make(verticalList, activity.getApplicationContext()
                                        .getString(R.string.activated), Snackbar.LENGTH_SHORT).show();
                                return;
                            case R.id.btnBltConnecting:
                                // conectando com o robo
                                bltDisabled.setVisible(false);
                                bltActivated.setVisible(false);
                                bltConnecting.setVisible(true);
                                bltConnected.setVisible(false);
                                Snackbar.make(verticalList, activity.getApplicationContext()
                                        .getString(R.string.connecting), Snackbar.LENGTH_SHORT).show();
                                return;
                            case R.id.btnBltConnected:
                                // bluetooth conectado com o robo
                                bltDisabled.setVisible(false);
                                bltActivated.setVisible(false);
                                bltConnecting.setVisible(false);
                                bltConnected.setVisible(true);
                                Snackbar.make(verticalList, activity.getApplicationContext()
                                        .getString(R.string.connected), Snackbar.LENGTH_SHORT).show();
                                return;
                            default:
                                return;
                        }
                    }
                }
            });
        }
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
        bluetoothReceive.setActivity(this);
        checkCoarsePermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        bluetoothReceive.unregisterReceiver();
        bt.off();
        super.onDestroy();
    }

    public void showCommandInfo(int position, Command command){

        // TODO trocar por activity

        Context ctx = MainActivity.getInstance();
        if(ctx != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, R.layout.adapter_string);
            adapter.add(command.getInfo(ctx));

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx, R.style.DialogTheme);
            alertDialogBuilder.setCancelable(true)
                    .setPositiveButton(ctx.getString(R.string.ok),
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.dismiss();
                                    /// TODO
                                }
                            })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                        }
                    });
            alertDialogBuilder.setAdapter(adapter, null);
            alertDialogBuilder.setTitle(position+": "+command.getTitle());
            AlertDialog alert = alertDialogBuilder.create();
            alert.getListView().deferNotifyDataSetChanged();
            alert.show();
        }
    }

    /** Adiciona comando na lista a ser enviada para o robo **/
    public static void addCommand(Command command){
        if(command != null){
            Log.i("addCommand",command.getTitle());
            MainActivity.mainList.add(0, command);
            MainActivity.verticalList.setAdapter(new CommandAdapter(MainActivity.mainList));
            MainActivity.verticalList.scrollTo(0, 0);
            if(MainActivity.mainList.size() == 1){
                MainActivity.mainLayout.setVisibility(View.VISIBLE);
                MainActivity.emptyLayout.setVisibility(View.GONE);
            }
        }
    }

    /** Remove comando na lista a ser enviada para o robo **/
    public static void remCommand(int index){
        if(index >= 0 && index <= MainActivity.mainList.size()){
            index = MainActivity.mainList.size()-index;
            Log.i("remCommand", index+"");
            MainActivity.mainList.remove(index);
            MainActivity.verticalList.setAdapter(new CommandAdapter(MainActivity.mainList));
            if(MainActivity.mainList.isEmpty()){
                MainActivity.mainLayout.setVisibility(View.GONE);
                MainActivity.emptyLayout.setVisibility(View.VISIBLE);
            }
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
            if(bt.isOn()) {
                if(bt.isConected()) {
                    if(!MainActivity.isSending) {
                        MainActivity.isSending = true;
                        showProgress(R.string.sending);

                        String message;
                        int total = MainActivity.mainList.size();

                        // Enviar 1 a 1
        //              Command comm;
        //              for (int i = total-1; i>=0; i--) {
        //                  comm = MainActivity.mainList.get(i);
        //                  message = comm.getCodeFormatted();
        //                  Log.i("sendCommands", (total-i)+"/"+total+": "+message);
        //                  bt.sendMessage(message);
        //                  MainActivity.progressDialog.setMessage((total-i)+"/"+total+": "+message);
        //              }
                        //

                        // Enviar todos de 1 vez
                        message = "[";
                        for (int i = total - 1; i >= 0; i--) {
                            message += "\"" + MainActivity.mainList.get(i).getCodeFormatted() + "\",";
                        }
                        message = message.substring(0, message.length() - 1);
                        message = message + "]";
                        Log.i("sendCommands", total + ": " + message);
                        bt.sendMessage(message);

                        MainActivity.closeProgress();
                        MainActivity.isSending = false;

                        Snackbar.make(verticalList, verticalList.getContext().getString(R.string.sended), Snackbar.LENGTH_SHORT).show();
                        //
                    } else {
                        Snackbar.make(verticalList, verticalList.getContext().getString(R.string.alreadySending), Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(verticalList, verticalList.getContext().getString(R.string.notConnected), Snackbar.LENGTH_SHORT).show();
                }
            }else{
                Snackbar.make(verticalList, verticalList.getContext().getString(R.string.disabled), Snackbar.LENGTH_SHORT).show();
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
