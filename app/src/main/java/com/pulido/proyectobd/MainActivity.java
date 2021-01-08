package com.pulido.proyectobd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.google.android.material.textfield.TextInputLayout;
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
    private ProgressDialog progressDialogLogin;
    private RequestQueue requestQueueLogin;
    private JSONArray jsonArray;
    private TextInputLayout textInput_username_login;
    private TextInputLayout textInput_pass_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_login = findViewById(R.id.button_login);
        button_singup = findViewById(R.id.button_singup);
        textInput_username_login = findViewById(R.id.textInput_username_login);
        textInput_pass_login = findViewById(R.id.textInput_pass_login);
        requestQueueSign = Volley.newRequestQueue(MainActivity.this);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag_usuario = false;
                boolean flag_pass = false;
                textInput_username_login.setError(null);
                textInput_pass_login.setError(null);

                if(!textInput_username_login.getEditText().getText().toString().isEmpty()){
                    flag_usuario = true;
                }else {
                    textInput_username_login.setError("Campo requerido");
                }

                if(!textInput_pass_login.getEditText().getText().toString().isEmpty()){
                    flag_pass = true;
                }else {
                    textInput_pass_login.setError("Campo requerido");
                }

                if(flag_usuario && flag_pass){
                    login();
                }else {
                    Toast.makeText(MainActivity.this, "Algunos campos son inválidos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSingUp();
            }
        });
    }

    private void login(){
        progressDialogLogin = new ProgressDialog(MainActivity.this);
        progressDialogLogin.setMessage("Cargando...");
        progressDialogLogin.show();

        requestQueueLogin = Volley.newRequestQueue(MainActivity.this);
        String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/login.php?username="+textInput_username_login.getEditText().getText().toString().trim();
        StringRequest respuesta = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    String password = jsonArray.getString(0);

                    if (textInput_pass_login.getEditText().getText().toString().equals(password)) {
                        Toast.makeText(MainActivity.this, "Bienvenid@ " + textInput_username_login.getEditText().getText().toString(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, CrudActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialogLogin.hide();
                    } else {
                        progressDialogLogin.hide();
                        showInformationDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showInformationDialog();
                    progressDialogLogin.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueLogin.add(respuesta);
    }

    private void showInformationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Inicio de sesión denegado").
                setMessage("Usuario o contraseña erróneos").
                setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    private void showDialogSingUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View view =inflater.inflate(R.layout.dialog_registrarse, null);
        builder.setView(view).setTitle("Registrar usuario");

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        final TextInputLayout textInputLayout_usuario_sign = dialog.findViewById(R.id.textInputLayout_usuario_sign);
        final TextInputLayout textInputLayout_pass_sign = dialog.findViewById(R.id.textInputLayout_pass_sign);
        final TextInputLayout textInputLayout_confirm_sign = dialog.findViewById(R.id.textInputLayout_confirm_sign);
        final TextInputLayout textInputLayout_clave_sign = dialog.findViewById(R.id.textInputLayout_clave_sign);
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
                textInputLayout_usuario_sign.setError(null);
                textInputLayout_pass_sign.setError(null);
                textInputLayout_confirm_sign.setError(null);
                textInputLayout_clave_sign.setError(null);

                if(!textInputLayout_usuario_sign.getEditText().getText().toString().isEmpty()){
                    flag_usuario = true;
                }else {
                    textInputLayout_usuario_sign.setError("Campo requerido");
                }

                if( !textInputLayout_pass_sign.getEditText().getText().toString().isEmpty() && !textInputLayout_confirm_sign.getEditText().getText().toString().isEmpty()){
                    if (textInputLayout_pass_sign.getEditText().getText().toString().equals(textInputLayout_confirm_sign.getEditText().getText().toString())){
                        flag_pass = true;
                    }else {
                        textInputLayout_pass_sign.setError("La contraseñas ingresadas deben de ser iguales");
                        textInputLayout_confirm_sign.setError("La contraseñas ingresadas deben de ser iguales");
                    }
                }else {
                    textInputLayout_pass_sign.setError("Campo requerido");
                    textInputLayout_confirm_sign.setError("Campo requerido");
                }

                if(textInputLayout_clave_sign.getEditText().getText().toString().equals(Constantes.CLAVE)){
                    flag_clave = true;
                }else{
                    textInputLayout_clave_sign.setError("La clave es distinta a la requerida, favor de comunicarse con los administradores");
                }

                if(flag_usuario && flag_clave && flag_pass){
                    progressDialogSign = new ProgressDialog(MainActivity.this);
                    progressDialogSign.setMessage("Cargando...");
                    progressDialogSign.show();
                    String url = "https://proyectobasedatositsu.000webhostapp.com/Servicios/signup.php?username="+textInputLayout_usuario_sign.getEditText().getText().toString()+
                            "&pass="+textInputLayout_pass_sign.getEditText().getText().toString();
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
        Toast.makeText(MainActivity.this,"Registro guardado", Toast.LENGTH_SHORT).show();
        progressDialogSign.hide();
    }


}