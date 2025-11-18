package com.demo.dietiestates25.support_classes.retrofit_utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ErrorResponseHandler {
    private final String httpErrorCode;
    private final String parseErrorBodyLabel;

    public ErrorResponseHandler(String httpErrorCodeLabel, String parseErrorBodyLabel) {
        this.httpErrorCode = httpErrorCodeLabel;
        this.parseErrorBodyLabel = parseErrorBodyLabel;
    }

    public String handleErrorResponse(Response<?> response) {
        String errorMsg = httpErrorCode + response.code();

        try {
            ResponseBody errorBody = response.errorBody();

            if(errorBody != null) {
                errorMsg = processErrorBody(errorBody, errorMsg);
            }
        } catch (IOException e) {
            errorMsg += parseErrorBodyLabel;
        }
        return errorMsg;
    }

    private String processErrorBody(ResponseBody errorBody, String initialErrorMsg) throws IOException {
        String errorMsg = initialErrorMsg;
        String memberName1 = "message";
        String memberName2 = "timestamp";

        try(ResponseBody body = errorBody) {
            JsonObject jsonObject = JsonParser.parseString(body.string()).getAsJsonObject();

            if(jsonObject.has(memberName1)) { errorMsg += "\n\n" + jsonObject.get(memberName1).getAsString(); }

            if(jsonObject.has(memberName2)) { errorMsg += "\n\nLast verified:\n" + jsonObject.get(memberName2).getAsString(); }
        }
        return errorMsg;
    }
}
