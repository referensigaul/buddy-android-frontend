package buddy.tecnologia.ws.buddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alisson on 04/05/2017.
 */

public class CustomAdapterCronogramas extends ArrayAdapter<Cronograma>{

    private Context context;
    private Fragment fragmentCronograma;

    public CustomAdapterCronogramas(Context context, int resouce, ArrayList<Cronograma> objects, Fragment fragment){
        super(context, resouce, objects);
        this.fragmentCronograma = fragment;

    }

    @Override
    public View getView(int position, View view, final ViewGroup parent){
        final Cronograma cronograma = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.lista_cronograma, parent, false);

        }

        view.setLongClickable(true);


        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView txtId = (TextView) v.findViewById(R.id.txt_id_cronograma);
                Integer id = Integer.parseInt(String.valueOf(txtId.getText()));

                Bundle bundle = new Bundle();
                bundle.putInt("cronogramaId", id);

                Fragment fragment = null;
                fragment = new Atividades();
                fragment.setArguments(bundle);
                FragmentTransaction ft = fragmentCronograma.getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

            }
        });



        TextView txtDescricao = (TextView)view.findViewById(R.id.txt_nome_cronograma);
        TextView txtId = (TextView)view.findViewById(R.id.txt_id_cronograma);

        txtDescricao.setText(cronograma.getDescricao());
        txtId.setText(cronograma.getId());

        return view;

    }
}
