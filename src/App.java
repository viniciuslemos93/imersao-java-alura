import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        //Consumindo a API de filmes.
        //Fazer uma conexão HTTP e buscar os top 250 filmes.
        String url = "https://alura-imdb-api.herokuapp.com/movies";
        /* Lista de APIs para utilização (caso esteja fora)
         * https://imdb-api.com/
         * (https://alura-imdb-api.herokuapp.com/movies) criada pelo Jhon Santana
         * (https://api.mocki.io/v2/549a5d8b) criada pelo instrutor Alexandre Aquiles
         * (https://alura-filmes.herokuapp.com/conteudos) criada pela instrutora Jacqueline Oliveira
         * (https://raw.githubusercontent.com/alexfelipe/imersao-java/json/top250.json) criada pelo instrutor Alex Felipe
         */
        URI endereco = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();        

        //Extrair só os dados que interessam (título, poster, classificação) - parsear os dados.
        var parser = new JasonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);
        
        //Exibir e manipular os dados.
        var geradora = new GeradoraDeFigurinhas();
        
        for (Map<String,String> filme : listaDeFilmes) {

            String urlImagem = filme.get("image");
            String titulo = filme.get("title");

            InputStream inputStream = new URL(urlImagem).openStream();
            String nomeArquivo = titulo + ".png";

            geradora.cria(inputStream, nomeArquivo);

            System.out.println(titulo);
            System.out.println();
        }
    }
}
