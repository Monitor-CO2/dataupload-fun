package br.edu.ifsc.co2monitor.dataupload.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeMessage {

    private String box;
    private String accessToken;
    private String sensor;
    private String value;

}
