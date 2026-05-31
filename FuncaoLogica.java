import java.util.*;

public class FuncaoLogica {
    private int numeroDeVariaveis;
    private Set<Long> mintermos;
    public FuncaoLogica(int numeroDeVariaveis, Set<Long> mintermos) {
        this.numeroDeVariaveis = numeroDeVariaveis;
        this.mintermos = mintermos;
    }
    public int getNumeroDeVariaveis() {
        return numeroDeVariaveis;
    }
    public Set<Long> getMintermos() {
        return mintermos;
    }
}
