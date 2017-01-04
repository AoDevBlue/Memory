package blue.aodev.memory.memory;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * A card of the memory board.
 */
public class Card implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(text);
        dest.writeString(type.name());
        dest.writeInt(flipped ? 1 : 0);
    }

    public static final Parcelable.Creator<Card> CREATOR
            = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    private Card(Parcel in) {
        itemId = in.readInt();
        text = in.readString();
        type = Type.valueOf(in.readString());
        flipped = in.readInt() != 0;
    }
}
