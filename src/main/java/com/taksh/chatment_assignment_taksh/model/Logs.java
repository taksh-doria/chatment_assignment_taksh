package com.taksh.chatment_assignment_taksh.model;

import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="API_LOGS")
public class Logs {
    @Id @GeneratedValue
    int log_id;
    String request_ip;
    String date_time;
    @Column(length=10000)
    String sent_response;

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getRequest_ip() {
        return request_ip;
    }

    public void setRequest_ip(String request_ip) {
        this.request_ip = request_ip;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getSent_response() {
        return sent_response;
    }

    public void setSent_response(String sent_response) {
        this.sent_response = sent_response;
    }
}
