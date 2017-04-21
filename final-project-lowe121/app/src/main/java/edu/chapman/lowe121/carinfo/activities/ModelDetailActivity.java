package edu.chapman.lowe121.carinfo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.NumberFormat;

import edu.chapman.lowe121.carinfo.R;
import edu.chapman.lowe121.carinfo.api_wrappers.EdmundsAPI;
import edu.chapman.lowe121.carinfo.models.PhotoURLs.Photos;
import edu.chapman.lowe121.carinfo.models.SpecificModelDetails.Style;
import edu.chapman.lowe121.carinfo.models.SpecificModelDetails.Styles;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ModelDetailActivity extends AppCompatActivity {

    private static final String TAG = "ModelDetailActivity";
    public static String MAKE_NAME = "MakeName";
    public static String MODEL_NAME = "ModelName";
    public static String YEAR = "Year";

    private String makeName;
    private String modelName;
    private String year;
    private Styles styles;
    private Style style = new Style();
    private Photos photos;
    private boolean error = false;
    private String basePhotoUrl = "https://media.ed.edmunds-media.com";
    private String specificModelURL;

    private TextView title;
    private ImageView carImage;
    private NumberPicker stylePicker;
    private ProgressBar styleProgress;

    private TextView priceDetails;
    private TextView priceMSRP;
    private TextView priceUsed;
    private TextView priceTradeIn;

    private TextView engineHorsepower;
    private TextView engineTorque;
    private TextView engineFuelType;
    private TextView engineSize;
    private TextView engineCylinder;
    private TextView engineDetails;

    private TextView transDetails;
    private TextView transType;
    private TextView transSpeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_detail);

        makeName = getIntent().getStringExtra(MAKE_NAME);
        modelName = getIntent().getStringExtra(MODEL_NAME);
        year = getIntent().getStringExtra(YEAR);

        Log.d(TAG, "onCreate: " + makeName + " " + modelName + " " + year);

        title = (TextView) findViewById(R.id.title);
        carImage = (ImageView) findViewById(R.id.car_image);
        stylePicker = (NumberPicker) findViewById(R.id.style_picker);
        styleProgress = (ProgressBar) findViewById(R.id.style_progress);

        priceDetails = (TextView) findViewById(R.id.price_details);
        priceMSRP = (TextView) findViewById(R.id.price_msrp);
        priceUsed = (TextView) findViewById(R.id.price_used);
        priceTradeIn = (TextView) findViewById(R.id.price_tradein);

        engineHorsepower = (TextView) findViewById(R.id.engine_horsepower);
        engineTorque = (TextView) findViewById(R.id.engine_torque);
        engineFuelType = (TextView) findViewById(R.id.engine_fuelType);
        engineSize = (TextView) findViewById(R.id.engine_size);
        engineCylinder = (TextView) findViewById(R.id.engine_cylinder);
        engineDetails = (TextView) findViewById(R.id.engine_details);

        transDetails = (TextView) findViewById(R.id.transmission_details);
        transType = (TextView) findViewById(R.id.transmission_type);
        transSpeeds = (TextView) findViewById(R.id.transmission_speeds);

        title.setText(year + " " + makeName + " " + modelName);

        EdmundsAPI.getModelDetails(makeName, modelName, Integer.valueOf(year), new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModelDetailActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "run: " + e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                if (resp.contains("FORBIDDEN") || resp.contains("BAD")) {
                    Log.e(TAG, "getModelDetails: onResponse: " + resp);
                    error = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ModelDetailActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    styles = new Gson().fromJson(resp, Styles.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initStylePicker();
                            styleProgress.setVisibility(View.GONE);
                            stylePicker.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        EdmundsAPI.getPictures(EdmundsAPI.FrontQuarter, makeName, modelName, Integer.valueOf(year), new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModelDetailActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                if (resp.contains("FORBIDDEN") || resp.contains("BAD_REQUEST")) {
                    error = true;
                    Log.e(TAG, "getPictures: onResponse: " + resp);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(ModelDetailActivity.this).load(R.drawable.no_image_available).into(carImage);
                        }
                    });
                } else {
                    photos = new Gson().fromJson(resp, Photos.class);
                    Log.d(TAG, "onResponse: " + resp);
                    try {
                        specificModelURL = photos.photos.get(0).sources.get(2).link.href;
                    } catch (IndexOutOfBoundsException | NullPointerException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(ModelDetailActivity.this).load(R.drawable.no_image_available).into(carImage);
                            }
                        });
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Load Image
                            if (specificModelURL != null)
                                Picasso.with(ModelDetailActivity.this).load(basePhotoUrl + specificModelURL).into(carImage);
                            else
                                Picasso.with(ModelDetailActivity.this).load(R.drawable.no_image_available).into(carImage);

                        }
                    });
                }
            }
        });

    }

    private void initStylePicker() {
        String[] styleNames = new String[styles.styles.size()];


        if (styles.styles.size() == 0) {
            Toast.makeText(this, getString(R.string.no_style), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            for (int i = 0; i < styles.styles.size(); ++i) {
                styleNames[i] = styles.styles.get(i).name;
            }
            stylePicker.setMinValue(0);
            stylePicker.setMaxValue(styleNames.length > 0 ? styleNames.length - 1 : 0);
            stylePicker.setDisplayedValues(styleNames);

            final NumberFormat format = NumberFormat.getCurrencyInstance();

            style = styles.styles.get(stylePicker.getValue());

            priceMSRP.setText(String.format("MSRP: %s", format.format(style.price.baseMSRP)));
            priceUsed.setText(String.format("Private Party: %s", format.format(style.price.usedPrivateParty)));
            priceTradeIn.setText(String.format("Trade In: %s", format.format(style.price.usedTradeIn)));
            engineHorsepower.setText(String.valueOf(style.engine.horsepower) + "HP @ " + String.valueOf(style.engine.rpm.horsepower) + " RPM");
            engineTorque.setText(String.valueOf(style.engine.torque) + "ft/lbs @ " + String.valueOf(style.engine.rpm.torque) + " RPM");
            engineFuelType.setText(String.format("%s %s", getString(R.string.fuel_type), style.engine.fuelType));
            engineSize.setText(String.format("%s %s", getString(R.string.size), String.valueOf(style.engine.size)));
            engineCylinder.setText(String.format("%s %s", getString(R.string.cylinderCount), String.valueOf(style.engine.cylinder)));
            transType.setText(String.format("%s %s", getString(R.string.type), style.transmission.transmissionType.replace("_", " ")));
            transSpeeds.setText(String.format("%s %s", getString(R.string.speed_count), String.valueOf(style.transmission.numberOfSpeeds)));

            stylePicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
                @Override
                public void onScrollStateChange(NumberPicker view, int scrollState) {
                    if (scrollState == SCROLL_STATE_IDLE) {
                        style = styles.styles.get(stylePicker.getValue());
                        priceMSRP.setText(String.format("MSRP: %s", format.format(style.price.baseMSRP)));
                        priceUsed.setText(String.format("Private Party: %s", format.format(style.price.usedPrivateParty)));
                        priceTradeIn.setText(String.format("Trade In: %s", format.format(style.price.usedTradeIn)));
                        engineHorsepower.setText(String.valueOf(style.engine.horsepower) + "HP @ " + String.valueOf(style.engine.rpm.horsepower) + " RPM");
                        engineTorque.setText(String.valueOf(style.engine.torque) + "ft/lbs @ " + String.valueOf(style.engine.rpm.torque) + " RPM");
                        engineFuelType.setText(String.format("%s %s", getString(R.string.fuel_type), style.engine.fuelType));
                        engineSize.setText(String.format("%s %s", getString(R.string.size), String.valueOf(style.engine.size)));
                        engineCylinder.setText(String.format("%s %s", getString(R.string.cylinderCount), String.valueOf(style.engine.cylinder)));
                        transType.setText(String.format("%s %s", getString(R.string.type), style.transmission.transmissionType.replace("_", " ")));
                        transSpeeds.setText(String.format("%s %s", getString(R.string.speed_count), String.valueOf(style.transmission.numberOfSpeeds)));


                    }
                }
            });
            engineCylinder.setVisibility(View.VISIBLE);
            engineSize.setVisibility(View.VISIBLE);
            engineTorque.setVisibility(View.VISIBLE);
            engineFuelType.setVisibility(View.VISIBLE);
            engineHorsepower.setVisibility(View.VISIBLE);
            engineDetails.setVisibility(View.VISIBLE);
            carImage.setVisibility(View.VISIBLE);

            transDetails.setVisibility(View.VISIBLE);
            transType.setVisibility(View.VISIBLE);
            transSpeeds.setVisibility(View.VISIBLE);

            priceDetails.setVisibility(View.VISIBLE);
            priceMSRP.setVisibility(View.VISIBLE);
            priceUsed.setVisibility(style.price.usedPrivateParty == 0 ? View.GONE : View.VISIBLE);
            priceTradeIn.setVisibility(style.price.usedTradeIn == 0 ? View.GONE : View.VISIBLE);
        }


    }

}
