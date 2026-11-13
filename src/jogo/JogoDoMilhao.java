package jogo;

import java.util.Scanner;

public class JogoDoMilhao {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcaoMenu = 0;

        // F8: pelo menos 2 vetores relacionados (declarados no main para respeitar a regra F9 sem globais)
        String[] codigosRanking = new String[50];
        int[] pontuacoesRanking = new int[50];
        int qtdJogadores = 0; // controla quantas pessoas já jogaram

        // F1: o main contém apenas o menu e as chamadas de funções
        do{
            // J1: menu inicial que se repete até "Sair"
            opcaoMenu = exibirMenuPrincipal(sc);

            switch (opcaoMenu){
                case 1:
                    // a função jogar agora devolve os pontos, e guardamos na variável
                    int pontosFinais = jogar(sc);

                    // Luiz: criar a função salvarPartida e descomentar a linha abaixo
                    // qtdJogadores = salvarPartida(codigosRanking, pontuacoesRanking, pontosFinais, qtdJogadores, sc);
                    break;
                case 2:
                    mostrarComoJogar();
                    break;
                case 3:
                    // Vitor: criar as funções ordenarRanking e exibirRanking e descomentar as linhas abaixo
                    // ordenarRanking(codigosRanking, pontuacoesRanking, qtdJogadores);
                    // exibirRanking(codigosRanking, pontuacoesRanking, qtdJogadores);
                    break;
                case 4:
                    System.out.println("Saindo do Jogo do Milhão. Obrigado por jogar e evitar o desperdício!");
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
    // J1: imprime os textos das 4 opções iniciais obrigatórias
    // F3: função com retorno devolvendo um int para o main
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
      Parâmetros: Scanner sc, int min (valor mínimo aceito), int max (valor máximo aceito)
      Retorno: int (o número válido digitado)
      Autor: Eduarda Guimarães
     */
    // F5: única função de leitura com validação por parâmetros
    // F3: função com retorno devolvendo um int para exibirMenuPrincipal
    public static int lerEntradaValida(Scanner sc, int min, int max) {
        int opcao = 0;
        boolean entradaValida = false;

        while (!entradaValida) {
            if (sc.hasNextInt()) {
                opcao = sc.nextInt();
                if(opcao >= min && opcao <= max){
                    entradaValida = true;
                }
                else{
                    System.out.println("Número inválido! Digite um número entre "+min+" e "+max+":");
                }
            }
            else{
                System.out.println("Erro: letras ou caracteres especiais não são aceitos!\nDigite apenas um número entre "+min+" e "+max+":");
                sc.next();
            }
        }
        return opcao;
    }

    /*
      O que faz: Controla o loop das 8 situações do jogo, chamando as perguntas e calculando os pontos.
      Parâmetros: Scanner sc
      Retorno: void
      Autores: Equipe (Bruno e Gabriel)
     */
    // J2: 8 situações baseadas no diagnóstico guardadas em vetores
    public static int jogar(Scanner sc){
        // Vetor 1: as 8 perguntas do diagnóstico
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

        // Vetor 2 (Matriz): as opções de cada pergunta (cada linha é uma pergunta)
        String[][] opcoes = {
                {"1 - Só o dinheiro", "2 - Água, solo e energia usados na produção", "3 - Apenas espaço no lixo"},
                {"1 - Verdade, no mercado eu lembro", "2 - Mito, planejar evita compras repetidas e desperdício"},
                {"1 - Oxigênio", "2 - Gás Carbônico", "3 - Gás Etileno"},
                {"1 - Primeiro que Vence, Primeiro que Sai", "2 - Pagar a Vista Para Sobrar", "3 - Produtos Verdes Para Saladas"},
                {"1 - Sim, já estragou", "2 - Não, pode ser congelada ou virar doce/bolo"},
                {"1 - 6 horas", "2 - 12 horas", "3 - 2 horas"},
                {"1 - Ordem alfabética", "2 - Do maior para o menor em quantidade", "3 - Do mais barato para o mais caro"},
                {"1 - Nada, se colocar na geladeira depois", "2 - A validade deixa de valer e pode estragar antes"}
        };

        // Vetor 3: o gabarito (qual é o número da resposta certa de cada uma)
        int[] respostasCertas = {2, 2, 3, 1, 2, 3, 2, 2};

        // Vetor 4: a conscientização (regra J4)
        String[] explicacoes = {
                "O desperdício tem um impacto invisível: gasta os recursos naturais do planeta (Fonte: FAO).",
                "Mito! Planejar o cardápio evita comprar por impulso o que você já tem em casa.",
                "É o gás etileno! Separe as bananas maduras das outras hortaliças para elas durarem mais.",
                "O que vence primeiro deve ficar na frente na geladeira para ser consumido logo.",
                "Fruta madura não é lixo! Use o aproveitamento integral (Fonte: CEASA).",
                "Deixar a comida mais de 2h fora da geladeira aumenta o risco de bactérias.",
                "O primeiro ingrediente listado é o que tem em maior quantidade no produto.",
                "Quebrar a cadeia do frio compromete a segurança da comida."
        };

        // começa o jogo com 0 pontos
        int pontuacao = 0;

        System.out.println("\n=== INICIANDO A SEMANA SEM DESPERDÍCIO ===");

        for (int i = 0; i < perguntas.length; i++){
            mostrarSituacao(perguntas[i], opcoes[i], i);

            System.out.print("Sua resposta: ");
            int resposta = lerEntradaValida(sc, 1, opcoes[i].length);

            // Gabriel: criar a função atualizarPontuacao e descomentar a linha abaixo
            // pontuacao = atualizarPontuacao(pontuacao, resposta, respostasCertas[i], explicacoes[i]);
        }

        System.out.println("\n🎉 FIM DA SEMANA! Sua pontuação final foi: " + pontuacao);

        // retorna os pontos para o main, onde o Luiz vai pegar para salvar
        return pontuacao;
    }

    // =========================================================================
    // ÁREA DOS COLEGAS: ESTRUTURAS VAZIAS PARA ELES PREENCHEREM A LÓGICA
    // =========================================================================

    /*
      O que faz: Exibe as regras e instruções de como jogar o Jogo do Milhão.
      Parâmetros: nenhum
      Retorno: void
      Autor: Bruno Leonardo
     */
    // J1: imprime na tela a descrição de como jogar
    // F3: função com retorno void
    public static void mostrarComoJogar(){
        System.out.println("\nCOMO JOGAR: ");
        System.out.println("O jogo consiste de 8 desafios (perguntas) diversos ");
        System.out.println("sobre casos recorrentes durante a semana de consumo de alimentos.");
        System.out.println("Desafios como:");
        System.out.println(" • Múltipla escolha — uma alternativa está certa");
        System.out.println(" • Verdade ou mito — julgue a afirmação");
        System.out.println("Cada acerto soma 100 pontos!");
        System.out.println("No final será gerado um ranking anônimo dos usuários.");
        System.out.println("Cada pergunta terá uma explicação confiável!");
        System.out.println("O jogo tem como intuito conscientizar as pessoas sobre a causa!");
        System.out.println("--------------------------------------------------");
    }

    /*
      O que faz: Imprime a pergunta atual e faz um laço para exibir as alternativas formatadas.
      Parâmetros: String pergunta, String[] opcoes, int rodada
      Retorno: void
      Autor: Bruno Leonardo
     */
    // J2: exibe situações do diagnóstico
    // F4: recebe um vetor de Strings (opcoes) como parâmetro
    // F3: função com retorno void
    public static void mostrarSituacao(String pergunta, String[] opcoes, int rodada) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("Pergunta " + (rodada + 1) + ": " + pergunta);

        // Percorre o vetor de opções correspondente à rodada atual para exibir as alternativas
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println(opcoes[i]);
        }
        System.out.println("--------------------------------------------------");
    }

    /*
      O que faz: Ordena os vetores do maior para o menor sem usar funções prontas.
      Parâmetros: String[] codigos, int[] pontuacoes, int qtdAtual
      Retorno: void
      Autor: Vitor Soares
     */
    // F7
    // public static void ordenarRanking(String[] codigos, int[] pontuacoes, int qtdAtual) {
    //      (Vitor coloca a lógica do laço de ordenação (Bubble Sort) aqui)
    // }

    /*
      O que faz: Exibe o ranking, e calcula a maior, menor e média.
      Parâmetros: String[] codigos, int[] pontuacoes, int qtdAtual
      Retorno: void
      Autor: Vitor Soares
     */
    // J6
    // public static void exibirRanking(String[] codigos, int[] pontuacoes, int qtdAtual) {
    //      (Vitor coloca o for que imprime o ranking e calcula a média aqui)
    // }
}
