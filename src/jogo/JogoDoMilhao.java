package jogo;

import java.util.Scanner;
import java.util.Random;

public class JogoDoMilhao {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcaoMenu = 0;

        // F8: pelo menos 2 vetores relacionados (declarados no main para respeitar a regra F9 sem globais)
        // F9: sem variáveis globais; os dados circulam por parâmetros e retornos
        String[] codigosRanking = new String[50];
        int[] pontuacoesRanking = new int[50];
        int qtdJogadores = 0; // controla quantas pessoas já jogaram

        // F1: o main contém apenas o menu e as chamadas de funções
        do{
            // J1: menu inicial que se repete até "Sair"
            opcaoMenu = exibirMenuPrincipal(sc);

            switch (opcaoMenu){
                case 1:
                    int pontosFinais = jogar(sc);
                    qtdJogadores = salvarPartida(codigosRanking, pontuacoesRanking, pontosFinais, qtdJogadores, sc);  // J6: guarda código e pontuação da partida

                    // F7: ordena o ranking (a alteração aparece aqui porque vetores são passados por referência)
                    ordenarRanking(codigosRanking, pontuacoesRanking, qtdJogadores);
                    // J5: classificação em faixas + dica de acordo com o desempenho
                    exibirClassificacao(pontosFinais);
                    break;
                case 2:
                    mostrarComoJogar();
                    break;
                case 3:
                    // F7: exibe o ranking sempre ordenado
                    ordenarRanking(codigosRanking, pontuacoesRanking, qtdJogadores);
                    exibirRanking(codigosRanking, pontuacoesRanking, qtdJogadores);
                    break;
                case 4:
                    mostrarDespedida();
                    break;
            }
        }while(opcaoMenu != 4);

