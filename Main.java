import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numeroDeVariaveis, numeroDeMintermos;
        do {
            System.out.printf("Digite o número de variáveis da função > ");
            numeroDeVariaveis = scanner.nextInt();
        } while (numeroDeVariaveis <= 0);
        do {
            System.out.printf("Digite o número de mintermos da função > ");
            numeroDeMintermos = scanner.nextInt();
        } while (numeroDeMintermos <= 0);
        System.out.print("Digite os mintermos da função > ");
        Set<Integer> mintermos = new HashSet<>();
        for (int i = 0; i < numeroDeMintermos; i++) {
            mintermos.add(scanner.nextInt());
        }
        FuncaoLogica funcao = new FuncaoLogica(numeroDeVariaveis, mintermos);
        QuineMcCluskey algoritmo = new QuineMcCluskey(funcao);
        System.out.print("Implicantes primos da função > ");
        System.out.println(algoritmo.encontrarImplicantesPrimos().toString());
    }
}
