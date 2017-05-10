package buddy.tecnologia.ws.buddy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alisson on 05/05/2017.
 */

public class Atividade {
    String id;
    String cronograma_id;
    String descricao;
    String valor;
    String dtentrega;

    public Atividade(String id, String cronograma_id, String descricao, String valor, String dtentrega) {
        this.id = id;
        this.cronograma_id = cronograma_id;
        this.descricao = descricao;
        this.valor = valor;
        this.dtentrega = dtentrega;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCronograma_id() {
        return cronograma_id;
    }

    public void setCronograma_id(String cronograma_id) {
        this.cronograma_id = cronograma_id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDtentrega() {
        return dtentrega;
    }

    public void setDtentrega(String dtentrega) {
        this.dtentrega = dtentrega;
    }
}
