package edu.chapman.lowe121.carinfo.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.chapman.lowe121.carinfo.activities.ModelListActivity;

public class MakesAdapter extends RecyclerView.Adapter<MakesAdapter.MakesViewHolder> {

    protected Activity activity;
    private List<String> makes;

    public MakesAdapter(List<String> makes, Activity activity) {
        this.activity = activity;
        this.makes = makes;
    }

    @Override
    public MakesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MakesViewHolder(LayoutInflater.from(activity)
                .inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(MakesViewHolder holder, int position) {
        holder.initialize(this.makes.get(position));
    }


    @Override
    public int getItemCount() {
        return makes.size();
    }

    class MakesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String makesText;
        private TextView textView;

        MakesViewHolder(View itemView) {
            super(itemView);

            this.textView = (TextView) itemView;
            this.textView.setOnClickListener(this);
        }

        void initialize(String text) {
            this.makesText = text;
            this.textView.setText(text);
        }

        @Override
        public void onClick(View view) {
            Intent makesIntent = new Intent(activity, ModelListActivity.class);
            makesIntent.putExtra(ModelListActivity.MAKE_NAME, makesText);
            activity.startActivity(makesIntent);
        }
    }
}
