package com.apps.er.project;

import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class productsAdapter extends ArrayAdapter<String>
{
    private ImageView imgIcon;
    private TextView txtProduct, txtDescription, txtQuantity, txtProvider, txtPrice;

    private final Activity context;
    private final Integer[] icon;
    private final String[] product;
    private final String[] description;
    private final String[] quantity;
    private final String[] provider;
    private final String[] price;

    public productsAdapter(Activity context, Integer[] pIcon, String[] pProduct, String[] pDescription, String[] pQuantity, String[] pProvider, String[] pPrice)
    {
        super(context, R.layout.products_list, pProduct);

        this.context=context;
        icon=pIcon;
        product=pProduct;
        description=pDescription;
        quantity=pQuantity;
        provider=pProvider;
        price=pPrice;
    }

    public View getView(int pos, View view, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.products_list,null,true);

        imgIcon = rowView.findViewById(R.id.icon);
        txtProduct = rowView.findViewById(R.id.txtName);
        txtDescription = rowView.findViewById(R.id.txtDescription);
        txtQuantity = rowView.findViewById(R.id.txtQuantity);
        txtProvider = rowView.findViewById(R.id.txtProvider);
        txtPrice = rowView.findViewById(R.id.txtPrice);

        imgIcon.setImageResource(icon[pos]);
        txtProduct.setText(product[pos]);
        txtDescription.setText(description[pos]);
        txtQuantity.setText(quantity[pos]);
        txtProvider.setText(provider[pos]);
        txtPrice.setText(price[pos]);

        return rowView;
    }
}
