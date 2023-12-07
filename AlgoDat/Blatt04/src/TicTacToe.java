/**
 * This class implements and evaluates game situations of a TicTacToe game.
 */
public class TicTacToe {

    /**
     * Returns an evaluation for player at the current board state.
     * Arbeitet nach dem Prinzip der Alphabeta-Suche. Works with the principle of Alpha-Beta-Pruning.
     *
     * @param board  current Board object for game situation
     * @param player player who has a turn
     * @return rating of game situation from player's point of view
     **/
    public static int alphaBeta(Board board, int player) {
        // TODO
        if (player == 1) {
            return alphaBeta1(board,-Integer.MAX_VALUE, Integer.MAX_VALUE);
        } else {
            return alphaBeta2(board,-Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
    }

    public static int alphaBeta1(Board board, int alpha, int beta) {
        if ((board.isGameWon()) || (board.nfree == 0)) {
            return score(board);
        }
        for (Position p : board.validMoves()) {
            board.doMove(p, 1);
            int score =  alphaBeta2(board, alpha, beta);
            board.undoMove(p);
            if (score > alpha) {
                alpha = score;
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return alpha;
    }

    public static int alphaBeta2(Board board, int alpha, int beta) {
        if ((board.isGameWon()) || (board.nfree == 0)) {
            return score(board);
        }
        for (Position p : board.validMoves()) {
            board.doMove(p, -1);
            int score =  alphaBeta1(board, alpha, beta);
            board.undoMove(p);
            if (score < beta) {
                beta = score;
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return beta;
    }

    public static int score(Board board) {
        if ( board.isGameWon()) {
            return (board.nfree * board.winner)+(board.winner);
        }
        else {
            return 0;
        }
    }

    /**
     * Vividly prints a rating for each currently possible move out at System.out.
     * (from player's point of view)
     * Uses Alpha-Beta-Pruning to rate the possible moves.
     * formatting: See "Beispiel 1: Bewertung aller Zugmöglichkeiten" (Aufgabenblatt 4).
     *
     * @param board  current Board object for game situation
     * @param player player who has a turn
     **/
    public static void evaluatePossibleMoves(Board board, int player) {
        // TODO
        String [][] fake = new String [board.getN()][board.getN()];
        for (int i = 0 ; i < board.getN() ; i++){
            for (int j = 0 ; j < board.getN() ; j++ ){
                if (board.card[i][j]==-1) {
                    fake[i][j] = "o";
                }
                if (board.card[i][j]==1) {
                    fake[i][j]="x";
                }
            }
        }
        for (Position p : board.validMoves()){
            board.doMove(p,player);
            fake[p.x][p.y] = Integer.toString(alphaBeta(board,player));
            board.undoMove(p);
        }
        for ( int j =0 ; j < board.getN() ; j ++ ){
            for (int i = 0 ; i < board.getN(); i++){
                if ((fake[i][j]=="o") || (fake[i][j]=="x")){
                    System.out.print(" " + fake[i][j] + " ");
                }
                else if ( (Integer.parseInt(fake[i][j]) >= 0) ) {
                    System.out.print(" " + fake[i][j] + " ");
                }
                else{
                    System.out.print(fake[i][j] + " ");
                }
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
    }
}

