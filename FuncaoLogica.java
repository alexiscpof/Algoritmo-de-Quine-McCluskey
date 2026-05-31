import java.util.*;

public class FuncaoLogica {
    private int numeroDeVariaveis;
    private Set<Integer> mintermos;
    public FuncaoLogica(int numeroDeVariaveis, Set<Integer> mintermos) {
        this.numeroDeVariaveis = numeroDeVariaveis;
        this.mintermos = mintermos;
    }
    public int getNumeroDeVariaveis() {
        return numeroDeVariaveis;
    }
    public Set<Integer> getMintermos() {
        return mintermos;
    }
}
