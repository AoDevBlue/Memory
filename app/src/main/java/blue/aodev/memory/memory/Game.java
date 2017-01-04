package blue.aodev.memory.memory;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Game implements Parcelable {

    public interface FlipListener {
        void onFlip(int row, int column, @NonNull Card card);
    }

    /** The state of the board, flattened **/
    private List<Card> cards;

    private int columnCount;
    private int rowCount;

    /** The number of items that were matched **/
    private int matchedCardsCount;

    /** Number of flips made **/
    private int flipCount;

    /** The index of the card flipped first during a two cards flip **/
    private Point firstFlippedPos;

    /** A listener of the game flips **/
    private FlipListener flipListener;

    public Game(int rowCount, int columnCount, @NonNull List<Card> cards) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.cards = new ArrayList<>(cards);
    }

    public void setFlipListener(@Nullable FlipListener flipListener) {
        this.flipListener = flipListener;
    }

    public int getCardsCount() {
        return cards.size();
    }

    public int getFlipCount() {
        return flipCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    @Nullable
    public Card getCard(int row, int column) {
        if (row >= getRowCount() || column >= getColumnCount()) {
            return null;
        }
        int index = row*columnCount + column;
        if (index >= cards.size()) {
            return null;
        }
        return cards.get(index);
    }

    public void flip(int row, int column) {
        Card flipping = getCard(row, column);

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
            Card firstFlipped = getCard(firstFlippedPos.x, firstFlippedPos.y);

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
        return matchedCardsCount == getCardsCount();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getRowCount());
        dest.writeInt(getColumnCount());
        dest.writeTypedList(cards);
        dest.writeInt(matchedCardsCount);
        dest.writeInt(flipCount);
        dest.writeParcelable(firstFlippedPos, 0);
    }

    public static final Parcelable.Creator<Game> CREATOR
            = new Parcelable.Creator<Game>() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    private Game(Parcel in) {
        rowCount = in.readInt();
        columnCount = in.readInt();
        cards = new ArrayList<>();
        in.readTypedList(cards, Card.CREATOR);
        matchedCardsCount = in.readInt();
        flipCount = in.readInt();
        firstFlippedPos = in.readParcelable(getClass().getClassLoader());
    }
}
