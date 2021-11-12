package br.edu.ifsc.co2monitor.dataupload.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpStatus {

    private int code;

    public static HttpStatus of(int code) {
        return new HttpStatus(code);
    }

    public boolean is4xxClientError() {
        return code >= 400 && code < 500;
    }

    public boolean is5xxServerError() {
        return code >= 500 && code < 600;
    }

}
