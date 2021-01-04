package com.pulido.proyectobd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.pulido.proyectobd.Helpers.Constantes;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Properties;

public class CrudActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    private FloatingActionButton floatingActionButton_add;
    private ListView listView_deudas;
    private ArrayAdapter arrayAdapterListView;
    private ArrayList<String> listaImprimir = new ArrayList<>();
    private ProgressDialog progressDialogRegistrar;
    private JsonObjectRequest jsonObjectRequestCreate;
    private RequestQueue requestQueueCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        floatingActionButton_add = findViewById(R.id.floatingActionButton_add);
        listView_deudas = findViewById(R.id.listView_deudas);

        arrayAdapterListView = new ArrayAdapter(CrudActivity.this, android.R.layout.simple_list_item_1, listaImprimir);
        listView_deudas.setAdapter(arrayAdapterListView);

        floatingActionButton_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddDeudor();
            }
        });
    }

    private void showDialogAddDeudor(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_add_deudor, null);
        builder.setView(view).setTitle("Registrar deudor");

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        final TextInputLayout textInputLayout_nombre_deudor = dialog.findViewById(R.id.textInputLayout_nombre_deudor);
        final TextInputLayout textInputLayout_apellidos_deudor = dialog.findViewById(R.id.textInputLayout_apellidos_deudor);
        final TextInputLayout textInputLayout_monto_deudor = dialog.findViewById(R.id.textInputLayout_monto_deudor);
        final TextInputLayout textInputLayout_observaciones_deudor = dialog.findViewById(R.id.textInputLayout_observaciones_deudor);
        final Button button_add_deudor = dialog.findViewById(R.id.button_add_deudor);
        final Button button_salir_deudor = dialog.findViewById(R.id.button_salir_deudor);

        button_salir_deudor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button_add_deudor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag_nombre = false;
                boolean flag_apellidos = false;
                boolean flag_monto = false;
                String observaciones = "";

                textInputLayout_nombre_deudor.setError(null);
                textInputLayout_apellidos_deudor.setError(null);
                textInputLayout_nombre_deudor.setError(null);
                textInputLayout_observaciones_deudor.setError(null);

                if(!textInputLayout_nombre_deudor.getEditText().getText().toString().isEmpty()){
                    flag_nombre = true;
                }else {
                    textInputLayout_nombre_deudor.setError("Campo requerido");
                }

                if(!textInputLayout_apellidos_deudor.getEditText().getText().toString().isEmpty()){
                    flag_apellidos = true;
                }else {
                    textInputLayout_apellidos_deudor.setError("Campo requerido");
                }

                if(!textInputLayout_monto_deudor.getEditText().getText().toString().isEmpty()){
                    flag_monto = true;
                }else {
                    textInputLayout_monto_deudor.setError("Campo requerido");
                }

                if(!textInputLayout_observaciones_deudor.getEditText().getText().toString().isEmpty()){
                    observaciones = textInputLayout_observaciones_deudor.getEditText().getText().toString();
                }else {
                    observaciones = "Sin observaciones";
                }

                if(flag_nombre && flag_apellidos && flag_monto){
                    requestQueueCreate = Volley.newRequestQueue(CrudActivity.this);
                    progressDialogRegistrar= new ProgressDialog(CrudActivity.this);
                    progressDialogRegistrar.setMessage("Registrando...");
                    progressDialogRegistrar.show();

                    String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/alta.php?Nombre="+textInputLayout_nombre_deudor.getEditText().getText().toString()+
                            "&Apellidos="+textInputLayout_apellidos_deudor.getEditText().getText().toString()+"&SaldoDeudor="+textInputLayout_monto_deudor.getEditText().getText().toString()+
                            "&FechaUltimaCompra="+ Constantes.obtenerFecha()+"&Observaciones="+observaciones;

                    url = url.replace(" ","%20");
                    jsonObjectRequestCreate = new JsonObjectRequest(Request.Method.GET,url,null,CrudActivity.this,CrudActivity.this);
                    requestQueueCreate.add(jsonObjectRequestCreate);
                    dialog.dismiss();
                }else {
                    Toast.makeText(CrudActivity.this, "Algunos campos son inválidos",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(CrudActivity.this,"Error en la bd "+ error.toString(), Toast.LENGTH_SHORT).show();
        progressDialogRegistrar.hide();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(CrudActivity.this,"Deuda registrada", Toast.LENGTH_SHORT).show();
        progressDialogRegistrar.hide();
    }

}