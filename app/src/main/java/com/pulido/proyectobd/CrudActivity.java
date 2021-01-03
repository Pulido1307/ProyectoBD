package com.pulido.proyectobd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CrudActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton_add;
    private ListView listView_deudas;
    private ArrayAdapter arrayAdapterListView;
    private ArrayList<String> listaImprimir = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        floatingActionButton_add = findViewById(R.id.floatingActionButton_add);
        listView_deudas = findViewById(R.id.listView_deudas);

        arrayAdapterListView = new ArrayAdapter(CrudActivity.this, android.R.layout.simple_list_item_1, listaImprimir);
        listView_deudas.setAdapter(arrayAdapterListView);

        consultar();

        floatingActionButton_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOpc();
            }
        });

    }

    private void consultar(){
        listaImprimir.add("Nombre:Anel\nApellidos:Cuevas Duran");
        listaImprimir.add("Nombre:Antonio\nApellidos:Pulido");
        listaImprimir.add("Nombre:Vannesa\nApellidos:Cuevas Duran");
    }

    public void showDialogOpc(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_registrarse, null);
        builder.setView(view).setTitle("Registrar usuario");

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        final EditText editText_usuario_sign = dialog.findViewById(R.id.editText_usuario_sign);
        final EditText editText_pass_sign = dialog.findViewById(R.id.editText_pass_sign);
        final EditText editText_confirm_sign = dialog.findViewById(R.id.editText_confirm_sign);
        final EditText editText_clave_sign = dialog.findViewById(R.id.editText_clave_sign);
        final Button button_sign = dialog.findViewById(R.id.button_sign);
        final Button button_cancelar_sign = dialog.findViewById(R.id.button_cancelar_sign);

        button_cancelar_sign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }

}