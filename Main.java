import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
public class Main {
    public static void main(String[] args) throws IOException {
        // Lê o arquivo especificado e cria a função que ele define
        FuncaoLogica funcao = LeitorPLA.ler("testes/funcao1.pla");
        // Inicializa o algoritmo para aquela função
        QuineMcCluskey algoritmo = new QuineMcCluskey(funcao);
        // Guarda o conjunto de termos minimizados
        Set<Termo> resultado = algoritmo.minimizar();
        // Formata o resultado
        String mensagem = resultado.stream().map(Termo::toString).collect(Collectors.joining(" + "));
        // Exibe o resultado
        System.out.println("Função minizada: " + mensagem);
    }
}