package com.apps.er.project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private UserLoginTask userLoginTask = null;
    private EditText txtEmail, txtPass;
    private TextView btnRecover, btnCreate;
    private Button btnLogin;
    private View frmLogin, progressBar;
    private String idSucursal, sucursal, dir, tel, correo, clave,  lat, lon, activado;
    public static boolean log;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        frmLogin = findViewById(R.id.frmLogin);
        progressBar = findViewById(R.id.progressBar);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPassword);
        btnRecover = (TextView) findViewById(R.id.btnRecover);
        btnCreate = (TextView) findViewById(R.id.btnCreate);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnRecover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recoverAccount(view);
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createAccount();
            }
        });
        btnLogin.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                login();
            }
        });
    }

    private void recoverAccount(View view)
    {
        final View v = view;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.recover_account, null);
        final EditText email = dialogView.findViewById(R.id.txtEmail);
        alertDialog.setView(dialogView);
        alertDialog.setTitle(getResources().getString(R.string.oRecover));
        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //Send Mail
                Snackbar.make(v, "Please see your email" +  email.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //Nothing
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void createAccount()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.create_account, null);
        final EditText name = dialogView.findViewById(R.id.txtName);
        final EditText direction = dialogView.findViewById(R.id.txtDirection);
        final EditText phone = dialogView.findViewById(R.id.txtPhone);
        final EditText email = dialogView.findViewById(R.id.txtEmail);
        final EditText pass = dialogView.findViewById(R.id.txtPassword);
        final EditText rPass = dialogView.findViewById(R.id.txtRPassword);
        alertDialog.setView(dialogView);
        alertDialog.setTitle(getResources().getString(R.string.oCreate));
        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //Create Account...
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //Nothing
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void login()
    {
        if (userLoginTask != null)
            return;

        txtEmail.setError(null);
        txtPass.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (txtEmail.getText().toString().length() > 0)
        {
            if (!isValidEmail(txtEmail.getText().toString()))
            {
                txtEmail.setError(getString(R.string.eEmail));
                focusView = txtEmail;
                cancel = true;
            }
        }
        else
        {
            txtEmail.setError(getString(R.string.required));
            focusView = txtEmail;
            cancel = true;
        }

        if (txtPass.getText().toString().length() > 0)
        {
            showProgress(true);
            userLoginTask = new UserLoginTask();
            userLoginTask.execute((Void) null);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Connection.login, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        idSucursal = jsonObject.getString("idSucursal");
                        sucursal = jsonObject.getString("sucursal");
                        dir = jsonObject.getString("dir");
                        tel = jsonObject.getString("tel");
                        correo = jsonObject.getString("correo");
                        clave = jsonObject.getString("clave");
                        lat = jsonObject.getString("lat");
                        lon = jsonObject.getString("lon");
                        activado = jsonObject.getString("activado");
                        log = true;
                    }
                    catch (JSONException e)
                    {

                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError volleyError)
                {
                    Toast.makeText(Login.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", txtEmail.getText().toString());
                    params.put("password", txtPass.getText().toString());
                    return params;
                }
            };
            Singleton.getInstance(Login.this).addToRequest(stringRequest);
            if (Integer.parseInt(activado) == 0)
            {
                //Intent i = new Intent(this, Admin.class);
                //startActivity(i);
            }
            else if (Integer.parseInt(activado) == 1)
            {
                //Intent i = new Intent(this, Customer.class);
                //startActivity(i);
            }
            /*
            if (!txtPass.getText().toString().equals(clave))
            {
                txtPass.setError(getResources().getString(R.string.ePassword));
            }
            else
            {

            }
            */
        }
        else
        {
            txtPass.setError(getResources().getString(R.string.required));
            focusView = txtPass;
            cancel = true;
        }
        if (cancel)
            focusView.requestFocus();
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            userLoginTask = null;
            showProgress(false);

            if (success)
            {
                finish();
            }
        }

        @Override
        protected void onCancelled()
        {
            userLoginTask = null;
            showProgress(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            frmLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            frmLogin.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    frmLogin.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            frmLogin.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isValidEmail(String email)
    {
        boolean state = false;
        if (email.contains("@") && email.contains(".com"))
            state = true;
        return state;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }
}

