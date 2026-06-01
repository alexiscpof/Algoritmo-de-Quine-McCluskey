import java.util.*;

public class Termo {
    // Representação do termo, podendo ser composto por '1's, '0's e '-'s
    private String representacao;
    // Este conjunto armazena todos os mintermos que estão sendo contemplados por este termo
    private Set<Integer> mintermosContemplados;
    // Atributo de controle, indica se este termo foi usado em alguma combinação na etapa atual 
    private boolean combinado;
    // Na criação de um termo, calcula os mintermos contemplados
    public Termo(String representacao) {
        this.representacao = representacao;
        combinado = false;
        mintermosContemplados = new HashSet<>();
        determinarMintermosContemplados("", 0);
    }
    /* Método para determinar quais mintermos estão sendo contemplados por este termo. Ele percorre a representação do termo
    e, ao encontrar o caractere '-', gera dois caminhos, em um deles substitui '-' por '1', e no outro, substitui por '0' */ 
    private void determinarMintermosContemplados(String termoAtual, int indice) {
        /* Quando já percorreu toda a representação, significa que o mintermo já foi obtido e pode ser adicionado à 
        lista de mintermos contemplados */
        if (indice == representacao.length()) {
            // Converte a representação em String para número inteiro 
            Integer mintermoContemplado = Integer.parseInt(termoAtual, 2);
            // Adiciona à lista de mintermos
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
            // Para cada diferença entre bits, incrementa o contador
            if (representacao.charAt(i) != outroTermo.getRepresentacao().charAt(i))
                numeroDeBitsDiferentes++;
        }
        // retorna True quando apenas 1 bit diferiu entre os termos
        return numeroDeBitsDiferentes == 1;
    }
    // Método que retorna a combinação deste termo com outro
    public Termo combinarCom(Termo outroTermo) {
        /* A lógica consiste em percorrer as representações de ambos os termos e copia os caracteres, exceto quando encontrar
        o bit diferente. Quando isso acontece, adiciona o caractere '-' no lugar.*/
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
    // Método que retorna o número de literais da representação. Literais são bits '0' ou '1', ignorando o caractere '-'
    public int getNumeroDeLiterais() {
        int numeroDeLiterais = 0;
        for (char caractere : representacao.toCharArray()) {
            if (caractere != '-')
                numeroDeLiterais++;
        }
        return numeroDeLiterais;
    }
    /* Método para marcar este termo como combinado, indicando que ele participou de ao menos uma combinação, portanto, 
    não é candidato a implicante primo */
    public void marcarComoCombinado() {
        combinado = true;
    }
    // Métodos getters
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
    // Reescreve os método hashCode() e equals() para que objetos desta classe possam se comportar corretamente dentro de um Set
    @Override
    public int hashCode() {
        // Usa o hashCode da String
        return representacao.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        // Compara os termos pela representação
        Termo outro = (Termo) obj;
        return representacao.equals(outro.representacao);
    }
}
