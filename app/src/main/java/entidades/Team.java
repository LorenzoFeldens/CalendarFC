package entidades;


import java.io.Serializable;
import java.util.ArrayList;

public class Team
        implements Serializable
{
    int id;
    ArrayList<Game> jogos;
    String nome;

    public Team(String paramString, int paramInt)
    {
        this.nome = paramString;
        this.id = paramInt;
    }

    public int getId()
    {
        return this.id;
    }

    public ArrayList<Game> getJogos()
    {
        return this.jogos;
    }

    public String getNome()
    {
        return this.nome;
    }

    public void setJogos(ArrayList<Game> paramArrayList)
    {
        this.jogos = paramArrayList;
    }
}

