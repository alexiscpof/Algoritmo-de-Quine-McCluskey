import java.util.*;

public class QuineMcCluskey {
    private FuncaoLogica funcao;
    public QuineMcCluskey(FuncaoLogica funcao) {
        this.funcao = funcao;
    }
    private List<Termo> gerarTermosIniciais() {
        List<Termo> termosIniciais = new ArrayList<>();
        for (Integer mintermo: funcao.getMintermos()) {
            String binario = Integer.toBinaryString(mintermo);
            String binarioTratado = String.format("%" + funcao.getNumeroDeVariaveis() + "s", binario).replace(' ', '0');
            termosIniciais.add(new Termo(binarioTratado));
        }
        return termosIniciais;
    }
    private Map<Integer, List<Termo>> agruparPorNumeroDeBits1(List<Termo> termos) {
        Map<Integer, List<Termo>> termosAgrupados = new HashMap<>();
        for (Termo termo : termos) {
            int numeroDeBits1 = termo.getNumeroDeBits1();
            termosAgrupados.putIfAbsent(numeroDeBits1, new ArrayList<>());
            termosAgrupados.get(numeroDeBits1).add(termo);
        }
        return termosAgrupados;
    }
    private Set<Termo> combinarTermosAdjacentes(List<Termo> grupo1, List<Termo> grupo2) {
        Set<Termo> combinacoes = new HashSet<>();
        for (Termo termo1 : grupo1) {
            for (Termo termo2 : grupo2) {
                if (termo1.podeCombinarCom(termo2)) {
                    combinacoes.add(termo1.combinarCom(termo2));
                    termo1.marcarComoCombinado();
                    termo2.marcarComoCombinado();
                }
            }
        }
        return combinacoes;
    }
    
}
