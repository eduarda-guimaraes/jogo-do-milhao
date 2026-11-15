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

                    qtdJogadores = salvarPartida(codigosRanking, pontuacoesRanking, pontosFinais, qtdJogadores, sc);

                    // Exemplo temporário para testar a classificação ao final da partida:
                    System.out.println(classificarJogador(pontosFinais));
                    break;
                case 2:
                    mostrarComoJogar();
                    break;
                case 3:
                    // Exibe o ranking de forma ordenada
                    ordenarRanking(codigosRanking, pontuacoesRanking, qtdJogadores);
                    exibirRanking(codigosRanking, pontuacoesRanking, qtdJogadores);
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
      Retorno: int
      Autor: Eduarda Guimarães
     */
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
      Retorno: int
      Autores: Equipe (Bruno e Gabriel)
     */
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
                {"1 - Verdade, no mercado eu lembro", "2 - Mito, planejar evita compras repetidas e desperdício"},
                {"1 - Oxigênio", "2 - Gás Carbônico", "3 - Gás Etileno"},
                {"1 - Primeiro que Vence, Primeiro que Sai", "2 - Pagar a Vista Para Sobrar", "3 - Produtos Verdes Para Saladas"},
                {"1 - Sim, já estragou", "2 - Não, pode ser congelada ou virar doce/bolo"},
                {"1 - 6 horas", "2 - 12 horas", "3 - 2 horas"},
                {"1 - Ordem alfabética", "2 - Do maior para o menor em quantidade", "3 - Do mais barato para o mais caro"},
                {"1 - Nada, se colocar na geladeira depois", "2 - A validade deixa de valer e pode estragar antes"}
        };

        int[] respostasCertas = {2, 2, 3, 1, 2, 3, 2, 2};

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

        int pontuacao = 0;

        System.out.println("\n=== INICIANDO A SEMANA SEM DESPERDÍCIO ===");

        for (int i = 0; i < perguntas.length; i++){
            mostrarSituacao(perguntas[i], opcoes[i], i);

            System.out.print("Sua resposta: ");
            int resposta = lerEntradaValida(sc, 1, opcoes[i].length);

            pontuacao = atualizarPontuacao(pontuacao, resposta, respostasCertas[i], explicacoes, i);
        }

        System.out.println("\n🎉 FIM DA SEMANA! Sua pontuação final foi: " + pontuacao);
        return pontuacao;
    }

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
        System.out.println("--------------------------------------------------");
    }

    public static void mostrarSituacao(String pergunta, String[] opcoes, int rodada) {
        System.out.println("\n--------------------------------------------------");
        System.out.println("Pergunta " + (rodada + 1) + ": " + pergunta);

        for (int i = 0; i < opcoes.length; i++) {
            System.out.println(opcoes[i]);
        }
        System.out.println("--------------------------------------------------");
    }

    /*
      O que faz: Salva o código do jogador e a pontuação final no ranking.
      Retorno: int (a quantidade de jogadores após a tentativa de salvar)
      Autor: Luiz
     */
    public static int salvarPartida(String[] codigos, int[] pontuacoes, int pontosFinais, int qtdAtual, Scanner sc) {
        if (qtdAtual >= codigos.length || qtdAtual >= pontuacoes.length) {
            System.out.println("O ranking está cheio. A partida não foi salva.");
            return qtdAtual;
        }

        System.out.print("Digite seu código de jogador (ex: P01): ");
        String codigo = sc.next();

        codigos[qtdAtual] = codigo;
        pontuacoes[qtdAtual] = pontosFinais;
        return qtdAtual + 1;
    }

    public static void ordenarRanking(String[] codigos, int[] pontuacoes, int qtdAtual) {
        int auxInt = 0;
        String auxString = "";
        for(int j = qtdAtual; j > 1; j--) {
            for(int i = 0; i < (j-1); i++) {
                if(pontuacoes[i] < pontuacoes[i+1]) {
                    auxInt = pontuacoes[i];
                    pontuacoes[i] = pontuacoes[i+1];
                    pontuacoes[i+1] = auxInt;
                    auxString = codigos[i];
                    codigos[i] = codigos[i+1];
                    codigos[i+1] = auxString;
                }
            }
        }
    }

    public static void exibirRanking(String[] codigos, int[] pontuacoes, int qtdAtual) {
        System.out.println("Ranking Final 🏆");
        if(qtdAtual == 0) {
            System.out.println("Ainda não há jogadores no ranking.");
            return;
        }

        float soma = 0;
        for(int i = 0; i < qtdAtual; i++) {
            soma += pontuacoes[i];
            System.out.println((i+1) + "° Lugar: " + codigos[i] + " - " + pontuacoes[i] + " pontos");
        }
        System.out.printf("A pontuação média da sessão foi de: %.2f pontos%n", (soma/qtdAtual));
        System.out.println("A maior pontuação foi de: " + codigos[0] + " com um total de " + pontuacoes[0] + " pontos");
        System.out.println("A menor pontuação foi de: " + codigos[qtdAtual-1] + " com um total de " + pontuacoes[qtdAtual-1] + " pontos");
    }

    public static int atualizarPontuacao(int pontosAtuais, int escolha, int correta, String[] dicas, int rodada){
        if (escolha == correta){
            System.out.println("Boa! " + dicas[rodada]);
            return pontosAtuais + 125;
        }

        System.out.println("Atenção! " + dicas[rodada]);
        return pontosAtuais;
    }

    public static String classificarJogador(int pontuacaoFinal){
        if (pontuacaoFinal >= 800) return "Classificação - Exemplar: Você domina o aproveitamento integral!";
        if (pontuacaoFinal >= 500) return "Classificação - Consciente: Bom caminho, revise a validade dos itens.";
        return "Classificação - Iniciante: Planeje sua lista antes de ir ao mercado!";
    }
}
