package com.planday.deliveroo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.planday.deliveroo.adapter.PlaceAdapter;
import com.planday.deliveroo.model.MapPlace;
import com.planday.deliveroo.service.PlaceService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlaceActivity extends AppCompatActivity {

    public static final String TAG             = "PlaceActivity";
    public static final String SELECTED_PLACES = "SELECTED_PLACES";
    private PlaceService   placeService;
    private List<MapPlace> selectedPlaces;
    private PlaceAdapter   mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        selectedPlaces = new ArrayList<>();
        placeService = new PlaceService();
        mAdapter = new PlaceAdapter(this, selectedPlaces);

        RecyclerView mRecyclerView = findViewById(R.id.selected_places_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        requestGetPlacesFromAPI();

        Button btnFindRoute = findViewById(R.id.button_find_route);
        btnFindRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceActivity.this, MapActivity.class);
                intent.putExtra(SELECTED_PLACES, (Serializable) selectedPlaces);
                startActivity(intent);
            }
        });

        selectedPlaces = initializeData();
        mAdapter.updateData(selectedPlaces);
    }

    private void requestGetPlacesFromAPI() {
        addSubscription(placeService.getAllPlaces()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<List<MapPlace>>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e(TAG, e.getMessage());
                                        }

                                        @Override
                                        public void onNext(List<MapPlace> mapPlaces) {
                                            selectedPlaces = mapPlaces;
                                            mAdapter.updateData(selectedPlaces);
                                        }
                                    }));
    }

    private List<MapPlace> initializeData() {
        List<MapPlace> mapPlaces = new ArrayList<>();
        mapPlaces.add(new MapPlace("Xuat Phat - 201 Linh Nam", 20.985430, 105.872188, "Nha Ngoai"));
        mapPlaces.add(new MapPlace("59 Tran Hung Dao", 21.021921, 105.850099, "Nha Noi"));
        mapPlaces.add(new MapPlace("13 Linh Nam", 20.991720, 105.864168, "Cho Mai Dong"));
        mapPlaces.add(new MapPlace("28 Ho Hoan Kiem", 21.031855, 105.853298, "Mua Roi Nuoc Thang Long"));
        mapPlaces.add(new MapPlace("1 Tran Hung Dao", 21.018894, 105.860558, "Benh Vien 108"));

        return mapPlaces;
    }

    protected void addSubscription(Subscription subscription) {
        SubscriptionManager.subscribe(this.getClass(), subscription);
    }
}
