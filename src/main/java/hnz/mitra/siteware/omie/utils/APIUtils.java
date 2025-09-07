package hnz.mitra.siteware.omie.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hnz.mitra.siteware.omie.models.APIError;
import hnz.mitra.siteware.omie.models.AccessParams;
import hnz.mitra.siteware.omie.service.APISourceService;
import okhttp3.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class APIUtils {

    private final Integer MAX_TRIES=1;

    public APIUtils(){}

    public String executeRequest(AccessParams accessParams, String id, Integer nextIntegerPageToSkip, String apiUrl)
     throws IOException, InterruptedException, URISyntaxException {
        String accessToken = makeAuthenticationRequest(accessParams);
        int tries = 1;
        String result = null;
        String errorResult;
        while(result == null){
            URL url = new URL(apiUrl.replace("{pagina}", String.valueOf(nextIntegerPageToSkip)));
            errorResult = null;
            Response response = null;
            PrintUtils.printDebug("Request URL: " + url);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(30, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.MINUTES)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            try{
                response = client.newCall(request).execute();
                if(response.code()>300){
                  errorResult=response.body().string();
                    throw new RuntimeException("Not success response from HTTP request, code: "+response.code()+". Error message: "+errorResult);
                }
                result = response.body().string();
            }catch(Exception e){
                e.printStackTrace();
                PrintUtils.printError(e.getMessage());
                if(tries<=MAX_TRIES){
                    PrintUtils.printError("Try count <"+tries+"> is smaller then max tries limit: "+MAX_TRIES+". Thread sleeping for 10 seconds before trying again.");
                    Thread.sleep(10*1000);
                    tries++;
                }else{
                  APIError apiError = new APIError(id, url.toURI().toString(), response.code(),e.getMessage(),errorResult);
                    PrintUtils.printError("Failed getting HTTP response, Max tries reached!!! Will save this error on log table and call next params request. Error: "+apiError);
                    APISourceService.errorList.add(apiError);
                    break;
                }
            }
        }
        return result;
    }

    private String makeAuthenticationRequest(AccessParams accessParams) {
        OkHttpClient client = new OkHttpClient().newBuilder()
         .connectTimeout(10, TimeUnit.MINUTES)
         .writeTimeout(30, TimeUnit.MINUTES)
         .readTimeout(30, TimeUnit.MINUTES)
         .build();

        RequestBody formBody = new FormBody.Builder()
         .add("username", accessParams.getUserName())
         .add("password", accessParams.getPassword())
         .build();

        Request request = new Request.Builder()
         .url(accessParams.getUrl())
         .post(formBody)
         .addHeader("Content-Type", "application/x-www-form-urlencoded")
         .build();

        try {
            PrintUtils.printDebug("Executing authentication request");
            Response execute = client.newCall(request).execute();
            assert execute.body() != null;
            JsonObject jsonObject = new JsonParser().parse(execute.body().string()).getAsJsonObject();
            PrintUtils.printDebug("Successfully executed authentication request");
            return jsonObject.get("accessToken").getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
