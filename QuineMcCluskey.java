import java.util.*;

public class QuineMcCluskey {
    private FuncaoLogica funcao;
    private Set<Termo> implicantesPrimos;
    private Set<Termo> implicantesPrimosEssenciais;
    public QuineMcCluskey(FuncaoLogica funcao) {
        this.funcao = funcao;
        implicantesPrimos = new HashSet<>();
        implicantesPrimosEssenciais = new HashSet<>();
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
    // Cria a "tabela" de cobertura do algoritmo
    public Map<Integer, Set<Termo>> mapeamentoDeCobertura() {
        // Cria um mapeamento cujas chaves são os mintermos da função, e o valor é um conjunto com os implicantes primos que cobrem esse mintermo
        Map<Integer, Set<Termo>> cobertura = new HashMap<>();
        // Pega os mintermos da função e cria os mapeamentos
        for (Integer mintermo : funcao.getMintermos()) {
            cobertura.put(mintermo, new HashSet<>());
        }
        // Percorre todos mintermos contemplados por cada implicante primo, e adiciona o implicante no conjunto de termos que cobrem aquele mintermo
        for (Termo implicante : implicantesPrimos) {
            for (Integer mintermoContemplado : implicante.getMintermosContemplados()) {
                cobertura.get(mintermoContemplado).add(implicante);
            }
        }
        // Retorna essa mapeamento
        return cobertura;
    }
    // Determina os implicantes essenciais por meio da "tabela" de cobertura. Os implicantes essenciais são aqueles que cobrem mintermos que não são cobertos por nenhum outro
    public Set<Termo> determinarImplicantesEssenciais() {
        // Os mintermos que são cobertos por apenas um termo indicam que aquele termo é essencial
        for (Set<Termo> cobertura : mapeamentoDeCobertura().values()) {
            if (cobertura.size() == 1) 
                // Adiciona o termo na lista de implicantes essenciais
                implicantesPrimosEssenciais.add(cobertura.iterator().next());
        }
        return implicantesPrimosEssenciais;
    }
    // Método que determina quais mintermos são cobertos por implicantes essenciais
    public Set<Integer> mintermosCobertosPorEssenciais() {
        Set<Integer> mintermosCobertosPorEssenciais = new HashSet<>();
        for (Termo termo : implicantesPrimosEssenciais) {
            mintermosCobertosPorEssenciais.addAll(termo.getMintermosContemplados());
        }
        return mintermosCobertosPorEssenciais;
    }
    // Método booleano que indica se os implicantes essenciais são suficientes para cobrir todos os mintermos da função
    public boolean essenciaisCobremTodosMintermos() {
        return mintermosCobertosPorEssenciais().containsAll(funcao.getMintermos());
    }
    // Método que determina quais mintermos não são cobertos por implicantes essenciais
    public Set<Integer> mintermosNaoCobertosPorEssenciais() {
        Set<Integer> mintermosNaoCobertosPorEssenciais = new HashSet<>(funcao.getMintermos());
        mintermosNaoCobertosPorEssenciais.removeAll(mintermosCobertosPorEssenciais());
        return mintermosNaoCobertosPorEssenciais;
    }
    // Método que determina quais implicantes primos não são essenciais
    public Set<Termo> implicantesNaoEssenciais() {
        Set<Termo> implicantesNaoEssenciais = new HashSet<>(implicantesPrimos);
        implicantesNaoEssenciais.removeAll(implicantesPrimosEssenciais);
        return implicantesNaoEssenciais;
    }
    // Monta a "tabela" de cobertura restante, isto é, com aqueles mintermos que não são cobertos por implicantes essenciais e os implicantes não-essenciais que os cobrem
    public Map<Integer, Set<Termo>> coberturaRestante() {
        // Cria um mapeamento cujas chaves são os mintermos restante da função, e o valor é um conjunto com os implicantes não essenciais que cobrem esse mintermo
        Map<Integer, Set<Termo>> coberturaRestante = new HashMap<>();
        Set<Integer> mintermosNaoCobertosPorEssenciais =  mintermosNaoCobertosPorEssenciais();
        // Pega os mintermos restante e cria os mapeamentos
        for (Integer mintermo : mintermosNaoCobertosPorEssenciais) {
            coberturaRestante.put(mintermo, new HashSet<>());
        }
        // Percorre todos mintermos não contemplados por implicantes essenciais, e adiciona o implicante não essencial no conjunto de termos que cobrem aquele mintermo
        for (Termo implicante : implicantesNaoEssenciais()) {
            for (Integer mintermoContemplado : implicante.getMintermosContemplados()) {
                if (mintermosNaoCobertosPorEssenciais.contains(mintermoContemplado)) {
                    coberturaRestante.get(mintermoContemplado).add(implicante);
                }
            }
        }
        // Retorna essa mapeamento
        return coberturaRestante;
    }
    /* Nesta etapa do algoritmo, temos todos os implicantes primos da função, assim como os implicantes essenciais. Além disso, já sabemos quais mintermos não são cobertos
    por implicantes essenciais. O problema agora é determinar qual o menor número de implicantes não-essenciais devemos incluir na expressão minimizada de função. 
    Para isso, fazemos uma outra tabela, contendo apenas os mintermos que não foram cobertos e os implicantes que os cobrem. Daí, precisamos criar uma expressão com todas
    as possibilidades que cobrem todos os mintermos restantes. Essa expressão é composta, à princípio, pelo produto da soma dos implicantes de cada mintermo restante. 
    Após isso, multiplicamos estes fatores (somas de implicantes) e minimizamos a expressão resultante. Ao final deste processo, escolheremos o produto com menor quantidade
    de termos */

    /* Cria os fatores (somas de implicantes) de cada mintermos restante e os guarda em um conjunto, cada elemento deste conjunto representa um fator. 
    Exemplo: {[A, B],[C, D]} representa (A + B)*(C + D) */
    public List<Set<Termo>> criarFatoresDePetrick() {
        List<Set<Termo>> expressaoDePetrick = new ArrayList<>();
        for (Set<Termo> somaDeTermos : coberturaRestante().values()) {
            expressaoDePetrick.add(somaDeTermos);
        }
        return expressaoDePetrick;
    }
    // Método que reescreve um fator como SOP
    public Set<Set<Termo>> converterPOSparaSOP(Set<Termo> fator) {
        Set<Set<Termo>> resultado = new HashSet<>();
        for (Termo termo : fator) {
            Set<Termo> produto = new HashSet<>();
            produto.add(termo);
            resultado.add(produto);
        }
        return resultado;
    }
    public Set<Set<Termo>> multiplicarFatores(Set<Set<Termo>> fator1, Set<Set<Termo>> fator2) {
        Set<Set<Termo>> resultado = new HashSet<>();
        for (Set<Termo> produto1 : fator1) {
            for (Set<Termo> produto2 : fator2) {
                Set<Termo> novoProduto = new HashSet<>();
                novoProduto.addAll(produto1);
                novoProduto.addAll(produto2);
                resultado.add(novoProduto);
            }
        }
        return resultado;
    }
    private Set<Set<Termo>> absorver(Set<Set<Termo>> expressao) {
        Set<Set<Termo>> resultado = new HashSet<>(expressao);
        for (Set<Termo> produtoA : expressao) {
            for (Set<Termo> produtoB : expressao) {
                if (produtoA == produtoB)
                    continue;
                if (produtoB.containsAll(produtoA)) {
                    resultado.remove(produtoB);
                }
            }
        }

        return resultado;
    }
    public Set<Set<Termo>> calcularExpressaoDePetrick() {
        List<Set<Termo>> fatores = criarFatoresDePetrick();
        Set<Set<Termo>> resultado = converterPOSparaSOP(fatores.get(0));
        for (int i = 1; i < fatores.size(); i++) {
            resultado = multiplicarFatores(resultado, converterPOSparaSOP(fatores.get(i)));
            resultado = absorver(resultado);
        }
        return resultado;
    }
}
