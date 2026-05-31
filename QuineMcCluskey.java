import java.util.*;

public class QuineMcCluskey {
    private FuncaoLogica funcao;
    private Set<Termo> implicantesPrimos;
    private Set<Termo> implicantesPrimosEssenciais;
    public QuineMcCluskey(FuncaoLogica funcao) {
        this.funcao = funcao;
        implicantesPrimos = new HashSet<>();
    }
    // Na primeira iteração do algoritmo, quando nenhuma combinação foi realizada, os termos são os próprios implicantes de cada mintermo da função
    private List<Termo> gerarTermosIniciais() {
        // Lista que possui o termos iniciais do algoritmo
        List<Termo> termosIniciais = new ArrayList<>();
        // Percorre cada mintermo da função, obtém sua representação em String e normaliza o resultado. Após isso, insere na lista
        for (Integer mintermo: funcao.getMintermos()) {
            // Converte o valor inteiro decimal para String em binário
            String binario = Integer.toBinaryString(mintermo);
            // Adiciona zeros à esquerda quando necessário
            String binarioTratado = String.format("%" + funcao.getNumeroDeVariaveis() + "s", binario).replace(' ', '0');
            // Insere na lista
            termosIniciais.add(new Termo(binarioTratado));
        }
        // Retorna os termos iniciais do algoritmo
        return termosIniciais;
    }
    // Este método recebe um conjunto de termos e os agrupa por número de bits de valor 1
    private Map<Integer, List<Termo>> agruparPorNumeroDeBits1(Set<Termo> termos) {
        // Cria um mapeamento, com o número de bits de valor 1 sendo a chave e uma lista com o termos correspondentes sendo o valor
        Map<Integer, List<Termo>> termosAgrupados = new HashMap<>();
        // Percorre o conjunto de termos
        for (Termo termo : termos) {
            // Obtém do número de bits de valor 1 do termo atual
            int numeroDeBits1 = termo.getNumeroDeBits1();
            // Cria um mapeamento, caso ainda não exista, usando aquele número de bits 1 como chave
            termosAgrupados.putIfAbsent(numeroDeBits1, new ArrayList<>());
            // Adiciona esse termo no grupo correspondente
            termosAgrupados.get(numeroDeBits1).add(termo);
        }
        // Retorna o mapeamento
        return termosAgrupados;
    }
    // Método para combinar os termos de dois grupos adjacentes
    private Set<Termo> combinarTermosAdjacentes(List<Termo> grupo1, List<Termo> grupo2) {
        // Cria um conjunto que possuirá todas as combinações resultantes
        Set<Termo> combinacoes = new HashSet<>();
        // Percorre os grupos com uso de loops aninhados
        for (Termo termo1 : grupo1) {
            for (Termo termo2 : grupo2) {
                // Caso seja possível combinar os termos, adiciona o resultado da combinação na lista e marca ambos como combinados
                if (termo1.podeCombinarCom(termo2)) {
                    combinacoes.add(termo1.combinarCom(termo2));
                    termo1.marcarComoCombinado();
                    termo2.marcarComoCombinado();
                }
            }
        }
        // Retorna a lista de combinações
        return combinacoes;
    }
    // Método para realizar todas as combinações possíveis em determinada iteração do algoritmo
    private Set<Termo> realizarCombinacoes(Map<Integer, List<Termo>> grupos) {
        // Cria um conjunto que possuirá todas as combinações realizadas por cada grupo de termos adjacentes
        Set<Termo> termosCombinados = new HashSet<>();
        // Percorre todas as chave e verifica se ela possui um grupo adjacente superior
        for (Integer chave : grupos.keySet()) {
            if (grupos.containsKey(chave + 1)) {
                // Caso possua, realiza as combinações entre os grupos e as adiciona ao conjunto
                termosCombinados.addAll(combinarTermosAdjacentes(grupos.get(chave), grupos.get(chave + 1)));
            }
        }
        // Verifica quais termos não participaram de nenhuma combinação e os adiciona à lista de implicantes primos
        verificarImplicantesPrimos(grupos);
        // Retorna o conjunto com todas as combinações realizadas nesta iteração
        return termosCombinados;
    }
    // Método que verifica quais implicantes são primos. Um implicante é primo caso não possa ser combinado com nenhum outro implicante.
    private void verificarImplicantesPrimos(Map<Integer, List<Termo>> grupos) {
        // Percorre cada grupo de um mapeamento
        for (Map.Entry<Integer, List<Termo>> entrada : grupos.entrySet()) {
            for (Termo termo : entrada.getValue()) {
                if (!termo.getCombinado())
                    // Quando encontra um termo que não foi combinado, adiciona aos implicantes primos
                    implicantesPrimos.add(termo);
            }
        }
    }
    // Método que realiza uma bateria de combinações até que todos os implicantes primos tenham sido determinados
    public Set<Termo> encontrarImplicantesPrimos() {
        // Este conjunto representa os termos que serão usados na iteração atual. Na primeira iteração, são simplesmente os implicantes dos mintermos
        Set<Termo> termosAtuais = new HashSet<>(gerarTermosIniciais());
        // Realiza combinações até que nenhuma combinação possa ser feita
        while (!termosAtuais.isEmpty()) {
            // Agrupa os termos pelo número de bits de valor 1
            Map<Integer, List<Termo>> grupos = agruparPorNumeroDeBits1(termosAtuais);
            // Realiza as combinações, e no processo, adiciona os implicantes primos à lista de implicantes primos
            Set<Termo> novosTermos = realizarCombinacoes(grupos);
            // Atualiza os termos atuais para a próxima iteração
            termosAtuais = novosTermos;
        }
        // Após todas as iterações de combinações, todos os implicantes primos foram determinados
        return implicantesPrimos;
    }
    public Map<Integer, Set<Termo>> mapeamentoDeCobertura() {
        Map<Integer, Set<Termo>> cobertura = new HashMap<>();
        for (Integer mintermo : funcao.getMintermos()) {
            cobertura.put(mintermo, new HashSet<>());
        }
        for (Termo implicante : implicantesPrimos) {
            for (Integer mintermoContemplado : implicante.getMintermosContemplados()) {
                cobertura.get(mintermoContemplado).add(implicante);
            }
        }
        return cobertura;
    }
    public Set<Termo> determinarImplicantesEssenciais() {
        for (Set<Termo> cobertura : mapeamentoDeCobertura().values()) {
            if (cobertura.size() == 1) 
                implicantesPrimosEssenciais.add(cobertura.iterator().next());
        }
        return implicantesPrimosEssenciais;
    }
}
