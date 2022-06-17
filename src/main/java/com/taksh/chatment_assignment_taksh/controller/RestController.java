package com.taksh.chatment_assignment_taksh.controller;

import com.taksh.chatment_assignment_taksh.model.Logs;
import com.taksh.chatment_assignment_taksh.repository.LogRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    LogRepository repository;
    @Value("${service.url}")
    private String api_url;


    @GetMapping("/fact")
    public String getFacts(HttpServletRequest request) throws JSONException {
        String animal=request.getParameter("animal");
        RestTemplate template=new RestTemplate();
        String response=template.getForObject(api_url+animal,String.class);
        JSONObject object=new JSONObject(response);
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(object.getString("data"));
        String data="";
        while (m.find()) {
            data=m.group(1);
        }
        String finalData = data;
        Runnable runnable= () -> {
            Logs log=new Logs();
            log.setRequest_ip(request.getRemoteAddr());
            String time=Calendar.getInstance().getTime().toString();
            log.setDate_time(time);
            log.setSent_response(finalData);
            repository.save(log);
        };
        runnable.run();
        return data;
    }

    @GetMapping("/logs")
    public String getLogs(HttpServletRequest request) throws JSONException {
        if (request.getParameter("auth")!=null && !request.getParameter("auth").isEmpty() && request.getParameter("auth").equals("taksh"))
        {
            JSONArray array=new JSONArray();
            List<Logs> logs=repository.findAll();
            for (Logs log:logs) {
                JSONObject object=new JSONObject();
                object.put("id",log.getLog_id());
                object.put("request time",log.getDate_time());
                object.put("client ip",log.getRequest_ip());
                object.put("sent response",log.getSent_response());
                array.put(object);
            }
            return array.toString();
        }
        else
        {
            return "access-denied";
        }
    }
}
