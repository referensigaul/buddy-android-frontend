package buddy.tecnologia.ws.buddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class NovoCronograma extends Fragment implements View.OnClickListener{

    private Button btnCadastrarCronograma;
    private AutoCompleteTextView mNomeCronograma;
    private HttpClient httpClient = HttpClientBuilder.create().build();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        View view = inflater.inflate(R.layout.fragment_novo_cronograma, container, false);

        btnCadastrarCronograma = (Button)view.findViewById(R.id.btn_cadastrar_cronograma);
        btnCadastrarCronograma.setOnClickListener(this);
        mNomeCronograma = (AutoCompleteTextView)view.findViewById(R.id.nome_cronograma);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Novo Cronograma");

    }

    @Override
    public void onClick(View view){


        HttpPost clientPost = new HttpPost("http://buddy.tecnologia.ws/api/cronograma");
        String token = UserSession.getInstance(getContext()).getUserToken();
        clientPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        clientPost.setHeader("Accept", "application/json");
        clientPost.setHeader("Authorization", "Bearer " + token);
        Integer codeStatus = null;
        JSONObject result = null;
        Boolean retunValue = false;

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("descricao", mNomeCronograma.getText().toString()));
            clientPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(clientPost);

            String json = EntityUtils.toString(response.getEntity());
            result = new JSONObject(json);
            codeStatus = response.getStatusLine().getStatusCode();

            getFragmentManager().popBackStack();

            if (codeStatus == null || codeStatus != 201){
                Toast toast = Toast.makeText(getContext(), "Falha ao cadastrar Cronograma",Toast.LENGTH_LONG);
                toast.show();
            }else{
                Fragment fragment = null;
                fragment = new Cronogramas();
                FragmentTransaction ft = this.getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}
