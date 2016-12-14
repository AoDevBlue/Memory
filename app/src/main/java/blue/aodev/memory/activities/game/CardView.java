package blue.aodev.memory.activities.game;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import blue.aodev.memory.R;

public class CardView extends android.support.v7.widget.CardView {

    @BindColor(R.color.cardColorCue) int cueColor;
    @BindColor(R.color.cardColorResponse) int responseColor;
    @BindView(R.id.text) TextView textView;

    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.game_card_contents, this);
        ButterKnife.bind(this);
    }

    public void setText(@NonNull String text) {
        textView.setText(text);
    }

    public void setType(Card.Type type) {
        switch (type) {
            case CUE:
                setBackgroundColor(cueColor);
                break;
            case RESPONSE:
            default:
                setBackgroundColor(responseColor);
                break;
        }
    }
}
