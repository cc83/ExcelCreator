package main.java.datasdownloading;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import main.java.datasdownloading.entities.Campaign;
import main.java.datasdownloading.entities.CampaignHeader;
import main.java.datasdownloading.entities.PeriodData;
import main.java.excelreader.entities.CampaignRowPeriod;
import main.java.utils.Utils;

public class HttpDownload {

   

    private String userName; // gpapp

    private String password; // Zup38fer

    private String sessionId;

    private XmlReader xmlReader;

    private static HttpClient client;

    static {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000).setSocketTimeout(10000)
                .setConnectionRequestTimeout(10000).build();

        client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig).build();
    }

    public HttpDownload(String userName, String password) throws Exception {

        this.userName = userName;
        this.password = password;

        HttpMessage m = login(this.userName, this.password);

        if (!m.isOk()) {

            throw new LoginException(m.getErrorMessage());
        }
    }

    public HttpDownload() throws Exception {
        this("omg_hu_api_test", "cpr4932sw");
    }

    private static HttpMessage sendGet(String url) {

        HttpGet request = null;
        try {
            request = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            return new HttpMessage(false,
                    "Only aphanumerical characters allowed", "");
        }

        try {

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            // Read the contents of an entity and return it as a String.
            String content = EntityUtils.toString(entity);

            return new HttpMessage(true, "OK", content);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return new HttpMessage(false,
                    "Connection with the server failed.\nPlease check your internet connection",
                    "");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return new HttpMessage(false, e.getMessage(), "");
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpMessage(false, e.getMessage(), "");
        }

    }

    /**
     * Tries to log in with the distant host gdeapi.gemius.com, if success fills
     * sessionId
     * 
     * @param loginName
     * @param password
     * @return a message containing infos about the success of the login
     */
    private HttpMessage login(String loginName, String password) {

        String url = "https://gdeapi.gemius.com/OpenSession.php?ignoreEmptyParams=Y&login="
                + loginName + "&passwd=" + password;

        HttpMessage m = sendGet(url);

        if (m.isOk()) {

            try {
                sessionId = XmlReader.getSessionID(m.getContent());
            } catch (LoginException e) {
                return new HttpMessage(false, e.getMessage(), "");
            }

        }

        return m;
    }

    

    private HttpMessage getXmlDataByCampaignID(String campaignID) {
        String url = "https://gdeapi.gemius.com/GetTechStats.php?ignoreEmptyParams=Y&sessionID="
                +sessionId+"&dimensionIDs=10&indicatorIDs=4%2C2&techDimension=Country&campaignIDs="+
                campaignID+"&showNames=Y";

        HttpMessage m = sendGet(url);

        if (m.isOk())
            return new HttpMessage(true, "", m.getContent());
        return m;
    }

    

    

    public boolean isSameLogin(HttpDownload other) {
        if (other == null)
            return false;
        return other.client.equals(client) && other.password.equals(password);
    }

    public boolean isSameLogin(String login, String password) {

        return this.userName.equals(login) && this.password.equals(password);
    }

    public void close() {
        String url = "https://gdeapi.gemius.com/CloseSession.php?ignoreEmptyParams=Y&sessionID="
                + sessionId;

        sendGet(url);

    }

   

    

//    public static void main(String[] args) throws Exception {
//        HttpDownload a = new HttpDownload();
//        // Campaign c = a.getCampaignTechnicalById("557150106");
//
//        // System.out.println(/c.getCampaignContent());
//        // System.out.println(c.getAll());
////        HttpMessage m = a.getXmlAllData("557150106", "Week");
////        System.out.println(m.getContent());
//    }

    public static boolean isInternetConnected() {

        Callable<Boolean> callable1 = new Callable<Boolean>() {
            @Override
            public Boolean call() {

                return sendGet("https://www.google.com").isOk();
            }
        };
        Callable<Boolean> callable2 = new Callable<Boolean>() {
            @Override
            public Boolean call() {

                return sendGet("https://www.facebook.com").isOk();
            }
        };
        Callable<Boolean> callable3 = new Callable<Boolean>() {
            @Override
            public Boolean call() {

                return sendGet("https://www.amazon.com").isOk();
            }
        };

        List<Callable<Boolean>> callableList = new ArrayList<Callable<Boolean>>();
        callableList.add(callable1);
        callableList.add(callable2);
        callableList.add(callable3);

        final ExecutorService service = Executors.newFixedThreadPool(3);

        List<Future<Boolean>> futureObjects = null;
        try {
            futureObjects = service.invokeAll(callableList);
        } catch (InterruptedException e) {
            return false;
        }

        try {
            if (futureObjects.get(0).get() || futureObjects.get(1).get()
                    || futureObjects.get(2).get()) {

                return true;

            }
        } catch (InterruptedException | ExecutionException e) {

        }

        return false;

    }

    public static boolean isGemiusReachable() {
        String url = "https://gdeapi.gemius.com/OpenSession.php?";
        HttpMessage m1 = sendGet(url);
        if (!m1.isOk())
            return false;

        if ("TechnicalBreak".equals(XmlReader.getStatus(m1.getContent())))
            return false;

        return true;
    }

    public static HttpMessage canDownloadDataFromServer() {

        if (!HttpDownload.isInternetConnected())
            return new HttpMessage(false, "No internet connection", "");

        if (!HttpDownload.isGemiusReachable()) {

            String errorMessage = null;
            if (Utils.isGemiusServerMaintenanceHour())
                errorMessage = "gDE system maintance. Try again later";
            else
                errorMessage = "Urgent/sustained gDE system maintance. Please contact technical support";

            return new HttpMessage(false, errorMessage, "");
        }
        return new HttpMessage(true, "", "");
    }

    
    public List<CampaignHeader> getCampaignHeaders(String xmlHeaders) {

        
            return xmlReader.getAllHeaders();
        

    }
    
}
