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

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private List<Integer> scores;

    public ScoreAdapter(@NonNull List<Integer> scores) {
        setValues(scores);
    }

    public void replaceData(@NonNull List<Integer> scores) {
        setValues(scores);
        notifyDataSetChanged();
    }

    private void setValues(@NonNull List<Integer> scores) {
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
        int score = scores.get(position);
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
