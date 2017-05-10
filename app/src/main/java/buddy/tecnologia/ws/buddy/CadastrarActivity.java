package buddy.tecnologia.ws.buddy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class CadastrarActivity extends AppCompatActivity {

    AutoCompleteTextView mNomeCadastrar;
    AutoCompleteTextView mEmailCadastrar;
    EditText mPasswordCadastrar;
    EditText mConfirmCadastrar;
    Button mBtnCadastrar;
    private CadastrarUsuarioTask mCadastrarTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        mNomeCadastrar = (AutoCompleteTextView) findViewById(R.id.nome_cadastrar);
        mEmailCadastrar = (AutoCompleteTextView) findViewById(R.id.email_cadastrar);
        mPasswordCadastrar = (EditText) findViewById(R.id.password_cadastrar);
        mConfirmCadastrar = (EditText) findViewById(R.id.confirm_cadastrar);
        mBtnCadastrar = (Button) findViewById(R.id.btn_cadastrar_usuario);

        mBtnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               validarCampos();
            }
        });

    }

    private void validarCampos() {

        String nome = mNomeCadastrar.getText().toString();
        String email = mEmailCadastrar.getText().toString();
        String senha = mPasswordCadastrar.getText().toString();
        String confirmaSenha = mConfirmCadastrar.getText().toString();

        if(!senha.equals(confirmaSenha)){
            Toast.makeText(getApplicationContext(), "As senhas não conferem", Toast.LENGTH_LONG).show();
        }else{

            if (!email.contains("@")){
                Toast.makeText(getApplicationContext(), "Digite um email válido", Toast.LENGTH_LONG).show();
            }else{
                mCadastrarTask = new CadastrarUsuarioTask(nome, email, senha, confirmaSenha);
                mCadastrarTask.execute((Void) null);
            }

        }


    }

    public class CadastrarUsuarioTask extends AsyncTask<Void, Void, Boolean> {

        private final String mNome;
        private final String mEmail;
        private final String mSenha;
        private final String mConfirmacao;
        private String msg;
        Boolean retunValue = false;


        public CadastrarUsuarioTask(String nome, String email, String senha, String confirmaSenha) {
            mNome = nome;
            mEmail = email;
            mSenha = senha;
            mConfirmacao = confirmaSenha;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost clientPost = new HttpPost("http://buddy.tecnologia.ws/api/user/cadastrar");
                clientPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                clientPost.setHeader("Accept", "application/json");
                Integer codeStatus = null;
                JSONObject result = null;

                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                    nameValuePairs.add(new BasicNameValuePair("name", mNome));
                    nameValuePairs.add(new BasicNameValuePair("email", mEmail));
                    nameValuePairs.add(new BasicNameValuePair("password", mSenha));
                    clientPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(clientPost);

                    String json = EntityUtils.toString(response.getEntity());
                    result = new JSONObject(json);
                    codeStatus = response.getStatusLine().getStatusCode();

                    if(codeStatus != null && codeStatus == 200) {
                        this.msg = result.getString("msg");
                        retunValue = true;
                    }else{
                        this.msg = "Falha na conexão com o servidor";
                        retunValue = false;
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            return  retunValue;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mCadastrarTask = null;
            Toast.makeText(getApplicationContext(), this.msg, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            mCadastrarTask = null;
        }

    }
}

