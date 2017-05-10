package buddy.tecnologia.ws.buddy;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Cronogramas extends Fragment implements View.OnClickListener{

    private Button btnNovoCronograma;
    private ListView listCronograma;
    private ArrayList<Cronograma> cronogramas = new ArrayList<Cronograma>();
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private CustomAdapterCronogramas cCronogramas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        View view = inflater.inflate(R.layout.fragment_cronogramas, container, false);
        btnNovoCronograma = (Button)view.findViewById(R.id.btn_novo_cronograma);
        btnNovoCronograma.setOnClickListener(this);

        listCronograma = (ListView)view.findViewById(R.id.listCronograma);

        registerForContextMenu(listCronograma);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cronogramas");

        findCronogramas();
    }

    @Override
    public void onClick(View view){
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new NovoCronograma());
        ft.commit();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Opções");
        menu.add(0, view.getId(), 0, "Deletar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        if(item.getTitle() == "Deletar"){

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;

            TextView txtId = (TextView) listCronograma.getChildAt(index).findViewById(R.id.txt_id_cronograma);
            String id = txtId.getText().toString();

            deleteCronograma(id);
        }
        return true;
    }


    public void updateUI(ArrayList<Cronograma> itens){
        cCronogramas.clear();

        if (itens != null){
            for(Object obj :  itens){
                cCronogramas.insert((Cronograma)obj, cCronogramas.getCount());
            }
        }

        cCronogramas.notifyDataSetChanged();
    }

    public void findCronogramas(){

        HttpGet clientGet = new HttpGet("http://buddy.tecnologia.ws/api/cronograma");
        String token = UserSession.getInstance(getContext()).getUserToken();
        clientGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        clientGet.setHeader("Accept", "application/json");
        clientGet.setHeader("Authorization", "Bearer " + token);
        Integer codeStatus = null;
        Boolean retunValue = false;

        try {

            HttpResponse response = httpClient.execute(clientGet);

            String json = EntityUtils.toString(response.getEntity());
            JSONArray result = new JSONArray(json);
            codeStatus = response.getStatusLine().getStatusCode();

            if(codeStatus == null && codeStatus != 201){
                Toast toast = Toast.makeText(getContext(), "Falha ao cadastrar Cronograma",Toast.LENGTH_SHORT);
                toast.show();
            }

            for(int i = 0; i < result.length(); i++){
                cronogramas.add(new Cronograma(result.getJSONObject(i).getString("id"), result.getJSONObject(i).getString("descricao")));
            }

            cCronogramas = new CustomAdapterCronogramas(getContext(), 0, cronogramas, this);

            listCronograma.setAdapter(cCronogramas);



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteCronograma(String id){

        HttpDelete clientGet = new HttpDelete("http://buddy.tecnologia.ws/api/cronograma/"+id);
        String token = UserSession.getInstance(getContext()).getUserToken();
        clientGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
        clientGet.setHeader("Accept", "application/json");
        clientGet.setHeader("Authorization", "Bearer " + token);
        Integer codeStatus = null;
        Boolean retunValue = false;

        try {

            HttpResponse response = httpClient.execute(clientGet);
            String json = EntityUtils.toString(response.getEntity());
            JSONObject result = new JSONObject(json);
            codeStatus = response.getStatusLine().getStatusCode();


            String erro = result.getString("erro");
            String msg = result.getString("msg");


            Toast toast = Toast.makeText(getContext(), msg,Toast.LENGTH_LONG);
            toast.show();


            updateUI(cronogramas);
            findCronogramas();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
