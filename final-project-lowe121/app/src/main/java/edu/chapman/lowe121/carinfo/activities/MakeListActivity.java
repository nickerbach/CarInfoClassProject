package edu.chapman.lowe121.carinfo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.chapman.lowe121.carinfo.R;
import edu.chapman.lowe121.carinfo.adapters.MakesAdapter;
import edu.chapman.lowe121.carinfo.api_wrappers.EdmundsAPI;
import edu.chapman.lowe121.carinfo.models.ListOfMakesModelsYears.Make;
import edu.chapman.lowe121.carinfo.models.ListOfMakesModelsYears.Makes;
import edu.chapman.lowe121.carinfo.models.SpecificModelDetails.Styles;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MakeListActivity extends AppCompatActivity {

    public final static String TAG = "MakeListActivity";
    private static Makes makes;
    private Styles styles;
    private List<Styles> stylesList;
    private List<String> makeNames = new ArrayList<>();
    private List<String> filteredMakeNames = new ArrayList<>();
    private RecyclerView rv_makeList;
    private boolean error = false;
    private ProgressBar loading;

    public static Makes getMakes() {
        return makes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect Views
        final EditText et_makeFilter = (EditText) findViewById(R.id.et_make_filter);
        rv_makeList = (RecyclerView) findViewById(R.id.rv_list_of_makes);
        rv_makeList.setAdapter(new MakesAdapter(makeNames, this));
        rv_makeList.setLayoutManager(new LinearLayoutManager(this));
        loading = (ProgressBar) findViewById(R.id.loading);

        //Populate Data
        EdmundsAPI.ListAllCars(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MakeListActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.e(TAG, "ListAllCars: error occured", e.fillInStackTrace());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String resp = response.body().string();

                if (resp.contains("FORBIDDEN")) {
                    error = true;
                    Log.e(TAG, "ListAllCars: onResponse: " + resp);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MakeListActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    makes = new Gson().fromJson(resp, Makes.class);
                    Log.d(TAG, "onResponse: makes populated");
                    if (makes.makes != null) {
                        for (Make make : makes.makes) {
                            makeNames.add(make.name);

                            //TODO: fix api call limit
                            //Causes a crash due to api call limit...
//                            for (Model model : make.models) {
//                                for (Year years : model.years) {
//                                    EdmundsAPI.getModelDetails(make.name, model.name, years.year, new Callback() {
//                                        @Override
//                                        public void onFailure(Call call, IOException e) {
//
//                                        }
//
//                                        @Override
//                                        public void onResponse(Call call, Response response) throws IOException {
//                                            String resp = response.body().string();
//                                            if (resp.contains("FORBIDDEN") || resp.contains("BAD")) {
//                                                Log.e(TAG, "getModelDetails: onResponse: " + resp);
//                                                error = true;
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        finish();
//                                                    }
//                                                });
//                                            } else {
//                                                styles = new Gson().fromJson(resp, Styles.class);
//                                                stylesList.add(styles);
//                                            }
//                                        }
//                                    });
//                                }
//                            }
                        }
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv_makeList.getAdapter().notifyDataSetChanged();
                            rv_makeList.setVisibility(View.VISIBLE);
                            et_makeFilter.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
//                            for (Styles styles : stylesList)
//                            {
//                                for (Style style : styles.styles)
//                                {
//                                    Log.d(TAG, style.name);
//                                }
//                            }
                        }
                    });
                }
            }
        });


        et_makeFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!error) {
                    filteredMakeNames.clear();
                    for (Make make : makes.makes) {
                        String result = make.name.toLowerCase();
                        if (result.contains(s.toString().toLowerCase())) {
                            filteredMakeNames.add(make.name);
                        }
                    }
                    rv_makeList.setAdapter(new MakesAdapter(filteredMakeNames, MakeListActivity.this));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
