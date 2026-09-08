package Principal;

import java.util.*;

public class Main {

    private static final int SIZE = 3; // Tamanho do tabuleiro (3x3 para o quebra-cabeça 8)

    public static void main(String[] args) {
        int[][] initialBoard = {
                {7, 2, 4},
                {5, 0, 6},
                {8, 3, 1}
        };

        int[][] goalBoard = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };

        PuzzleState initialState = new PuzzleState(initialBoard, null);
        PuzzleState goalState = new PuzzleState(goalBoard, null);

        List<PuzzleState> path = aStarSearch(initialState, goalState);
        if (path != null) {
            System.out.println("Número de movimentos: " + (path.size() - 1));
            for (PuzzleState state : path) {
                state.printBoard();
                System.out.println();
            }
        } else {
            System.out.println("Caminho não encontrado.");
        }
    }

    public static List<PuzzleState> aStarSearch(PuzzleState start, PuzzleState goal) {
        PriorityQueue<PuzzleState> openSet = new PriorityQueue<>(Comparator.comparingInt(s -> s.f));
        Map<PuzzleState, PuzzleState> cameFrom = new HashMap<>();
        Map<PuzzleState, Integer> costSoFar = new HashMap<>();
        Set<PuzzleState> closedSet = new HashSet<>();

        openSet.add(start);
        cameFrom.put(start, null);
        costSoFar.put(start, 0);

        while (!openSet.isEmpty()) {
            PuzzleState current = openSet.poll();

            if (closedSet.contains(current)) {
                continue;
            }
            closedSet.add(current);

            if (current.isGoal(goal.board)) {
                return reconstructPath(cameFrom, current);
            }

            for (PuzzleState neighbor : current.getNeighbors()) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                int newCost = costSoFar.get(current) + 1; // supõe-se que o custo de cada movimento é 1
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    costSoFar.put(neighbor, newCost);
                    neighbor.g = newCost;
                    neighbor.f = newCost + neighbor.h;
                    openSet.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }
        return null; // Não foi possível encontrar um caminho
    }

    public static List<PuzzleState> reconstructPath(Map<PuzzleState, PuzzleState> cameFrom, PuzzleState current) {
        List<PuzzleState> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    public static class PuzzleState {
        int[][] board;
        PuzzleState prev;
        int g; // Cost from start
        int h; // Heuristic cost
        int f; // Total cost

        public PuzzleState(int[][] board, PuzzleState prev) {
            this.board = deepCopy(board);
            this.prev = prev;
            this.g = (prev != null) ? prev.g + 1 : 0;
            this.h = calculateHeuristic(board);
            this.f = this.g + this.h;
        }

        public int calculateHeuristic(int[][] currentBoard) {
            int heuristic = 0;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    int value = currentBoard[i][j];
                    if (value != 0) {
                        int targetX = (value - 1) / SIZE;
                        int targetY = (value - 1) % SIZE;
                        heuristic += Math.abs(i - targetX) + Math.abs(j - targetY);
                    }
                }
            }
            return heuristic;
        }

        public List<PuzzleState> getNeighbors() {
            List<PuzzleState> neighbors = new ArrayList<>();
            int zeroPosition = findZero();
            int x = zeroPosition / SIZE;
            int y = zeroPosition % SIZE;

            // Definições para mover o espaço vazio: para cima, para baixo, para a esquerda, para a direita
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

            for (int[] direction : directions) {
                int newX = x + direction[0], newY = y + direction[1];
                if (newX >= 0 && newX < SIZE && newY >= 0 && newY < SIZE) {
                    int[][] newBoard = deepCopy(board);
                    newBoard[x][y] = newBoard[newX][newY];
                    newBoard[newX][newY] = 0;
                    neighbors.add(new PuzzleState(newBoard, this));
                }
            }
            return neighbors;
        }

        private int findZero() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == 0) {
                        return i * SIZE + j;
                    }
                }
            }
            return -1; // Não deveria acontecer se o tabuleiro sempre tiver um zero
        }

        private int[][] deepCopy(int[][] original) {
            int[][] copy = new int[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) {
                System.arraycopy(original[i], 0, copy[i], 0, SIZE);
            }
            return copy;
        }

        public boolean isGoal(int[][] goalBoard) {
            return Arrays.deepEquals(this.board, goalBoard);
        }

        public void printBoard() {
            for (int[] row : board) {
                for (int val : row) {
                    System.out.print(val + " ");
                }
                System.out.println();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PuzzleState that = (PuzzleState) o;
            return Arrays.deepEquals(board, that.board);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(board);
        }
    }
}
