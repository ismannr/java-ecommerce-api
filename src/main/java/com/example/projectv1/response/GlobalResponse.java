package com.example.projectv1.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import java.text.SimpleDateFormat;
import java.util.*;

public class GlobalResponse {
    public static ResponseEntity<Object> responseHandler(String message, HttpStatus httpStatus, Object object){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String timestamp = simpleDateFormat.format(new Date());
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", httpStatus.value());
        map.put("message", message);
        map.put("timestamp", timestamp);
        map.put("data", object);

        return new ResponseEntity<Object>(map, httpStatus);
    }
}
