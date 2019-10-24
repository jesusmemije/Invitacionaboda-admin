package com.example.pice.invitacionabodaadmin.CLASES;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pice.invitacionabodaadmin.CLASES.ConstructorInvitado;
import com.example.pice.invitacionabodaadmin.R;

import java.util.List;

public class ControladorAcompanante extends BaseAdapter {

    private Context mContext;
    private List<ConstructorInvitado> listaInvitados;

    public ControladorAcompanante(Context mContext, List<ConstructorInvitado> listaInvitados) {
        this.mContext = mContext;
        this.listaInvitados = listaInvitados;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.lv_invitado, null);

        TextView tvApodo = (TextView) v.findViewById(R.id.tvApodo);
        TextView tvMesa = (TextView) v.findViewById(R.id.tvMesa);
        TextView tvAcompanante = (TextView) v.findViewById(R.id.tvNombre);
        TextView tvApellido = (TextView) v.findViewById(R.id.tvApellido);

        tvApodo.setText(listaInvitados.get(position).getApodo());
        tvMesa.setText(listaInvitados.get(position).getMesa());
        tvAcompanante.setText(listaInvitados.get(position).getNombre());
        tvApellido.setText(listaInvitados.get(position).getApellido());

        v.setTag(listaInvitados.get(position).getId());

        return v;
    }

    @Override
    public int getCount() {
        return listaInvitados.size();
    }

    @Override
    public Object getItem(int position) {
        return listaInvitados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




}
