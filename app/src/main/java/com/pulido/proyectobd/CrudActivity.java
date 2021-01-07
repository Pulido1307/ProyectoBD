package com.pulido.proyectobd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.pulido.proyectobd.Helpers.Constantes;
import com.pulido.proyectobd.Modelos.Deudor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CrudActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    private FloatingActionButton floatingActionButton_add;
    private ListView listView_deudas;
    private ArrayAdapter arrayAdapterListView;
    private ArrayList<String> listaImprimir = new ArrayList<>();
    private ProgressDialog progressDialog;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private ArrayList<Deudor> listaDeudores = new ArrayList<>();

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

        listView_deudas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialogOpc(listaDeudores.get(i), i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        obtenerDatosBD();
    }

    private void showDialogOpc(Deudor deudor, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.opc_dialog, null);
        builder.setView(view).setTitle("Opciones");

        final AlertDialog dialog = builder.create();
        dialog.show();

        final Button button_actualizar  = dialog.findViewById(R.id.button_Actualizar_opc);
        final Button button_consultar = dialog.findViewById(R.id.button_consultar_opc);
        final Button button_abonar = dialog.findViewById(R.id.button_abonos_opc);

        button_actualizar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdateDeleteDeudor(deudor, position);
                dialog.dismiss();
            }
        });

        button_consultar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogConsultarDeudor(deudor, position);
                dialog.dismiss();
            }
        });

        button_abonar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAbonar(deudor, position);
                dialog.dismiss();
            }
        });
    }

    private void showDialogConsultarDeudor(Deudor deudor, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_consultar, null);
        builder.setView(view).setTitle("Información del deudor");

        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView textView_IdDeudor = dialog.findViewById(R.id.textView_idCliente_C);
        final TextView textView_Nombre = dialog.findViewById(R.id.textView_nombre_C);
        final TextView textView_Apellidos = dialog.findViewById(R.id.textView_apellidos_C);
        final TextView textView_SaldoDeusor = dialog.findViewById(R.id.textView_saldoDeudor_C);
        final TextView textView_FechaUC = dialog.findViewById(R.id.textView_fechaUC_C);
        final TextView textView_Observaciones = dialog.findViewById(R.id.textView_observaciones_C);
        final Button button_Cerrar = dialog.findViewById(R.id.button_cerrar_C);

        textView_IdDeudor.setText(textView_IdDeudor.getText()+" "+deudor.getIdDeudor());
        textView_Nombre.setText(textView_Nombre.getText()+" "+deudor.getNombre());
        textView_Apellidos.setText(textView_Apellidos.getText()+" "+deudor.getApellidos());
        textView_SaldoDeusor.setText(textView_SaldoDeusor.getText()+" "+deudor.getSaldoDeudor());
        textView_FechaUC.setText(textView_FechaUC.getText()+" "+deudor.getFechaUltimaCompra());
        textView_Observaciones.setText(textView_Observaciones.getText()+" "+deudor.getObservaciones());

        button_Cerrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void showDialogUpdateDeleteDeudor(Deudor deudor, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_update_deudor, null);
        builder.setView(view).setTitle("Actualizar y eliminar deudor");

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        final TextView textView_IdDeudor = dialog.findViewById(R.id.textView_idCliente_update);
        final TextInputLayout textInputLayout_nombre_deudor = dialog.findViewById(R.id.textInputLayout_nombre_update);
        final TextInputLayout textInputLayout_apellidos_deudor = dialog.findViewById(R.id.textInputLayout_apellidos_update);
        final TextInputLayout textInputLayout_monto_deudor = dialog.findViewById(R.id.textInputLayout_monto_update);
        final TextInputLayout textInputLayout_observaciones_deudor = dialog.findViewById(R.id.textInputLayout_observaciones_update);
        final Button button_actualizar_deudor = dialog.findViewById(R.id.button_actualizar_update);
        final Button button_eliminar_deudor = dialog.findViewById(R.id.button_eliminar_update);
        final Button button_salir = dialog.findViewById(R.id.button_salir_update);

        textView_IdDeudor.setText(textView_IdDeudor.getText()+" "+deudor.getIdDeudor());
        textInputLayout_nombre_deudor.getEditText().setText(deudor.getNombre());
        textInputLayout_apellidos_deudor.getEditText().setText(deudor.getApellidos());
        textInputLayout_monto_deudor.getEditText().setText(String.valueOf(deudor.getSaldoDeudor()));
        textInputLayout_observaciones_deudor.getEditText().setText(deudor.getObservaciones());

        button_actualizar_deudor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    requestQueue = Volley.newRequestQueue(CrudActivity.this);
                    progressDialog= new ProgressDialog(CrudActivity.this);
                    progressDialog.setMessage("Actualizando...");
                    progressDialog.show();

                    String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/actualizar.php?IdDeudor="+deudor.getIdDeudor()+"&Nombre="+textInputLayout_nombre_deudor.getEditText().getText().toString()+
                            "&Apellidos="+textInputLayout_apellidos_deudor.getEditText().getText().toString()+"&SaldoDeudor="+textInputLayout_monto_deudor.getEditText().getText().toString()+
                            "&FechaUltimaCompra="+ Constantes.obtenerFecha()+"&Observaciones="+observaciones;

                    StringRequest respuesta = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("Response", response);
                                if(response.equals("deudor modificado"))
                                {
                                    dialog.dismiss();
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "¡Se ha actualizado con exito el deudor!", Toast.LENGTH_SHORT).show();
                                    obtenerDatosBD();
                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "¡Error!, ¡No se ha actualizado el deudor!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "¡Error!, ¡No se ha eliminado el deudor!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(respuesta);
                }else {
                    Toast.makeText(CrudActivity.this, "Algunos campos son inválidos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_eliminar_deudor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                requestQueue = Volley.newRequestQueue(CrudActivity.this);
                progressDialog= new ProgressDialog(CrudActivity.this);
                progressDialog.setMessage("Eliminando...");
                progressDialog.show();

                String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/baja.php?IdDeudor="+deudor.getIdDeudor();
                StringRequest respuesta = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.trim().equals("Eliminado"))
                            {
                                dialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "¡Se ha eliminado con exito el deudor!", Toast.LENGTH_SHORT).show();
                                obtenerDatosBD();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "¡Error!, ¡No se ha eliminado el deudor!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "¡Error!, ¡No se ha eliminado el deudor!", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(respuesta);
            }
        });

        button_salir.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void showDialogAbonar(Deudor deudor, int position)
    {

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
                    requestQueue = Volley.newRequestQueue(CrudActivity.this);
                    progressDialog= new ProgressDialog(CrudActivity.this);
                    progressDialog.setMessage("Registrando...");
                    progressDialog.show();

                    String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/alta.php?Nombre="+textInputLayout_nombre_deudor.getEditText().getText().toString()+
                            "&Apellidos="+textInputLayout_apellidos_deudor.getEditText().getText().toString()+"&SaldoDeudor="+textInputLayout_monto_deudor.getEditText().getText().toString()+
                            "&FechaUltimaCompra="+ Constantes.obtenerFecha()+"&Observaciones="+observaciones;

                    url = url.replace(" ","%20");
                    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,CrudActivity.this,CrudActivity.this);
                    requestQueue.add(jsonObjectRequest);
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
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Id de deudor a buscar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                progressDialog= new ProgressDialog(CrudActivity.this);
                progressDialog.setMessage("Buscando...");
                progressDialog.show();
                requestQueue = Volley.newRequestQueue(CrudActivity.this);
                String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/buscar.php?IdDeudor="+searchView.getQuery().toString();
                StringRequest respuesta = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                       try {
                            JSONObject object = new JSONObject(response);
                            JSONArray resultados = new JSONArray(object.get("resultados").toString());
                            if(resultados.getJSONObject(0).getString("message").equals("Si retorna"))
                            {
                                JSONArray deudor = new JSONArray(object.get("deudor").toString());
                                llenarListaDeudores(deudor);
                                progressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(CrudActivity.this,"No existe un deudor con ese Id", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CrudActivity.this,"Error al buscar los datos de la bd", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(respuesta);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(searchView.getQuery().toString().isEmpty())
                {
                    obtenerDatosBD();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(CrudActivity.this,"Error en la bd"+ error.toString(), Toast.LENGTH_SHORT).show();
        progressDialog.hide();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("JSONObject: ", response.toString());
        try {
            if(response.getString("mensaje").equals("deudor creado"))
            {
                Toast.makeText(CrudActivity.this,"Deuda registrada", Toast.LENGTH_SHORT).show();
                obtenerDatosBD();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.hide();
    }

    private void obtenerDatosBD()
    {
        requestQueue = Volley.newRequestQueue(CrudActivity.this);
        String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/readAll.php";
        StringRequest respuesta = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    llenarListaDeudores(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CrudActivity.this,"Error al cargar los datos de la bd", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(respuesta);
    }

    private void llenarListaDeudores(JSONArray jsonArray) throws JSONException {
        listaDeudores.clear();
        listaImprimir.clear();
        for(int i = 0; i<jsonArray.length(); i++)
        {
            Deudor deudor = new Deudor();

            deudor.setIdDeudor(Integer.parseInt(jsonArray.getJSONObject(i).getString("IdDeudor")));
            deudor.setNombre(jsonArray.getJSONObject(i).getString("Nombre"));
            deudor.setApellidos(jsonArray.getJSONObject(i).getString("Apellidos"));
            deudor.setSaldoDeudor(Double.parseDouble(jsonArray.getJSONObject(i).getString("SaldoDeudor")));
            deudor.setFechaUltimaCompra(jsonArray.getJSONObject(i).getString("FechaUltimaCompra"));
            deudor.setObservaciones(jsonArray.getJSONObject(i).getString("Observaciones"));

            listaDeudores.add(deudor);

            String registro = "Nombre: " + deudor.getNombre() + "\n" +
                    "Saldo deudor: " + deudor.getSaldoDeudor() + "\n";

            listaImprimir.add(registro);
        }
        arrayAdapterListView.notifyDataSetChanged();
    }

}