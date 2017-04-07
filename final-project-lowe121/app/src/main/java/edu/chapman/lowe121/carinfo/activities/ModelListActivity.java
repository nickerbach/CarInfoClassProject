package edu.chapman.lowe121.carinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import edu.chapman.lowe121.carinfo.R;
import edu.chapman.lowe121.carinfo.models.ListOfMakesModelsYears.Make;
import edu.chapman.lowe121.carinfo.models.ListOfMakesModelsYears.Makes;
import edu.chapman.lowe121.carinfo.models.ListOfMakesModelsYears.Model;
import edu.chapman.lowe121.carinfo.models.ListOfMakesModelsYears.Year;

public class ModelListActivity extends AppCompatActivity {

    private static final String TAG = "ModelListActivity";
    public static String MAKE_NAME = "MakeName";
    String[] modelNames;
    String makeName;
    String year;
    private Make make = new Make();
    private Model model;
    private TextView yearPickerTitle;
    private NumberPicker modelPicker;
    private NumberPicker yearPicker;
    private Button loadButton;
    private String modelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_list);

        loadButton = (Button) findViewById(R.id.btn_load);

        //Get Make Name
        makeName = getIntent().getStringExtra(MAKE_NAME);

        //init modelPickerTitle
        TextView modelPickerTitle = (TextView) findViewById(R.id.picker_title);
        modelPickerTitle.setText(String.format("%s %s", getString(R.string.model_selector), makeName));

        Log.d(TAG, "onCreate: " + makeName);

        Makes makes = MakeListActivity.getMakes();

        for (Make make : makes.makes) {
            if (make.name.contains(makeName)) {
                this.make = make;
            }
        }

        initModelPicker();
        initYearPicker();

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelListActivity.this, ModelDetailActivity.class);
                intent.putExtra(ModelDetailActivity.MAKE_NAME, makeName);
                intent.putExtra(ModelDetailActivity.MODEL_NAME, modelName);
                intent.putExtra(ModelDetailActivity.YEAR, year);
                ModelListActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        modelName = modelNames[modelPicker.getValue()];
        model = make.models.get(modelPicker.getValue());
        year = model.years.get(yearPicker.getValue()).year;
        loadYears();

    }

    private void initYearPicker() {
        yearPicker = (NumberPicker) findViewById(R.id.year_picker);

        yearPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    year = model.years.get(yearPicker.getValue()).year;
                }
            }
        });
        yearPickerTitle = (TextView) findViewById(R.id.picker_title2);
    }

    private void loadYears() {
        int size = model.years.size();

        final String[] modelYears = new String[size];

        int i = 0;
        for (Year year : model.years) {
            modelYears[i] = year.year;
            i++;
        }

        yearPickerTitle.setText(String.format("%s %s %s", getString(R.string.year_selector), makeName, modelName));

        yearPicker.setDisplayedValues(null);
        yearPicker.setMinValue(0);
        yearPicker.setMaxValue(modelYears.length - 1);
        yearPicker.setDisplayedValues(modelYears);
    }

    private void initModelPicker() {

        modelPicker = (NumberPicker) findViewById(R.id.model_picker);

        modelNames = new String[make.models.size()];

        for (int i = 0; i < make.models.size(); ++i) {
            modelNames[i] = make.models.get(i).name;
        }

        modelPicker.setMinValue(0);
        modelPicker.setMaxValue(modelNames.length - 1);
        modelPicker.setDisplayedValues(modelNames);

        modelPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    modelName = modelNames[modelPicker.getValue()];
                    model = make.models.get(modelPicker.getValue());
                    loadYears();
                }
            }
        });
    }
}
