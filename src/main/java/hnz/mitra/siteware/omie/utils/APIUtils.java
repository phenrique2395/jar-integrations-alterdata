package hnz.mitra.siteware.omie.utils;

import hnz.mitra.siteware.omie.models.APIError;
import hnz.mitra.siteware.omie.models.AccessParams;
import hnz.mitra.siteware.omie.service.APISourceService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class APIUtils {

    private final Integer MAX_REQUISITIONS_PER_MINUTE=1;
    private ArrayList<Long> requisitionsPerMinute;
    private LinkedList<Long> requisitionsPerSecond;
    private final Integer MAX_TRIES=1;

    public APIUtils(){
        this.requisitionsPerMinute = new ArrayList<>();
        this.requisitionsPerSecond = new LinkedList<>();
    }

    public String executeRequest(AccessParams accessParams, String id, Integer nextIntegerPageToSkip, String apiUrl) throws IOException, InterruptedException, URISyntaxException {
        int tries = 1;
        String result = null;
        String errorResult;
        MessageFormat mf = new MessageFormat(apiUrl);
        while(result == null){
            URL url = getUrl(mf, nextIntegerPageToSkip);
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
                    .addHeader("token_exact", accessParams.getToken())
                    .addHeader("Content-Type", "application/json")
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

    private static URL getUrl(MessageFormat mf, Integer nextIntegerPageToSkip) throws MalformedURLException {
        return new URL(mf.format(new Object[]{nextIntegerPageToSkip}));
    }

    private byte[] buildBody(String id, String query) {
        MessageFormat mf = new MessageFormat("'{'\"id\":\"{0}\",\"query\": \"{1}\"'}'");
        return mf.format(new Object[]{id,query}).getBytes();
    }
}
