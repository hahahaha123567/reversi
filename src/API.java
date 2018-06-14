import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class API {

    private String server_ip = "http://10.180.58.5:5000/";
    private int group;
    private char id = 'B';

    public char getId() {
        return id;
    }

    public API (int group) {
        this.group = group;
    }

    public void init () throws Exception {
        id = sendGet("Create Session").charAt(0);
        if (id != 'W' && id != 'B') {
            throw new Exception("get id error");
        }
    }

    public boolean isOnGoing () throws Exception {
        return sendGet("Status").equals("ongoing");
    }

    public boolean isMyTurn () throws Exception {
        char currPlayer = sendGet("Turn").charAt(0);
//        System.out.println(currPlayer);
        return currPlayer == id;
    }

    public String getBoardInfo () throws Exception {
        String info = sendGet("Get Board");
        return info;
    }

    public void postMove (int x, int y) throws Exception {
        sendPost(x, y);
    }

    private String sendGet(String op) throws Exception{
        String url = server_ip;
        switch (op){
            case "Create Session": url += "create_session/" + group; break;
            case "Get Board": url += "board_string/" + group; break;
            case "Turn": url += "turn/" + group; break;
            case "Status": url += "status/" + group; break;
        }

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);

//        System.out.println("\nSending 'GET' request to URL : " + url);

//        System.out.println("Response Code : " +
//                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

//        System.out.println(result.toString()+"\n");
        return result.toString();
    }

    private void sendPost(int x, int y) throws Exception{
        String url = server_ip+"move/" + group + "/" + x + "/" + y + "/" + id;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        HttpResponse response = client.execute(post);

//        System.out.println("\nSending 'POST' request to URL : " + url);
//
//        System.out.println("Response Code : " +
//                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

//        System.out.println(result.toString()+"\n");
    }

    public static void main(String[] args) throws Exception{
        API http = new API(12);
        http.init();

        API http1 = new API(12);
        http1.init();

        http.sendGet("Get Board");

        http.sendGet("Turn");

        http.sendPost(3, 2);

        http.sendGet("Get Board");

        http.sendGet("Turn");
    }
}