        sc.close();
    }

    /*
      O que faz: Exibe o menu inicial com as opções do jogo e chama a leitura validada.
      Parâmetros: Scanner sc
      Retorno: int (a opção validada escolhida pelo jogador)
      Autor: Eduarda Guimarães
     */
    // J1, F3 (retorno), F5 (reutiliza a leitura validada)
    public static int exibirMenuPrincipal(Scanner sc) {
        System.out.println("-- Jogo do Milhão \uD83C\uDF3D --");
        System.out.println("Você sabe como evitar o desperdício de alimentos?");
        System.out.println("1 - Jogar");
        System.out.println("2 - Como Jogar");
        System.out.println("3 - Ranking");
        System.out.println("4 - Sair");
        System.out.println("Escolha uma opção: ");
        return lerEntradaValida(sc, 1, 4);
    }

    /*
      O que faz: Lê a entrada do usuário garantindo que seja um número inteiro dentro dos limites informados.
      Parâmetros: Scanner sc, int min, int max
      Retorno: int (o número válido digitado)
      Autor: Eduarda Guimarães
     */
    // F5: única função de leitura com validação, usada no menu, nas respostas e no código do jogador
    public static int lerEntradaValida(Scanner sc, int min, int max) {
        int opcao = 0;
        boolean entradaValida = false;

        while (!entradaValida) {
            String entrada = sc.nextLine();

            try{
                opcao = Integer.parseInt(entrada);
                if (opcao >= min && opcao <= max) {
                    entradaValida = true;
                } else {
                    System.out.println("Número inválido! Digite um número inteiro entre " + min + " e " + max + ":");
                }
            }catch(NumberFormatException e){
                System.out.println("Erro: letras, decimais ou caracteres especiais não são aceitos!\nDigite apenas um número entre " + min + " e " + max + ":");
            }
        }
        return opcao;
    }

    /*
      O que faz: Controla o loop das 8 situações do jogo, chamando as perguntas, as explicações e calculando os pontos.
      Parâmetros: Scanner sc
      Retorno: int (a pontuação final da partida)
      Autor: Bruno Kaefer e Gabriel Presotto
     */
    // J2: situações guardadas em vetores (texto, opções, resposta certa e explicação)
    public static int jogar(Scanner sc){
        String[] perguntas = {
                "Ao jogar comida no lixo, o que mais se perde junto com ela?",
                "Fazer lista de compras e olhar o que já tem em casa é perda de tempo?",
                "Qual gás liberado por frutas maduras (como banana) acelera o amadurecimento das outras?",
                "O que significa arrumar a despensa pelo método PVPS?",
                "Uma fruta muito madura deve ir direto para o lixo?",
                "Depois de pronta, a comida deve ser guardada na geladeira em no máximo quanto tempo?",
                "Na lista de ingredientes de um rótulo, a ordem dos itens significa o quê?",
                "Quando a cadeia do frio (refrigeração) do alimento é quebrada, o que acontece?"
        };

        String[][] opcoes = {
                {"1 - Só o dinheiro", "2 - Água, solo e energia usados na produção", "3 - Apenas espaço no lixo"},
                {"1 - Mito, planejar evita compras repetidas e desperdício", "2 - Verdade, no mercado eu lembro"},
                {"1 - Oxigênio", "2 - Gás Carbônico", "3 - Gás Etileno"},
                {"1 - Primeiro que Vence, Primeiro que Sai", "2 - Pagar a Vista Para Sobrar", "3 - Produtos Verdes Para Saladas"},
                {"1 - Não, pode ser congelada ou virar doce/bolo", "2 - Sim, já estragou"},
                {"1 - 6 horas", "2 - 12 horas", "3 - 2 horas"},
                {"1 - Do maior para o menor em quantidade", "2 - Ordem alfabética", "3 - Do mais barato para o mais caro"},
                {"1 - Nada, se colocar na geladeira depois", "2 - A validade deixa de valer e pode estragar antes"}
        };

        int[] respostasCertas = {2, 1, 3, 1, 1, 3, 1, 2};

        // J4: explicação de cada situação, cada uma com sua fonte
        String[] explicacoes = {
                "O desperdício tem um impacto invisível: gasta os recursos naturais do planeta (Fonte: FAO).",
                "Mito! Planejar o cardápio evita comprar por impulso o que você já tem em casa (Fonte: Guia Alimentar para a População Brasileira, Ministério da Saúde).",
                "É o gás etileno! Separe as bananas maduras das outras frutas e hortaliças para elas durarem mais (Fonte: Embrapa).",
                "O que vence primeiro deve ficar na frente da despensa para ser consumido logo (Fonte: SEBRAE).",
                "Fruta madura não é lixo! Use o aproveitamento integral (Fonte: CEASA).",
                "Deixar a comida mais de 2h fora da geladeira aumenta o risco de bactérias (Fonte: ANVISA).",
                "O primeiro ingrediente listado é o que tem em maior quantidade no produto (Fonte: ANVISA).",
                "Quebrar a cadeia do frio compromete a segurança da comida (Fonte: ANVISA)."
        };

        // vetor de índices para embaralhar as perguntas sem alterar os vetores originais
        int[] indices = criarIndicesSituacoes(perguntas.length);
        embaralharSituacoes(indices);

        int pontuacao = 0;

        System.out.println("\n=== Iniciando o Jogo ===");

        for (int i = 0; i < indices.length; i++){
            int atual = indices[i];
            mostrarSituacao(perguntas[atual], opcoes[atual], i);

            System.out.print("Sua resposta: ");
            int resposta = lerEntradaValida(sc, 1, opcoes[atual].length);  // F5: mesma leitura validada do menu

            boolean acertou = verificarResposta(resposta, respostasCertas[atual]);
            mostrarExplicacao(acertou, explicacoes[atual]);  // J4: explica por que a escolha é boa ou ruim
            pontuacao = atualizarPontosDesperdicio(pontuacao, acertou);  // J3: recebe os pontos atuais e devolve os novos
        }

        System.out.println("\n🎉 Fim do Jogo! Sua pontuação final foi: " + pontuacao);
        return pontuacao;
    }

    /*
      O que faz: Cria o vetor de índices (0, 1, 2...) que representa a ordem das situações do jogo.
      Parâmetros: int qtdSituacoes
      Retorno: int[] (vetor com os índices em ordem crescente)
      Autor: Bruno Kaefer e Gabriel Presotto
     */
    public static int[] criarIndicesSituacoes(int qtdSituacoes) {
        int[] indices = new int[qtdSituacoes];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        return indices;
    }

    /*
      O que faz: Embaralha a ordem das situações trocando as posições do vetor de índices (Fisher-Yates).
      Parâmetros: int[] indices (vetor alterado diretamente, sem retorno)
      Retorno: void
      Autor: Eduarda Guimarães
     */
    // F3 (void), F4 (recebe vetor como parâmetro)
    public static void embaralharSituacoes(int[] indices) {
        Random rand = new Random();
        for (int i = indices.length - 1; i > 0; i--) {
            int sorteado = rand.nextInt(i + 1);
            int temp = indices[i];
            indices[i] = indices[sorteado];
            indices[sorteado] = temp;
        }
    }

    /*
      O que faz: Mostra na tela as instruções de como jogar.
      Parâmetros: nenhum
      Retorno: void
      Autor: Bruno Kaefer
     */
    // F3 (void)
    public static void mostrarComoJogar(){
        System.out.println("\nCOMO JOGAR: ");
        System.out.println("O jogo consiste de 8 desafios (perguntas) diversos ");
        System.out.println("sobre casos recorrentes durante a semana de consumo de alimentos.");
        System.out.println("Desafios como:");
        System.out.println(" • Múltipla escolha — uma alternativa está certa");
        System.out.println(" • Verdade ou mito — julgue a afirmação");
        System.out.println("Cada acerto soma pontos!");
        System.out.println("No final será gerado um ranking anônimo dos usuários.");
        System.out.println("Cada pergunta terá uma explicação confiável!");
        System.out.println("O jogo tem como intuito conscientizar as pessoas sobre a causa!");
        System.out.println("--------------------------------------------------\n");
    }

    /*
      O que faz: Mostra na tela a pergunta da rodada e as alternativas de resposta.
      Parâmetros: String pergunta, String[] opcoes, int rodada
      Retorno: void
      Autor: Bruno Kaefer
     */
    // F3 (void), F4 (recebe vetor como parâmetro)
    public static void mostrarSituacao(String pergunta, String[] opcoes, int rodada) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("Pergunta " + (rodada + 1) + ": " + pergunta);

        for (int i = 0; i < opcoes.length; i++) {
            System.out.println(opcoes[i]);
        }
        System.out.println("--------------------------------------------------\n");
    }

    /*
      O que faz: Compara a escolha do jogador com a alternativa correta da situação.
      Parâmetros: int escolha, int correta
      Retorno: boolean (true se o jogador acertou)
      Autor: Gabriel Presotto
     */
    // F3 (retorno boolean)
    public static boolean verificarResposta(int escolha, int correta) {
        return escolha == correta;
    }

    /*
      O que faz: Mostra a mensagem curta que explica por que a escolha foi boa ou ruim.
      Parâmetros: boolean acertou, String explicacao
      Retorno: void
      Autor: Gabriel Presotto
     */
    // J4, F3 (void)
    public static void mostrarExplicacao(boolean acertou, String explicacao) {
        if (acertou) {
            System.out.println("Boa! " + explicacao);
        } else {
            System.out.println("Atenção! " + explicacao);
        }
    }

    /*
      O que faz: Atualiza os pontos do jogador: soma os pontos de um acerto ou mantém o valor em caso de erro.
      Parâmetros: int pontosAtuais, boolean acertou
      Retorno: int (os pontos atualizados)
      Autor: Gabriel Presotto
     */
    // J3: recebe os pontos atuais e devolve os novos; F3 (retorno int)
    public static int atualizarPontosDesperdicio(int pontosAtuais, boolean acertou){
        if (acertou){
            return pontosAtuais + 125;
        }
        return pontosAtuais;
    }

    /*
      O que faz: Define a faixa do jogador de acordo com a pontuação final.
      Parâmetros: int pontuacaoFinal
      Retorno: String (nome da faixa: Exemplar, Consciente ou Iniciante)
      Autor: Gabriel Presotto
     */
    // J5, F3 (retorno String)
    public static String classificarConsumidor(int pontuacaoFinal){
        if (pontuacaoFinal >= 800) return "Exemplar";
        if (pontuacaoFinal >= 500) return "Consciente";
        return "Iniciante";
    }

    /*
      O que faz: Escolhe a dica final de acordo com a faixa do jogador.
      Parâmetros: String faixa
      Retorno: String (a dica correspondente à faixa)
      Autor: Gabriel Presotto
     */
    // J5, F3 (retorno String)
    public static String gerarDicaDesperdicio(String faixa){
        if (faixa.equals("Exemplar")) return "Você domina o aproveitamento integral!";
        if (faixa.equals("Consciente")) return "Bom caminho, revise a validade dos itens.";
        return "Planeje sua lista antes de ir ao mercado!";
    }

    /*
      O que faz: Mostra na tela a faixa do jogador e a dica de acordo com o desempenho.
      Parâmetros: int pontuacaoFinal
      Retorno: void
      Autor: Gabriel Presotto
     */
    // J5, F3 (void)
    public static void exibirClassificacao(int pontuacaoFinal) {
        String faixa = classificarConsumidor(pontuacaoFinal);
        System.out.println("\nClassificação - " + faixa + ": " + gerarDicaDesperdicio(faixa) + "\n");
    }

    /*
      O que faz: Transforma o número escolhido pelo jogador em um código anônimo (ex: 1 vira P01, 12 vira P12).
      Parâmetros: int numero
      Retorno: String (o código do jogador)
      Autor: Luiz Gabriel Lauck
     */
    // F3 (retorno String); código anônimo para respeitar a LGPD
    public static String formatarCodigoJogador(int numero) {
        if (numero < 10) {
            return "P0" + numero;
        }
        return "P" + numero;
    }

    /*
      O que faz: Procura um jogador no ranking pelo código.
      Parâmetros: String[] codigos, int qtdAtual, String codigoBuscado
      Retorno: int (a posição do jogador no vetor, ou -1 se o código não existir)
      Autor: Luiz Gabriel Lauck
     */
    // F6: busca por código; devolve a posição ou -1 se não existir; F3 (retorno), F4 (recebe vetor como parâmetro)
    public static int buscarJogador(String[] codigos, int qtdAtual, String codigoBuscado) {
        for (int i = 0; i < qtdAtual; i++) {
            if (codigos[i].equals(codigoBuscado)) {
                return i;
            }
        }
        return -1;
    }

    /*
      O que faz: Salva o código do jogador e a pontuação final no ranking; se o código já existir, mantém a maior pontuação.
      Parâmetros: String[] codigos, int[] pontuacoes, int pontosFinais, int qtdAtual, Scanner sc
      Retorno: int (a quantidade de jogadores após a tentativa de salvar)
      Autor: Luiz Gabriel Lauck
     */
    // J6: guarda código e pontuação; F4 (recebe vetores como parâmetro), F8 (vetores relacionados pela mesma posição)
    public static int salvarPartida(String[] codigos, int[] pontuacoes, int pontosFinais, int qtdAtual, Scanner sc) {
        System.out.println("Escolha o número do seu código de jogador (1 a 99, ex: 1 vira P01): ");
        int numero = lerEntradaValida(sc, 1, 99);
        String codigo = formatarCodigoJogador(numero);

        int posicao = buscarJogador(codigos, qtdAtual, codigo);
        if (posicao != -1) {
            if (pontosFinais > pontuacoes[posicao]) {
                pontuacoes[posicao] = pontosFinais;
                System.out.println("Novo recorde para " + codigo + "! Pontuação atualizada.");
            } else {
                System.out.println("O código " + codigo + " já tem pontuação maior ou igual no ranking.");
            }
            return qtdAtual;
        }

        if (qtdAtual >= codigos.length || qtdAtual >= pontuacoes.length) {
            System.out.println("O ranking está cheio. A partida não foi salva.");
            return qtdAtual;
        }

        codigos[qtdAtual] = codigo;
        pontuacoes[qtdAtual] = pontosFinais;
        System.out.println("Partida salva com sucesso! Seu código é " + codigo + ".");
        return qtdAtual + 1;
    }

    /*
      O que faz: Ordena o ranking da maior para a menor pontuação (ordenação por seleção).
      Parâmetros: String[] codigos, int[] pontuacoes, int qtdAtual
      Retorno: void
      Autor: Vitor Soares
     */
    // F7: ordenação sem funções prontas; a alteração aparece fora da função porque o vetor é passado por referência
    // F3 (void), F4 (recebe vetores como parâmetro), F8 (os dois vetores são trocados juntos)
    public static void ordenarRanking(String[] codigos, int[] pontuacoes, int qtdAtual) {
        for (int i = 0; i < qtdAtual - 1; i++) {
            int posicaoMaior = i;
            for (int j = i + 1; j < qtdAtual; j++) {
                if (pontuacoes[j] > pontuacoes[posicaoMaior]) {
                    posicaoMaior = j;
                }
            }

            int auxPontos = pontuacoes[i];
            pontuacoes[i] = pontuacoes[posicaoMaior];
            pontuacoes[posicaoMaior] = auxPontos;

            String auxCodigo = codigos[i];
            codigos[i] = codigos[posicaoMaior];
            codigos[posicaoMaior] = auxCodigo;
        }
    }

    /*
      O que faz: Calcula a média das pontuações guardadas no ranking.
      Parâmetros: int[] pontuacoes, int qtdAtual
      Retorno: double
      Autor: Vitor Soares
     */
    // J6 (média), F3 (retorno double), F4 (recebe vetor como parâmetro)
    public static double calcularMediaPontuacao(int[] pontuacoes, int qtdAtual) {
        if (qtdAtual == 0) {
            return 0;
        }
        double somaPontos = 0;
        for (int i = 0; i < qtdAtual; i++) {
            somaPontos += pontuacoes[i];
        }
        return somaPontos / qtdAtual;
    }

    /*
      O que faz: Mostra o ranking em ordem, com empates, a média, a maior e a menor pontuação.
      Parâmetros: String[] codigos, int[] pontuacoes, int qtdAtual
      Retorno: void
      Autor: Vitor Soares
     */
    // J6: ordem, maior, menor e média; F3 (void), F4 (recebe vetores como parâmetro)
    // Atenção: usa a posição 0 como maior e a última como menor, então o ranking precisa estar ordenado antes (o main ordena)
    public static void exibirRanking(String[] codigos, int[] pontuacoes, int qtdAtual) {
        System.out.println("\nRanking Final 🏆");
        if(qtdAtual == 0) {
            System.out.println("Ainda não há jogadores no ranking.\n");
            return;
        }

        int posicaoReal = 1;

        for(int i = 0; i < qtdAtual; i++) {
            if (i > 0 && pontuacoes[i] != pontuacoes[i - 1]) {
                posicaoReal = i + 1;
            }
            if (i > 0 && pontuacoes[i] == pontuacoes[i - 1]) {
                System.out.println(posicaoReal + "° Lugar (Empate): " + codigos[i] + " - " + pontuacoes[i] + " pontos");
            } else {
                System.out.println(posicaoReal + "° Lugar: " + codigos[i] + " - " + pontuacoes[i] + " pontos");
            }
        }
        System.out.printf("A pontuação média da sessão foi de: %.2f pontos%n", calcularMediaPontuacao(pontuacoes, qtdAtual));
        System.out.println("A maior pontuação foi de: " + codigos[0] + " com um total de " + pontuacoes[0] + " pontos");
        System.out.println("A menor pontuação foi de: " + codigos[qtdAtual-1] + " com um total de " + pontuacoes[qtdAtual-1] + " pontos\n");
    }

    /*
      O que faz: Mostra a mensagem de despedida ao sair do jogo.
      Parâmetros: nenhum
      Retorno: void
      Autor: Eduarda Guimarães
     */
    // F1 (tira o println do main), F3 (void)
    public static void mostrarDespedida() {
        System.out.println("Saindo do Jogo do Milhão. Obrigado por jogar e evitar o desperdício!");
    }
}