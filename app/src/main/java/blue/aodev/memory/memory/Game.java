package blue.aodev.memory.memory;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class Game {

    public interface FlipListener {
        public void onFlip(int row, int column, @NonNull Card card);
    }

    /** The state of the board **/
    private Card[][] board;

    /** The number of cards the game has **/
    private int cardCount;

    /** The number of items that were matched **/
    private int matchedCardsCount;

    /** Number of flips made **/
    private int flipCount;

    /** The index of the card flipped first during a two cards flip **/
    private Point firstFlippedPos;

    /** A listener of **/
    private FlipListener flipListener;

    public Game(int rowCount, int columnCount, @NonNull List<Card> cards) {
        board = new Card[rowCount][columnCount];
        cardCount = cards.size();

        for (int i = 0; i < cardCount; i++) {
            board[i/columnCount][i%columnCount] = cards.get(i);
        }
    }

    public void setFlipListener(@Nullable FlipListener flipListener) {
        this.flipListener = flipListener;
    }

    public int getCardsCount() {
        return cardCount;
    }

    public int getFlipCount() {
        return flipCount;
    }

    public int getRowCount() {
        return board.length;
    }

    public int getColumnCount() {
        return board[0].length;
    }

    public Card getCard(int row, int column) {
        if (row >= getRowCount() || column >= getColumnCount()) {
            return null;
        }
        return board[row][column];
    }

    public void flip(int row, int column) {
        Card flipping = board[row][column];

        if (flipping.isFlipped()) {
            // We can't flip cards that are already flipped
            return;
        }

        flipAndNotify(row, column, flipping);
        flipCount++;

        if (firstFlippedPos == null) {
            // It's the first card we flip, just store it
            firstFlippedPos = new Point(row, column);
        } else {
            Card firstFlipped = board[firstFlippedPos.x][firstFlippedPos.y];

            if (firstFlipped.getItemId() == flipping.getItemId()) {
                // We have a match! We keep the two cards flipped
                firstFlippedPos = null;

                // We update the matched card counter for the game's end
                matchedCardsCount += 2;
            } else {
                // We flip back the two cards
                flipAndNotify(firstFlippedPos, firstFlipped);
                flipAndNotify(row, column, flipping);
                firstFlippedPos = null;
            }
        }
    }

    private void flipAndNotify(@NonNull Point pos, @NonNull Card card) {
        flipAndNotify(pos.x, pos.y, card);
    }

    private void flipAndNotify(int row, int column, @NonNull Card card) {
        card.flip();
        if (flipListener != null) {
            flipListener.onFlip(row, column, card);
        }
    }

    public boolean isOver() {
        return matchedCardsCount == cardCount;
    }
}
