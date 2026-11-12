package jogo;

import java.util.Scanner;

public class JogoDoMilhao {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcaoMenu = 0;

        // F1: o main contém apenas o menu e as chamadas de funções
        do{
            // J1: menu inicial que se repete até "Sair"
            opcaoMenu = exibirMenuPrincipal(sc);

            switch (opcaoMenu){
                case 1:
                    System.out.println("Iniciando jogo...");
                    break;
                case 2:
                    System.out.println("Como Jogar...");
                    break;
                case 3:
                    System.out.println("Ranking...");
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
}
