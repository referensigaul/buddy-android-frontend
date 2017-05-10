package buddy.tecnologia.ws.buddy;

/**
 * Created by Alisson on 04/05/2017.
 */

public class Cronograma {

    private String id;
    private String descricao;

    public Cronograma(String id, String descricao){
        this.id = id;
        this.descricao = descricao;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
}
