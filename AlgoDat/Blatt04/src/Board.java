import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Stack;

import static java.lang.Math.abs;

/**
 * This class represents a generic TicTacToe game board.
 */
public class Board {
    private int n;
    public int[][] card;
    public int nfree;
    int winner = 0;

    /**
     * Creates Board object, am game board of size n * n with 1<=n<=10.
     */
    public Board(int n) {
        // TODO
        if (n > 10) {
            throw new InputMismatchException("n > 10 ");
        }
        if (n < 1) {
            throw new InputMismatchException("n < 1");
        }
        this.n = n;
        this.card = new int[n][n];
        this.nfree = n * n;
    }

    /**
     * @return length/width of the Board object
     */
    public int getN() {
        return n;
    }

    /**
     * @return number of currently free fields
     */
    public int nFreeFields() {
        // TODO
        return this.nfree;
    }

    /**
     * @return token at position pos
     */
    public int getField(Position pos) throws InputMismatchException {
        // TODO
        if ((pos.x > n - 1) || (pos.x < 0) || (pos.y > n - 1) || (pos.y < 0)) {
            throw new InputMismatchException("non-valid input of pos");
        }
        return this.card[pos.x][pos.y];
    }

    /**
     * Sets the specified token at Position pos.
     */
    public void setField(Position pos, int token) throws InputMismatchException {
        // TODO
        if ((token != 0) && (token != 1) && (token != -1)) {
            throw new InputMismatchException("non-valid input of token");
        }
        if ((pos.x > n - 1) || (pos.x < 0) || (pos.y > n - 1) || (pos.y < 0)) {
            throw new InputMismatchException("non-valid input of pos");
        }
        if (token == 0) {
            if (this.card[pos.x][pos.y] == 0) {
            } else {
                this.card[pos.x][pos.y] = token;
                this.nfree++;
            }
        } else {
            if (this.card[pos.x][pos.y] == 0) {
                this.card[pos.x][pos.y] = token;
                this.nfree--;
            } else {
                this.card[pos.x][pos.y] = token;
            }
        }
    }

    /**
     * Places the token of a player at Position pos.
     */
    public void doMove(Position pos, int player) {
        // TODO
        if (this.card[pos.x][pos.y] != 0) {
            throw new InputMismatchException("pos is occupied");
        }
        setField(pos, player);
    }

    /**
     * Clears board at Position pos.
     */
    public void undoMove(Position pos) {
        // TODO
        if (getField(pos) == 0){
            throw new InputMismatchException("non-valid input of pos");
        }
        this.setField(pos,0);
    }

    /**
     * @return true if game is won, false if not
     */
    public boolean isGameWon() {
        // TODO
        for (int i = 0; i < n; i++) {
            int num = 0;
            for (int j = 0; j < n; j++) {
                if (this.card[i][0] == 0){
                    break;
                }
                if (this.card[i][j] == this.card[i][0]) {
                    num++;
                }
            }
            if (num == n ){
                winner = card[i][0];
               return true ;
            }
        }
        for (int i = 0; i < n; i++) {
            int num = 0;
            for (int j = 0; j < n; j++) {
                if (this.card[0][i] == 0){
                    break;
                }
                if (this.card[j][i] == this.card[0][i]) {
                    num++;
                }
            }
            if (num == n ){
                winner = this.card[0][i];
                return true;
            }
        }
        int i = 0;
        int j = 0;
        int num = 0;
        while ( i < n ){
            if (this.card[0][0] == 0){
                break;
            }
           if (this.card[i][j] == this.card[0][0] ){
               num ++ ;
           }
           i++;
           j++;
        }
        if ( num == n ){
            winner = this.card[0][0];
            return true ;
        }
        i = 0;
        j = n-1;
        num = 0;
        while ( i < n ){
            if (this.card[0][n-1] == 0){
                break;
            }
            if (this.card[i][j] == this.card[0][n-1] ){
                num ++ ;
            }
            i++;
            j--;
        }
        if ( num == n ){
            winner = this.card[0][n-1];
            return true ;
        }

       return false ;
    }

    /**
     * @return set of all free fields as some Iterable object
     */
    public Iterable<Position> validMoves() {
        // TODO
        LinkedList<Position> listOfFree = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.card[i][j] == 0) {
                    Position p = new Position(i, j);
                    listOfFree.add(p);
                }
            }
        }
        return listOfFree;
    }

    /**
     * Outputs current state representation of the Board object.
     * Practical for debugging.
     */
    public void print() {
        // TODO
    }

}

