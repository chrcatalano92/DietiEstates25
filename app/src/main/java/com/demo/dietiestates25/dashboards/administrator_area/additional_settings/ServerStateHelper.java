package com.demo.dietiestates25.dashboards.administrator_area.additional_settings;

import android.content.Context;

import androidx.annotation.NonNull;

import com.demo.dietiestates25.R;
import com.demo.dietiestates25.support_classes.dialog_utils.DialogManager;
import com.demo.dietiestates25.support_classes.retrofit_utility.PropertyServiceAPI;
import com.demo.dietiestates25.support_classes.retrofit_utility.connection_test.PingResponse;
import com.demo.dietiestates25.utils.ResourceUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerStateHelper {
    private final PropertyServiceAPI apiService;
    private final DialogManager dialogManager;
    private final Context context;
    private static final String TAG = "ServiceStateHelper";
    private static final String SEPARATOR = " : ";
    private static final String NEW_LINE = "\n";
    private static final String UNDEFINED = "N/A";

    public ServerStateHelper(PropertyServiceAPI apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
        this.dialogManager = new DialogManager(context);
    }

    public void testServerPingConnection() {
        Call<PingResponse> call = apiService.pingServer();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PingResponse> call, @NonNull Response<PingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PingResponse pingResponse = response.body();

                    try {
                        String status = pingResponse.getStatus();
                        String timestamp = pingResponse.getTimestamp();
                        String timezone = pingResponse.getTimezone();
                        String location = pingResponse.getLocation();

                        pingResponse.setStatus(checkIsNull(status));
                        pingResponse.setTimestamp(checkIsNull(timestamp));
                        pingResponse.setTimezone(checkIsNull(timezone));
                        pingResponse.setLocation(checkIsNull(location));

                        String displayTxt = NEW_LINE + ResourceUtils.getXMLString(context, R.string.status) + SEPARATOR + status
                                + NEW_LINE + ResourceUtils.getXMLString(context, R.string.timestamp) + SEPARATOR + timestamp
                                + NEW_LINE + ResourceUtils.getXMLString(context, R.string.timezone) + SEPARATOR + timezone
                                + NEW_LINE + ResourceUtils.getXMLString(context, R.string.location) + SEPARATOR + location;

                        dialogManager.showConfirmationDialog(R.drawable.Server, R.string.server_state, displayTxt, null, null);
                    } catch (Exception e) {
                        showServerError(R.string.response_format_err, e.getMessage());
                    }
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errMsg = ResourceUtils.getXMLString(context, R.string.http_err) + response.code();

                        if (errorBody != null) {
                            errMsg += SEPARATOR + errorBody.string();
                        }
                        dialogManager.showErrorDialog(TAG, R.string.server_state, errMsg);
                    } catch (IOException ioE) {
                        showServerError(R.string.msg_reading_err, ioE.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PingResponse> call, @NonNull Throwable t) {
                dialogManager.showErrorDialog(TAG, R.string.server_state, t.getMessage());
            }
        });
    }

    private void showServerError(int stringId, Object msg) {
        String errorPrefix = ResourceUtils.getXMLString(context, stringId);
        dialogManager.showErrorDialog(TAG, R.string.server_state, errorPrefix + SEPARATOR + msg);
    }

    private String checkIsNull(String txt) { return txt != null ? txt : UNDEFINED; }
}
