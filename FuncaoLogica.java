import java.util.*;

public class FuncaoLogica {
    // Uma função lógica é definida pelo número de variáveis e seu mintermos (saídas 1)
    private int numeroDeVariaveis;
    private Set<Integer> mintermos;
    // Método construtor da classe
    public FuncaoLogica(int numeroDeVariaveis, Set<Integer> mintermos) {
        this.numeroDeVariaveis = numeroDeVariaveis;
        this.mintermos = mintermos;
    }
    // Métodos getters
    public int getNumeroDeVariaveis() {
        return numeroDeVariaveis;
    }
    public Set<Integer> getMintermos() {
        return mintermos;
    }
}
