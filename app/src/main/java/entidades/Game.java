package entidades;

public class Game
{
    String data;
    String titulo;

    public Game(String paramString1, String paramString2)
    {
        this.titulo = paramString1;
        this.data = paramString2;
    }

    public String getData()
    {
        return this.data;
    }

    public String getTitulo()
    {
        return this.titulo;
    }
}
