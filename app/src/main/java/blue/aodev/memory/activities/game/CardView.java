package blue.aodev.memory.activities.game;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import blue.aodev.memory.R;
import blue.aodev.memory.memory.Card;

/**
 * A view for a card of the memory game.
 *
 * FIXME The card currently has an elevation of 0 because of
 * its shadow behaviour while flipped.
 */
public class CardView extends android.support.v7.widget.CardView {

    @BindColor(R.color.cardColorCue) int cueColor;
    @BindColor(R.color.cardColorResponse) int responseColor;
    @BindView(R.id.text) TextView textView;

    private GameContract.CardInfo cardInfo;

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

        cardInfo = new GameContract.CardInfo(Card.Type.CUE, "", false);
        updateType();
        setRotationY(180f);
        textView.setVisibility(INVISIBLE);
    }

    /**
     * Update the view according to the data.
     * @param cardInfo the data to display.
     */
    public void setCardInfo(@NonNull GameContract.CardInfo cardInfo) {
        GameContract.CardInfo oldCardInfo = this.cardInfo;
        this.cardInfo = cardInfo;

        if (!oldCardInfo.text.equals(cardInfo.text)) {
            updateText();
        }
        if (oldCardInfo.type != cardInfo.type) {
            updateType();
        }
        if (oldCardInfo.flipped != cardInfo.flipped) {
            updateFlipped();
        }
    }

    private void updateText() {
        textView.setText(cardInfo.text);
    }

    public void updateType() {
        int color;
        switch (cardInfo.type) {
            case CUE:
                color = cueColor;
                break;
            case RESPONSE:
            default:
                color = responseColor;
                break;
        }
        setCardBackgroundColor(color);
    }

    /**
     * Animate the flip of the card.
     * The text visibility is updated at the middle of the animation.
     */
    public void updateFlipped() {
        final boolean showText = cardInfo.flipped;
        float start = cardInfo.flipped ? 180 : 0;
        float end = cardInfo.flipped ? 0 : 180;

        final ObjectAnimator anim = ObjectAnimator.ofFloat(this, "rotationY", start, end);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedFraction() > 0.5f) {
                    int visibility = showText ? View.VISIBLE : View.INVISIBLE;
                    textView.setVisibility(visibility);
                    anim.removeUpdateListener(this);
                }
            }
        });
        anim.start();
    }
}
