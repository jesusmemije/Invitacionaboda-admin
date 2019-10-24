package com.example.pice.invitacionabodaadmin.NEVEGACION;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.pice.invitacionabodaadmin.NEVEGACION.ScannerQR;
import com.example.pice.invitacionabodaadmin.R;


public class LectorFragment extends Fragment {
    View view;
    ImageButton iv_qr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lector, container, false);

        iv_qr = (ImageButton) view.findViewById(R.id.iv_qr);
        iv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScannerQR.class);
                startActivity(intent);
            }
        });

        return view;


    }

}

