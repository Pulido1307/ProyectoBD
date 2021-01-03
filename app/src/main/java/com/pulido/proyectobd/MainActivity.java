package com.pulido.proyectobd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pulido.proyectobd.Helpers.Constantes;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private Button button_login;
    private Button button_singup;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private boolean ocultarD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_login = findViewById(R.id.button_login);
        button_singup = findViewById(R.id.button_singup);
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(MainActivity.this,CrudActivity.class);
                 startActivity(intent);
                 finish();
            }
        });

        button_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialogSingUp();
            }
        });
    }

    private void showDialogSingUp() {
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

        button_cancelar_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag_usuario = false;
                boolean flag_pass = false;
                boolean flag_clave = false;
                editText_usuario_sign.setError(null);
                editText_pass_sign.setError(null);
                editText_confirm_sign.setError(null);
                editText_clave_sign.setError(null);

                if(!editText_usuario_sign.getText().toString().isEmpty()){
                    flag_usuario = true;
                }else {
                    editText_usuario_sign.setError("Campo requerido");
                }

                if( !editText_pass_sign.getText().toString().isEmpty() && !editText_confirm_sign.getText().toString().isEmpty()){
                    if (editText_pass_sign.getText().toString().equals(editText_confirm_sign.getText().toString())){
                        flag_pass = true;
                    }else {
                        editText_pass_sign.setError("La contraseñas ingresadas deben de ser iguales");
                        editText_confirm_sign.setError("La contraseñas ingresadas deben de ser iguales");
                    }
                }else {
                    editText_pass_sign.setError("Campo requerido");
                    editText_confirm_sign.setError("Campo requerido");
                }

                if(editText_clave_sign.getText().toString().equals(Constantes.CLAVE)){
                    flag_clave = true;
                }else{
                    editText_clave_sign.setError("La clave es distinta a la requerida, favor de comunicarse con los administradores");
                }

                if(flag_usuario && flag_clave && flag_pass){
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Cargando...");
                    progressDialog.show();
                    String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/signup.php?username="+editText_usuario_sign.getText().toString()+
                            "&pass="+editText_pass_sign.getText().toString();
                    url = url.replace(" ","%20");

                    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,MainActivity.this,MainActivity.this);
                    requestQueue.add(jsonObjectRequest);

                    if(ocultarD){
                        dialog.dismiss();
                    }else {
                        Toast.makeText(MainActivity.this, "Ocurrio un error favor de revisar datos y volver a intenar",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Algunos campos son inválidos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivity.this,"Error en la bd", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
        Log.i("ERROR", error.toString());
        ocultarD = false;
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(MainActivity.this,"Registro Guardado", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
        ocultarD = true;
    }
}