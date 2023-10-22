package org.postemp.http.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String uri;
    private String raw;
    private Map<String,String> params;

    public String getUri() {
        return this.  uri;
    }

    public Request(String rawRequest) {
        this.raw = rawRequest;
        this.uri = parseUri(raw);
        this.params = parseGetRequestParams(raw);
    }

    private  String parseUri(String request) {
        int startIndex = request.indexOf(' ');
        int endIndex = request.indexOf(' ', startIndex + 1);
        String uri = request.substring(startIndex + 1, endIndex);
        if (!uri.contains("?")) {
            return uri;
        }
        endIndex = uri.indexOf('?');
        return uri.substring(0, endIndex);
    }

    private  Map<String, String> parseGetRequestParams(String request) {
        int startIndex = request.indexOf(' ');
        int endIndex = request.indexOf(' ', startIndex + 1);
        String uri = request.substring(startIndex + 1, endIndex);
        if (!uri.contains("?")) {
            return Collections.emptyMap();
        }

        String[] strParamsKeyValue = uri.substring(uri.indexOf('?') + 1).split("&");
        Map<String, String> out = new HashMap<>();
        for (String p : strParamsKeyValue) {
            String[] keyValue = p.split("=");
            out.put(keyValue[0], keyValue[1]);
        }
        return out;
    }

    public String show() {
        return "uri: "+ uri +
        "  params: " + params;
    }

    public String getParam(String key) {
        return params.get(key);
    }
}
