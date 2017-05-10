package buddy.tecnologia.ws.buddy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Alisson on 05/05/2017.
 */

public class CronogramaAdapter extends ArrayAdapter<Cronograma> {

    private Activity context;
    ArrayList<Cronograma> data = null;

    public CronogramaAdapter(Activity context, int resource, ArrayList<Cronograma> data){
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        Cronograma cronograma = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.cronograma_spinner, parent, false);
        }

        Cronograma item = data.get(position);

        if (item != null){
            TextView cronogramaName = (TextView) view.findViewById(R.id.item_name);
            if (cronogramaName != null){
                cronogramaName.setText((item.getDescricao()));
            }
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent){
        View row = view;

        if (row == null){
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.cronograma_spinner, parent, false);
        }

        Cronograma item = data.get(position);

        if (item != null){
            TextView cronogramaName = (TextView)row.findViewById(R.id.item_name);
            TextView cronogramaId = (TextView)row.findViewById(R.id.item_id);

            if (cronogramaName != null){
                cronogramaName.setText((item.getDescricao()));
            }

            if (cronogramaId != null){
                cronogramaId.setText((item.getId()));
            }
        }

        return row;

    }
}
