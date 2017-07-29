package com.humaniq.apilib.react;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.response.BasePayload;
import com.humaniq.apilib.network.models.response.FacialImage;
import com.humaniq.apilib.network.models.response.FacialImageValidation;
import com.humaniq.apilib.network.models.response.ValidationResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import com.humaniq.apilib.utils.ModelConverterUtils;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ognev on 7/29/17.
 */

public class PhotoValidationModule extends ReactContextBaseJavaModule {

  public PhotoValidationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
    ServiceBuilder.init(Constants.BASE_URL, reactContext);
  }

  @Override public String getName() {
    return "HumaniqPhotoValidation";
  }

  @ReactMethod public void isRegistered(String base64, final Promise promise) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("facial_image", base64);
        ServiceBuilder
            .getValidationService()
            .isRegistered(jsonObject)
            .enqueue(new Callback<BasePayload<FacialImage>>() {
              @Override public void onResponse(Call<BasePayload<FacialImage>> call,
                  Response<BasePayload<FacialImage>> response) {
                if(response.body() != null) {
                  try {
                    WritableMap writableMap =
                        ModelConverterUtils
                            .convertJsonToMap(
                                new JSONObject(
                                    new Gson().toJson(response.body().payload)));

                    promise.resolve(writableMap);
                  } catch (Exception e) {
                    e.printStackTrace();
                    promise.reject(e);
                  }
                }
              }

              @Override public void onFailure(Call<BasePayload<FacialImage>> call,
                  Throwable t) {
                promise.reject(t);
              }
            });
  }

    @ReactMethod public void createValidation(String facialImageId, final Promise promise) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("facial_image_id", facialImageId);
        ServiceBuilder
            .getValidationService()
            .createValidation(jsonObject)
            .enqueue(new Callback<BasePayload<FacialImageValidation>>() {
              @Override public void onResponse(Call<BasePayload<FacialImageValidation>> call,
                  Response<BasePayload<FacialImageValidation>> response) {
                if(response.body() != null) {
                  try {
                    WritableMap writableMap =
                        ModelConverterUtils
                            .convertJsonToMap(
                                new JSONObject(
                                    new Gson().toJson(response.body().payload)));

                    promise.resolve(writableMap);
                  } catch (Exception e) {
                    e.printStackTrace();
                    promise.reject(e);
                  }
                }
              }

              @Override public void onFailure(Call<BasePayload<FacialImageValidation>> call,
                  Throwable t) {
                promise.reject(t);
              }
            });
  }

    @ReactMethod public void validate(String facialImageId, String base64, final Promise promise) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("facial_image_validation_id", facialImageId);
      jsonObject.addProperty("facial_image", base64);

        ServiceBuilder
            .getValidationService()
            .validate(jsonObject)
            .enqueue(new Callback<BasePayload<ValidationResponse>>() {
              @Override public void onResponse(Call<BasePayload<ValidationResponse>> call,
                  Response<BasePayload<ValidationResponse>> response) {
                if(response.body() != null) {
                  try {
                    WritableMap writableMap =
                        ModelConverterUtils
                            .convertJsonToMap(
                                new JSONObject(
                                    new Gson().toJson(response.body().payload)));

                    promise.resolve(writableMap);
                  } catch (Exception e) {
                    e.printStackTrace();
                    promise.reject(e);
                  }
                }
              }

              @Override public void onFailure(Call<BasePayload<ValidationResponse>> call,
                  Throwable t) {
                promise.reject(t);
              }
            });
  }


}
