package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EstadoPuzzle {
    private int[][] tabuleiro;
    private int xVazio;
    private int yVazio;
    private int passos;
    private EstadoPuzzle pai;
    private String chave; // Cache da chave para melhor performance

    // Constante para o tamanho do tabuleiro
    public static final int TAMANHO = 3;

    public EstadoPuzzle(int[][] tabuleiro, int xVazio, int yVazio, int passos, EstadoPuzzle pai) {
        this.tabuleiro = new int[TAMANHO][TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            this.tabuleiro[i] = tabuleiro[i].clone();
        }
        this.xVazio = xVazio;
        this.yVazio = yVazio;
        this.passos = passos;
        this.pai = pai;
        this.chave = gerarChave();
    }

    // Construtor para estado inicial
    public EstadoPuzzle(int[][] tabuleiro) {
        this(tabuleiro, encontrarVazioX(tabuleiro), encontrarVazioY(tabuleiro), 0, null);
    }

    // Métodos para encontrar a posição do vazio
    private static int encontrarVazioX(int[][] tabuleiro) {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (tabuleiro[i][j] == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int encontrarVazioY(int[][] tabuleiro) {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (tabuleiro[i][j] == 0) {
                    return j;
                }
            }
        }
        return -1;
    }

    // Gera a chave única para o estado
    private String gerarChave() {
        StringBuilder sb = new StringBuilder();
        for (int[] linha : tabuleiro) {
            for (int val : linha) {
                sb.append(val);
            }
        }
        return sb.toString();
    }

    public String getChave() {
        return chave;
    }

    // Verifica se o estado é o objetivo
    public boolean isObjetivo() {
        int esperado = 1;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (i == TAMANHO - 1 && j == TAMANHO - 1) {
                    if (tabuleiro[i][j] != 0) return false;
                } else {
                    if (tabuleiro[i][j] != esperado++) return false;
                }
            }
        }
        return true;
    }

    // Gera os movimentos possíveis a partir do estado atual
    public List<EstadoPuzzle> gerarMovimentos() {
        List<EstadoPuzzle> movimentos = new ArrayList<>();

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int novaLinha = xVazio + dx[i];
            int novaColuna = yVazio + dy[i];

            if (novaLinha >= 0 && novaLinha < TAMANHO && novaColuna >= 0 && novaColuna < TAMANHO) {
                int[][] novoTabuleiro = copiarTabuleiro();
                novoTabuleiro[xVazio][yVazio] = novoTabuleiro[novaLinha][novaColuna];
                novoTabuleiro[novaLinha][novaColuna] = 0;

                EstadoPuzzle novoEstado = new EstadoPuzzle(
                        novoTabuleiro,
                        novaLinha,
                        novaColuna,
                        passos + 1,
                        this
                );
                movimentos.add(novoEstado);
            }
        }
        return movimentos;
    }

    private int[][] copiarTabuleiro() {
        int[][] copia = new int[TAMANHO][TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            copia[i] = tabuleiro[i].clone();
        }
        return copia;
    }

    // Getters
    public int[][] getTabuleiro() {
        int[][] copia = new int[TAMANHO][TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            copia[i] = tabuleiro[i].clone();
        }
        return copia;
    }

    public int getXVazio() {
        return xVazio;
    }

    public int getYVazio() {
        return yVazio;
    }

    public int getPassos() {
        return passos;
    }

    public EstadoPuzzle getPai() {
        return pai;
    }

    // Reconstroi o caminho até o estado inicial
    public List<EstadoPuzzle> getCaminho() {
        List<EstadoPuzzle> caminho = new ArrayList<>();
        EstadoPuzzle atual = this;
        while (atual != null) {
            caminho.add(0, atual);
            atual = atual.pai;
        }
        return caminho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadoPuzzle that = (EstadoPuzzle) o;
        return chave.equals(that.chave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chave);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estado ").append(passos).append(" passos\n");
        for (int[] linha : tabuleiro) {
            sb.append("[");
            for (int j = 0; j < linha.length; j++) {
                sb.append(linha[j] == 0 ? " " : linha[j]);
                if (j < linha.length - 1) sb.append(", ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}