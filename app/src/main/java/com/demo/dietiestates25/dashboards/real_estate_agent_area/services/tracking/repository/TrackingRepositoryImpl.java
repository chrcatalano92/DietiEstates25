package com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking.TrackedOfferData;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.RetrofitClient;
import com.demo.dietiestates25.utils.FeedbackUtils;
import com.demo.dietiestates25.utils.LogUtils;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingRepositoryImpl implements TrackingRepository {
    private static final String TAG = "TrackingRepositoryImpl";
    private final PropertyServiceAPI apiService;
    private final Context appContext;

    public TrackingRepositoryImpl(Context applicationContext) {
        this.appContext = applicationContext.getApplicationContext();
        this.apiService = RetrofitClient.getRetrofitInstance(appContext);
    }

    @Override
    public void getAgentTrackedOffers(int page, int size, @Nullable String sortBy, boolean isAscending, LoadTrackedOffersCallback callback) {
        String sortDir = isAscending ? "asc" : "desc";
        String effectiveSortBy = sortBy == null ? "actionDateTime" : sortBy;

        Call<List<TrackedOfferData>> call = apiService.getAgentTrackedOfferApi(page, size, effectiveSortBy, sortDir);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<TrackedOfferData>> call, @NonNull Response<List<TrackedOfferData>> response) {
                if(response.isSuccessful()) {
                    handleSuccessfulResponse(response, callback);
                }
                else {
                    handleErrorResponse(response, callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TrackedOfferData>> call, @NonNull Throwable t) {
                String networkError = ResourceUtils.getXMLString(appContext, R.string.retrofit_network_error);
                callback.onError(t.getMessage() != null ? t.getMessage() : networkError);
            }
        });
    }

    private void handleSuccessfulResponse(Response<List<TrackedOfferData>> response, LoadTrackedOffersCallback callback) {
        if(response.body() != null) {
            callback.onSuccess(response.body());
        }
        else {
            callback.onError(ResourceUtils.getXMLString(appContext, R.string.error_empty_response));
        }
    }

    private void handleErrorResponse(Response<List<TrackedOfferData>> response, LoadTrackedOffersCallback callback) {
        String errorMsg = ResourceUtils.getXMLString(appContext, R.string.error_loading_repository) + response.code();
        ResponseBody errorBody = response.errorBody();

        if(errorBody != null) {
            try (errorBody) {
                errorMsg += " - " + errorBody.string();
            } catch (Exception e) {
                String parsingError = ResourceUtils.getXMLString(appContext, R.string.error_parsing_response);
                LogUtils.logError(TAG, parsingError, e);
            }
        }
        callback.onError(errorMsg);
    }

    @Override
    public void exportTrackedOffers(List<TrackedOfferData> offersToExport, SimpleActionCallback callback) {
        FeedbackUtils.notImplemented(appContext);
    }

    @Override
    public void deleteTrackedOffers(List<String> offerIdToDelete, SimpleActionCallback callback) {
        FeedbackUtils.notImplemented(appContext);
    }
}
