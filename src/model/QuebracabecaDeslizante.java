package model;

import java.util.*;

public class QuebracabecaDeslizante {
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};

    // Classe interna que representa um estado do tabuleiro
    static class EstadoPuzzle {
        int[][] tabuleiro;
        int xVazio, yVazio;
        int passos;
        EstadoPuzzle pai;

        EstadoPuzzle(int[][] tabuleiro, int xVazio, int yVazio, int passos, EstadoPuzzle pai) {
            this.tabuleiro = tabuleiro;
            this.xVazio = xVazio;
            this.yVazio = yVazio;
            this.passos = passos;
            this.pai = pai;
        }

        // Gera uma chave única representando o estado (para controle de visitados)
        String getChave() {
            StringBuilder sb = new StringBuilder();
            for (int[] linha : tabuleiro) {
                for (int val : linha) {
                    sb.append(val);
                }
            }
            return sb.toString();
        }
    }

    public static void resolver(int[][] estadoInicial) {
        String estadoObjetivo = "123456780";

        // Encontrar a posição inicial do zero
        int xZero = -1, yZero = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (estadoInicial[i][j] == 0) {
                    xZero = i;
                    yZero = j;
                }
            }
        }

        Queue<EstadoPuzzle> fila = new LinkedList<>();
        Set<String> visitados = new HashSet<>();

        EstadoPuzzle inicio = new EstadoPuzzle(estadoInicial, xZero, yZero, 0, null);
        fila.add(inicio);
        visitados.add(inicio.getChave());

        System.out.println("=== TELA INICIAL: Estado do Tabuleiro ===");
        imprimirTabuleiro(estadoInicial);

        while (!fila.isEmpty()) {
            EstadoPuzzle atual = fila.poll();

            if (atual.getChave().equals(estadoObjetivo)) {
                System.out.println("\n=== TELA FINAL: Resultado ao Término ===");
                System.out.println("Número mínimo de movimentos: " + atual.passos);
                imprimirCaminho(atual);
                return;
            }

            for (int i = 0; i < 4; i++) {
                int novoX = atual.xVazio + dx[i];
                int novoY = atual.yVazio + dy[i];

                if (novoX >= 0 && novoX < 3 && novoY >= 0 && novoY < 3) {
                    int[][] novoTabuleiro = new int[3][3];
                    for (int r = 0; r < 3; r++) {
                        novoTabuleiro[r] = atual.tabuleiro[r].clone();
                    }

                    // Troca a peça adjacente com o zero
                    novoTabuleiro[atual.xVazio][atual.yVazio] = novoTabuleiro[novoX][novoY];
                    novoTabuleiro[novoX][novoY] = 0;

                    EstadoPuzzle novoEstado = new EstadoPuzzle(novoTabuleiro, novoX, novoY, atual.passos + 1, atual);

                    if (!visitados.contains(novoEstado.getChave())) {
                        visitados.add(novoEstado.getChave());
                        fila.add(novoEstado);
                    }
                }
            }
        }
        System.out.println("Sem solução.");
    }

    private static void imprimirTabuleiro(int[][] tab) {
        for (int[] linha : tab) {
            for (int val : linha) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    private static void imprimirCaminho(EstadoPuzzle estado) {
        List<EstadoPuzzle> caminho = new ArrayList<>();
        while (estado != null) {
            ((ArrayList<?>) caminho).add(estado);
            estado = estado.pai;
        }
        Collections.reverse(caminho);

        for (int i = 0; i < ((ArrayList<?>) caminho).size(); i++) {
            System.out.println("\nPasso " + i + ":");
            imprimirTabuleiro(caminho.get(i).tabuleiro);
        }
    }

    public static void main(String[] args) {
        int[][] inicio = {
                {1, 2, 3},
                {4, 0, 6},
                {7, 5, 8}
        };
        resolver(inicio);
    }
}

}
