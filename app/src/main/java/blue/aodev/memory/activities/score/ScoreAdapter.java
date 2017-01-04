package blue.aodev.memory.activities.score;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import blue.aodev.memory.R;
import blue.aodev.memory.data.score.Score;

/**
 * A {@link RecyclerView.Adapter} to display scores.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private List<Score> scores;

    public ScoreAdapter(@NonNull List<Score> scores) {
        setValues(scores);
    }

    public void replaceData(@NonNull List<Score> scores) {
        setValues(scores);
        notifyDataSetChanged();
    }

    private void setValues(@NonNull List<Score> scores) {
        this.scores = scores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int score = scores.get(position).getScore();
        holder.positionView.setText(String.valueOf(position + 1));
        holder.scoreView.setText(String.valueOf(score));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        @BindView(R.id.position) TextView positionView;
        @BindView(R.id.score) TextView scoreView;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }
}
