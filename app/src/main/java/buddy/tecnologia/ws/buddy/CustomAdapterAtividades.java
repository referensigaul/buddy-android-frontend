package buddy.tecnologia.ws.buddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alisson on 05/05/2017.
 */

public class CustomAdapterAtividades extends ArrayAdapter<Atividade> {

    private Context context;

    public CustomAdapterAtividades(Context context, int resouce, ArrayList<Atividade> objects){
        super(context, resouce, objects);
    }
    @Override
    public View getView(int position, View view, ViewGroup parent){
        Atividade atividade = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.lista_atividade, parent, false);

        }

        view.setLongClickable(true);

        TextView txtDescricao = (TextView)view.findViewById(R.id.txt_nome_atividade);
        TextView txtId = (TextView)view.findViewById(R.id.txt_id_atividade);
        TextView txtDtentrega = (TextView)view.findViewById(R.id.txt_dtentrega_atividade);
        TextView txtValor = (TextView)view.findViewById(R.id.txt_valor_atividade);

        txtDescricao.setText(atividade.getDescricao());
        txtId.setText(atividade.getId());
        txtValor.setText("Valor:  " + atividade.getValor());
        txtDtentrega.setText("Entrega:  " + atividade.getDtentrega());

        return view;

    }

}
