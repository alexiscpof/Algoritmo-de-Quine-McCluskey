import java.util.*;

public class Termo {
    private String representacao;
    private Set<Integer> mintermosContemplados;
    private boolean combinado;
    public Termo(String representacao) {
        this.representacao = representacao;
        combinado = false;
        mintermosContemplados = new HashSet<>();
        determinarMintermosContemplados("", 0);
    }
    /* Método para determinar quais mintermos estão sendo contemplados pelo termo */ 
    private void determinarMintermosContemplados(String termoAtual, int indice) {
        /* Se o índice for maior que o número de caracteres da representação, significa que o mintermos já foi obtido
        e já pode ser adicionado à lista de mintermos contemplados */
        if (indice == representacao.length()) {
            /* Conversão de uma String para número inteiro */
            Integer mintermoContemplado = Integer.parseInt(termoAtual, 2);
            mintermosContemplados.add(mintermoContemplado);
            return;
        }
        char caractereAtual = representacao.charAt(indice);
        /* Se o caractere atual for '-' chama a função recursivamente, escrevendo '0' e '1' e avançando o índice.
        Caso contrário, escreve o caractere atual, chama a função recursivamente, escrevendo o caractere e avançando o índice */
        if (caractereAtual == '-') {
            determinarMintermosContemplados(termoAtual + '1', indice + 1);
            determinarMintermosContemplados(termoAtual + '0', indice + 1);
        } else {
            determinarMintermosContemplados(termoAtual + caractereAtual, indice + 1);
        }
    }
    /* Método que verifica se dois termos podem ser combinados. Dois termos podem ser combinados quando a diferença entre um 
    e o outro se dá em apenas um bit */
    public boolean podeCombinarCom(Termo outroTermo) {
        int numeroDeBitsDiferentes = 0;
        for (int i = 0; i < representacao.length(); i++) {
            if (representacao.charAt(i) != outroTermo.getRepresentacao().charAt(i))
                numeroDeBitsDiferentes++;
        }
        return numeroDeBitsDiferentes == 1;
    }
    // Método que retorna a combinação deste termo com outro
    public Termo combinarCom(Termo outroTermo) {
        char caractereAtual;
        StringBuilder termoCombinado = new StringBuilder();
        for (int i = 0; i < representacao.length(); i++) {
            caractereAtual = representacao.charAt(i);
            if (caractereAtual != outroTermo.getRepresentacao().charAt(i))
                termoCombinado.append('-');
            else
                termoCombinado.append(caractereAtual);
        }
        return new Termo(termoCombinado.toString());
    }
    // Método que retorna o número de bits com valor 1 presentes na representação do termo
    public int getNumeroDeBits1() {
        int numeroDeBits1 = 0;
        for (char caractere : representacao.toCharArray()) {
            if (caractere == '1')
                numeroDeBits1++;
        }
        return numeroDeBits1;
    }
    public void marcarComoCombinado() {
        combinado = true;
    }
    public boolean getCombinado() {
        return combinado;
    }
    public String getRepresentacao() {
        return representacao;
    }
    public Set<Integer> getMintermosContemplados() {
        return mintermosContemplados;
    }
    @Override
    public String toString() {
        return representacao;
    }
}
