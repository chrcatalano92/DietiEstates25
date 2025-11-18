package com.demo.dietiestates25.support_classes.retrofit_utility;

import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.real_estate_adv_create.collecting_data_steps.PropertyStepsRequest;
import com.demo.dietiestates25.dashboards.real_estate_agent_area.services.tracking.TrackedOfferData;
import com.demo.dietiestates25.data.dto.property.PropertyResponseDTO;
import com.demo.dietiestates25.support_classes.real_estate_ads.RealEstateAd;
import com.demo.dietiestates25.support_classes.real_estate_ads.advanced_search.filters_layout_manager.support_query_filters_helper.AdvSearchFiltersRequest;
import com.demo.dietiestates25.support_classes.retrofit_utility.connection_test.PingResponse;
import com.demo.dietiestates25.support_classes.retrofit_utility.property_save.PropertySaveResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interfaccia Retrofit dedicata ESCLUSIVAMENTE alla Gestione della Proprietà Immobiliari.
 */
public interface PropertyServiceAPI {
    @GET("api/public/health")
    Call<String> healthCheck();

    @GET("api/ping")
    Call<PingResponse> pingServer();

    @GET("api/all-properties")
    Call<List<RealEstateAd>> getAllProperties();

    @GET("api/data/list/{tableName}/{columnName}")
    Call<List<String>> getListFromTable(@Path("tableName") String tableName, @Path("columnName") String columnName);

    @GET("api/filter/{filterType}/{inputCriteria}")
    Call<List<String>> getListFromFilterTableOrFilterName(@Path("filterType") String filterType, @Path("inputCriteria") String inputCriteria);

    @GET("api/filter/filterRadius")
    Call<List<String>> getListFromFilterRadius(@Query("userLongitude") double userLongitude, @Query("userLatitude") double userLatitude, @Query("metersRadius") int metersRadius);

    @GET("api/properties/properties-search")
    Call<List<RealEstateAd>> searchProperties(@Query("criteriaSearch") String criteriaSearch);

    @POST("api/properties/steps-save-property")
    Call<PropertySaveResponse> savePropertiesSteps(@Body PropertyStepsRequest request);

    @POST("api/filter/all-advanced-search-filters")
    Call<List<String>> getListFromAllAdvancedSearchFilters(@Body AdvSearchFiltersRequest filters);

    @POST("api/tracking")
    Call<List<TrackedOfferData>> getAgentTrackedOfferApi(
//            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sortBy") String sortBy,
            @Query("sortDir") String sortDir
    );

    @Multipart
    @POST("api/images/upload")
    Call<ResponseBody> uploadImg(
            @Part MultipartBody.Part img,
            @Part("propertyId") RequestBody propertyId,
            @Part("folderName") RequestBody folder
    );

    /**
     * NUOVO ENDPOINT:
     * Endpoint per la creazione di un immobile con upload di immagine.
     * Accetta una parte 'property' (il DTO in formato JSON) e una parte 'imageFile' (il file).
     */
    @Multipart
    @POST("api/properties/create") // Assicurati che questo endpoint corrisponda al tuo Backend
    Call<PropertyResponseDTO> createProperty(
            @Part("property") RequestBody propertyDto, // Il DTO come RequestBody JSON
            @Part MultipartBody.Part imageFile         // Il file immagine
    );

    /* TUTTI gli altri metodi RELATIVI alle proprietà (...) */
}
