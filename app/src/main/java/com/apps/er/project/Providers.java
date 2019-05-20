package com.apps.er.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Providers extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getProviders();
    }
/*
    private void getProviders()
    {
        StringRequest strRequest = new StringRequest(Request.Method.GET, Conexion.getProviders, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                JSONObject j = null;
                try
                {
                    JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            String codeNombre = jsonObject.getString("institucion");

                            if(codeNombre.equals("1")){
                                // no hay instituciones
                                institucionArrayList.add(sinInstitucion);
                                btnRegistro.setEnabled(false);
                                spinner.setAdapter(new ArrayAdapter<>(RegistroUsuarioActivity.this, android.R.layout.simple_spinner_dropdown_item, institucionArrayList));

                            }else{
                                // si hay instituciones
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject item = jsonArray.getJSONObject(i);
                                        String nombre = item.getString("institucion");
                                        String miId = item.getString("id_institucion");
                                        institucionArrayList.add(nombre);
                                        //arrayID.add(miId);
                                        spinner.setAdapter(new ArrayAdapter<>(RegistroUsuarioActivity.this, android.R.layout.simple_spinner_dropdown_item, institucionArrayList));

                                    } catch (JSONException e) {
                                    }
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(RegistroUsuarioActivity.this, "no hay internet", Toast.LENGTH_SHORT).show();
                    }
                });

        Singleton.getmInstance(RegistroUsuarioActivity.this).addToRequest(strRequest);
    }
*/
}
