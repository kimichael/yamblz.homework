package com.gcteam.yamblz.homework.presentation.presenter.city;

import android.support.annotation.NonNull;

import com.gcteam.yamblz.homework.domain.interactor.cities.CityFilterInteractor;
import com.gcteam.yamblz.homework.presentation.BasePresenter;
import com.gcteam.yamblz.homework.presentation.view.city.CityFilterView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * Created by Kim Michael on 03.08.17
 */
public class CityChooserPresenter extends BasePresenter<CityFilterView> {

    private static final int FILTER_DEBOUNCE_TIME = 100;
    private static final int SEARCH_LENGTH_LIMIT = 0;

    private CityFilterInteractor cityFilterInteractor;
    private Disposable filterDisposable;

    @Inject
    public CityChooserPresenter(CityFilterInteractor cityFilterInteractor) {
        this.cityFilterInteractor = cityFilterInteractor;
    }

    @Override
    public void onAttach(@NonNull CityFilterView view) {
        super.onAttach(view);
        filterDisposable =
                view.getFilterObs()
                        .doOnNext(s -> {
                            if (getView() != null) {
                                if (s.length() <= SEARCH_LENGTH_LIMIT) {
                                    getView().hideChosenCities();
                                    getView().hideLoadingSpinner();
                                } else {
                                    getView().showLoadingSpinner();
                                }
                            }
                        })
                        .debounce(FILTER_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                        .filter(s -> s.length() > SEARCH_LENGTH_LIMIT)
                        .subscribe(s -> cityFilterInteractor.getCitiesByFilter(s)
                                .subscribe(filteredCities -> {
                                    if (getView() != null) {
                                        getView().showChosenCities(filteredCities);
                                        getView().hideLoadingSpinner();
                                    }
                                }, throwable -> {
                                    if (getView() != null) {
                                        getView().showError();
                                    }
                                }));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        filterDisposable.dispose();
    }

}
