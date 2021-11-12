package br.edu.ifsc.co2monitor.dataupload.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {

    private String value;

    public static Measurement of(String value) {
        return new Measurement(value);
    }

}
