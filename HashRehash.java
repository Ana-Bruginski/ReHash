import java.util.Random;

public class HashRehash {

    int capacidade;
    int[] tabela;
    int limiteOcupacao;
    int ocupadas;
    int totalColisoes;
    int recusados;

    int[] elementos;
    int[] colisoes;
    int inseridos;

    int indiceTop1;
    int indiceTop2;
    int indiceTop3;

    // Construtor
    HashRehash(int capacidade, int porcentagemMaxima) {
        this.capacidade = capacidade;
        this.tabela = new int[capacidade];
        this.limiteOcupacao = (capacidade * porcentagemMaxima) / 100;
        this.ocupadas = 0;
        this.totalColisoes = 0;
        this.recusados = 0;

        this.elementos = new int[capacidade];
        this.colisoes = new int[capacidade];
        this.inseridos = 0;

        for (int i = 0; i < capacidade; i++) {
            tabela[i] = -1;
        }
    }

    // Funcao hash
    int hash(int chave) {
        return chave % capacidade;
    }

    // Funcao de rehash
    int rehash(int posicao) {
        return (posicao + 1) % capacidade;
    }

    void inserir(int chave) {
        if (ocupadas >= limiteOcupacao) {
            recusados = recusados + 1;
        } else {
            int posicao = hash(chave);
            int colisoesDaChave = 0;

            while (tabela[posicao] != -1) {
                colisoesDaChave = colisoesDaChave + 1;
                totalColisoes = totalColisoes + 1;
                posicao = rehash(posicao);
            }

            tabela[posicao] = chave;
            ocupadas = ocupadas + 1;

            elementos[inseridos] = chave;
            colisoes[inseridos] = colisoesDaChave;
            inseridos = inseridos + 1;
        }
    }

    int buscar(int chave) {
        int posicao = hash(chave);
        int passos = 0;

        while (tabela[posicao] != -1 && passos < capacidade) {
            if (tabela[posicao] == chave) {
                return posicao;
            }
            posicao = rehash(posicao);
            passos = passos + 1;
        }
        return -1;
    }

    void calcularTresQueMaisColidiram() {
        indiceTop1 = -1;
        for (int i = 0; i < inseridos; i++) {
            if (indiceTop1 == -1 || colisoes[i] > colisoes[indiceTop1]) {
                indiceTop1 = i;
            }
        }

        indiceTop2 = -1;
        for (int i = 0; i < inseridos; i++) {
            if (i != indiceTop1) {
                if (indiceTop2 == -1 || colisoes[i] > colisoes[indiceTop2]) {
                    indiceTop2 = i;
                }
            }
        }

        indiceTop3 = -1;
        for (int i = 0; i < inseridos; i++) {
            if (i != indiceTop1 && i != indiceTop2) {
                if (indiceTop3 == -1 || colisoes[i] > colisoes[indiceTop3]) {
                    indiceTop3 = i;
                }
            }
        }
    }

    void imprimirUmDosTres(int colocacao, int indice) {
        if (indice != -1) {
            System.out.println(colocacao + ") elemento " + elementos[indice]
                    + " | posicao inicial " + hash(elementos[indice])
                    + " | colisoes ate achar lugar: " + colisoes[indice]);
        }
    }

    void buscarUmDosTres(int indice) {
        if (indice != -1) {
            int chave = elementos[indice];
            int posicao = buscar(chave);

            if (posicao == -1) {
                System.out.println("elemento " + chave + " nao encontrado.");
            } else {
                System.out.println("elemento " + chave + " encontrado na posicao " + posicao
                        + " (posicao inicial era " + hash(chave) + ")");
            }
        }
    }

    void imprimirPosicoesLivres() {
        int livres = 0;

        for (int i = 0; i < capacidade; i++) {
            if (tabela[i] == -1) {
                System.out.print(i + " ");
                livres = livres + 1;
            }
        }
        System.out.println();
        System.out.println("total de posicoes livres: " + livres);
    }

    void imprimirResultados() {
        System.out.println("===== HASH COM REHASH =====");
        System.out.println("tamanho da tabela: " + capacidade);
        System.out.println("funcao hash: chave % " + capacidade);
        System.out.println("funcao de rehash: (posicao + 1) % " + capacidade);
        System.out.println("limite de ocupacao: " + limiteOcupacao + " elementos");
        System.out.println();

        System.out.println("--- resultado da carga ---");
        System.out.println("elementos inseridos: " + inseridos);
        System.out.println("insercoes recusadas pelo limite: " + recusados);
        System.out.println("total de colisoes na insercao: " + totalColisoes);
        System.out.println();

        System.out.println("--- tres elementos que mais colidiram ---");
        imprimirUmDosTres(1, indiceTop1);
        imprimirUmDosTres(2, indiceTop2);
        imprimirUmDosTres(3, indiceTop3);
        System.out.println();

        System.out.println("--- posicoes livres na matriz ---");
        imprimirPosicoesLivres();
        System.out.println();

        System.out.println("--- teste de busca com os tres que mais colidiram ---");
        buscarUmDosTres(indiceTop1);
        buscarUmDosTres(indiceTop2);
        buscarUmDosTres(indiceTop3);
    }

    public static void main(String[] args) {
        HashRehash tabela = new HashRehash(100, 90);
        Random sorteio = new Random(1090);

        for (int i = 0; i < 100; i++) {
            int chave = sorteio.nextInt(100001);
            tabela.inserir(chave);
        }

        tabela.calcularTresQueMaisColidiram();
        tabela.imprimirResultados();
    }
}
