package blue.aodev.memory.memory;

import android.support.annotation.NonNull;

/**
 * A card of the memory board.
 */
public class Card {

    public enum Type {
        CUE,
        RESPONSE;
    }

    private final int itemId;
    private final String text;
    private final Type type;
    private boolean flipped = false;

    public Card(int itemId, @NonNull String text, @NonNull Type type) {
        this.itemId = itemId;
        this.text = text;
        this.type = type;
    }

    public int getItemId() {
        return itemId;
    }

    public @NonNull String getText() {
        return text;
    }

    public @NonNull Type getType() {
        return type;
    }

    public boolean isFlipped() {
        return flipped;
    }

    void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    void flip() {
        flipped = !flipped;
    }
}
