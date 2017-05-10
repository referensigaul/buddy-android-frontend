package buddy.tecnologia.ws.buddy;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.AbstractResponseHandler;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class NovoAtividade extends Fragment implements View.OnClickListener{

    private Button btnCadastrarAtividade;

    private AutoCompleteTextView mNomeAtividade;
    private TextView mDataEntregaAtividade;
    private AutoCompleteTextView mValorAtividade;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Spinner sCronograma;
    private ArrayList<Cronograma> cronogramasList = new ArrayList<Cronograma>();
    private View view;
    private Cronograma selectedCronograma;
    private HttpClient httpClient = HttpClientBuilder.create().build();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        view = inflater.inflate(R.layout.fragment_novo_atividade, container, false);

        btnCadastrarAtividade = (Button)view.findViewById(R.id.btn_cadastrar_atividade);
        btnCadastrarAtividade.setOnClickListener(this);

        mNomeAtividade = (AutoCompleteTextView)view.findViewById(R.id.nome_atividade);
        mDataEntregaAtividade = (TextView)view.findViewById(R.id.data_entrega_atividade);
        mValorAtividade = (AutoCompleteTextView)view.findViewById(R.id.valor_atividade);

        buildCrnogramaSpinner();
        buildDataPIcker();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Nova Atividade");

    }

    public void buildCrnogramaSpinner(){
        CronogramaAsync taskAsync = new CronogramaAsync(UserSession.getInstance(getContext()).getUserToken());
        try {
            cronogramasList = taskAsync.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        sCronograma = (Spinner)view.findViewById(R.id.select_cronograma);
        CronogramaAdapter cAdapter = new CronogramaAdapter(getActivity(), R.id.select_cronograma, cronogramasList);
        sCronograma.setAdapter(cAdapter);

        sCronograma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                selectedCronograma = cronogramasList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });

    }

    public void buildDataPIcker(){
        mDataEntregaAtividade.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        ano, mes, dia
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String dataFormatada = dayOfMonth + "/" + month + "/" + year;
                mDataEntregaAtividade.setText(dataFormatada);
            }
        };
    }

    @Override
    public void onClick(View view){

        HttpPost clientPost = new HttpPost("http://buddy.tecnologia.ws/api/atividade");
        String token = UserSession.getInstance(getContext()).getUserToken();
        clientPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        clientPost.setHeader("Accept", "application/json");
        clientPost.setHeader("Authorization", "Bearer " + token);
        Integer codeStatus = null;
        JSONObject result = null;
        Boolean retunValue = false;

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("cronograma_id", selectedCronograma.getId()));
            nameValuePairs.add(new BasicNameValuePair("descricao", mNomeAtividade.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("valor", mValorAtividade.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("dtentrega", mDataEntregaAtividade.getText().toString()));
            clientPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(clientPost);

            String json = EntityUtils.toString(response.getEntity());
            result = new JSONObject(json);
            codeStatus = response.getStatusLine().getStatusCode();


            Fragment fragment = null;
            fragment = new Atividades();

            Bundle dados = new Bundle();
            dados.putInt("cronogramaId", Integer.parseInt(selectedCronograma.getId()));

            fragment.setArguments(dados);
            FragmentTransaction ft = this.getFragmentManager().beginTransaction();

            ft.replace(R.id.content_frame, fragment);
            ft.commit();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}

class CronogramaAsync extends AsyncTask<Void, Void, ArrayList>{

    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ArrayList<Cronograma> cronogramas = new ArrayList<Cronograma>();
    private String token;

    public CronogramaAsync(String token){
        this.token = token;
    }

    @Override
    public ArrayList<Cronograma> doInBackground(Void... params){

        HttpGet clientGet = new HttpGet("http://buddy.tecnologia.ws/api/cronograma");
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

            for(int i = 0; i < result.length(); i++){
                cronogramas.add(new Cronograma(result.getJSONObject(i).getString("id"), result.getJSONObject(i).getString("descricao")));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return cronogramas;
    }


}
