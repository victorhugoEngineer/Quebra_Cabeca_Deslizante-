package Principal;

import model.QuebracabecaDeslizante;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("   APLICATIVO 1: QUEBRA-CABEÇA DESLIZANTE (8-PUZZLE)");
        System.out.println("==================================================");
        System.out.println("Escolha a opção de teste:");
        System.out.println("1. Usar tabuleiro padrão de teste");
        System.out.println("2. Digitar matriz 3x3 customizada (use 0 para o espaço vazio)");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();

        int[][] tabuleiroInicial = new int[3][3];

        if (opcao == 2) {
            System.out.println("\nDigite os 9 números do tabuleiro (3 por linha, de 0 a 8):");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    tabuleiroInicial[i][j] = scanner.nextInt();
                }
            }
        } else {
            // Tabuleiro padrão jogável e solucionável
            tabuleiroInicial = new int[][]{
                    {1, 2, 3},
                    {4, 0, 6},
                    {7, 5, 8}
            };
            System.out.println("\nCarregando estado inicial padrão...");
        }

        System.out.println("\nIniciando busca em largura (BFS)...\n");

        // Executa o algoritmo de resolução
        QuebracabecaDeslizante.resolver(tabuleiroInicial);

        scanner.close();
    }
}