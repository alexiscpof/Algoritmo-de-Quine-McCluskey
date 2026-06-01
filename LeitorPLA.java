import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LeitorPLA {
    public static FuncaoLogica ler(String caminho) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(caminho));
        int numeroDeVariaveis = 0;
        Set<Integer> mintermos = new HashSet<>();
        String linha;
        while ((linha = leitor.readLine()) != null) {
            linha = linha.trim();
            if (linha.isEmpty()) {
                continue;
            }
            if (linha.startsWith(".i")) {
                String[] partes = linha.split("\\s+");
                numeroDeVariaveis = Integer.parseInt(partes[1]);
                continue;
            }
            if (linha.startsWith(".")) {
                continue;
            }
            String[] partes = linha.split("\\s+");
            if (partes[1].equals("1")) {
                int mintermo = Integer.parseInt(partes[0], 2);
                mintermos.add(mintermo);
            }
        }
        leitor.close();
        return new FuncaoLogica(numeroDeVariaveis, mintermos);
    }
}
