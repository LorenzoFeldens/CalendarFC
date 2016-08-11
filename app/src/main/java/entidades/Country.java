package entidades;


public class Country
{
    int id;
    String nome;

    public Country(String paramString, int paramInt)
    {
        this.nome = paramString;
        this.id = paramInt;
    }

    public int getId()
    {
        return this.id;
    }

    public String getNome()
    {
        return this.nome;
    }
}
