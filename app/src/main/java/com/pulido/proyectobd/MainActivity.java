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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pulido.proyectobd.Helpers.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private Button button_login;
    private Button button_singup;
    private ProgressDialog progressDialogSign;
    private RequestQueue requestQueueSign;
    private JsonObjectRequest jsonObjectRequest;
    private EditText editText_username_login;
    private EditText editText_pass_login;
    private ProgressDialog progressDialogLogin;
    private RequestQueue requestQueueLogin;
    private JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_login = findViewById(R.id.button_login);
        button_singup = findViewById(R.id.button_singup);
        editText_pass_login = findViewById(R.id.editText_pass_login);
        editText_username_login = findViewById(R.id.editText_username_login);
        requestQueueSign = Volley.newRequestQueue(MainActivity.this);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    progressDialogSign = new ProgressDialog(MainActivity.this);
                    progressDialogSign.setMessage("Cargando...");
                    progressDialogSign.show();
                    String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/signup.php?username="+editText_usuario_sign.getText().toString()+
                            "&pass="+editText_pass_sign.getText().toString();
                    url = url.replace(" ","%20");

                    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,MainActivity.this,MainActivity.this);
                    requestQueueSign.add(jsonObjectRequest);

                    dialog.dismiss();

                }else {
                    Toast.makeText(MainActivity.this, "Algunos campos son inválidos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MainActivity.this,"Error en la bd", Toast.LENGTH_SHORT).show();
        progressDialogSign.hide();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(MainActivity.this,"Registro Guardado", Toast.LENGTH_SHORT).show();
        progressDialogSign.hide();
    }
}