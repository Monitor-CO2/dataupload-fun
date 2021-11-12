package br.edu.ifsc.co2monitor.dataupload.domain.bucket;

import br.edu.ifsc.co2monitor.dataupload.domain.model.HttpStatus;
import br.edu.ifsc.co2monitor.dataupload.domain.model.Measurement;
import br.edu.ifsc.co2monitor.dataupload.domain.model.NodeMessage;
import br.edu.ifsc.co2monitor.dataupload.domain.model.SaveMeasurementException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static java.lang.String.format;

@AllArgsConstructor
public class OsemMessageBucketImpl implements OsemMessageBucket {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Override
    @SneakyThrows
    public void put(NodeMessage message) {
        var url = format("https://api.opensensemap.org/boxes/%s/%s",
                message.getBox(), message.getSensor());

        var body = objectMapper.writeValueAsString(Measurement.of(message.getValue()));

        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Authorization", message.getAccessToken())
                .build();

        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

        var status = HttpStatus.of(response.statusCode());

        if (status.is4xxClientError() || status.is5xxServerError()) {
            throw new SaveMeasurementException(format(
                    "The openSenseMap API returned status code: %s\n%s",
                    status.getCode(), response.body()));
        }
    }

}
