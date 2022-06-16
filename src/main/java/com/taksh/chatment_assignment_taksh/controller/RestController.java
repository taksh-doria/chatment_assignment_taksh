package com.taksh.chatment_assignment_taksh.controller;

import com.taksh.chatment_assignment_taksh.model.Logs;
import com.taksh.chatment_assignment_taksh.repository.LogRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    LogRepository repository;

    @GetMapping("/fact")
    public String getFacts(HttpServletRequest request) throws JSONException {
        String animal=request.getParameter("animal");
        RestTemplate template=new RestTemplate();
        String response=template.getForObject("http://api.svobodaweb.cz/animal-facts/random?animal="+animal,String.class);
        JSONObject object=new JSONObject(response);
        Runnable runnable= () -> {
            Logs log=new Logs();
            log.setRequest_ip(request.getRemoteAddr());
            String time=Calendar.getInstance().getTime().toString();
            log.setDate_time(time);
            try {
                log.setSent_response(object.getString("data"));
                repository.save(log);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        };
        runnable.run();
        return object.getString("data");
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
