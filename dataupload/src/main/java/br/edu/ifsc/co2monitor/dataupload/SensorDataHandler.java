package br.edu.ifsc.co2monitor.dataupload;

import br.edu.ifsc.co2monitor.dataupload.domain.bucket.InfluxDbMessageBucket;
import br.edu.ifsc.co2monitor.dataupload.domain.bucket.InfluxDbMessageBucketImpl;
import br.edu.ifsc.co2monitor.dataupload.domain.bucket.OsemMessageBucket;
import br.edu.ifsc.co2monitor.dataupload.domain.bucket.OsemMessageBucketImpl;
import br.edu.ifsc.co2monitor.dataupload.domain.model.NodeMessage;
import br.edu.ifsc.co2monitor.getx.GetX;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.http.HttpClient;

public class SensorDataHandler {

    // Create beans.
    static {
        var dotenv = Dotenv.configure()
                .directory("./env")
                .filename("dev.env")
                .load();
        GetX.put(Dotenv.class, dotenv);

        var httpClient = HttpClient.newBuilder()
                .build();
        GetX.put(HttpClient.class, httpClient);

        var objectManager = new ObjectMapper();
        GetX.put(ObjectMapper.class, objectManager);

        var osemMessageBucket = new OsemMessageBucketImpl(objectManager, httpClient);
        GetX.put(OsemMessageBucket.class, osemMessageBucket);

        var influxdbUrl = dotenv.get("INFLUXDB_URL");
        var influxdbToken = dotenv.get("INFLUXDB_TOKEN");
        var influxdbOrg = dotenv.get("INFLUXDB_ORG");
        var influxdbBucket = dotenv.get("INFLUXDB_BUCKET");

        var influxdbClient = InfluxDBClientFactory.create(influxdbUrl,
                influxdbToken.toCharArray(), influxdbOrg, influxdbBucket);
        GetX.put(InfluxDBClient.class, influxdbClient);

        var influxDbMessageBucket = new InfluxDbMessageBucketImpl(influxdbClient);
        GetX.put(InfluxDbMessageBucket.class, influxDbMessageBucket);
    }

    /** Entry point. */
    public void handleRequest(NodeMessage message, Context context) {
        var osemMessageBucket = GetX.find(OsemMessageBucket.class);

        try {
            osemMessageBucket.put(message);
        } catch (Exception e) {
            System.out.println("Could not save measurement in openSenseMap!");
            e.printStackTrace();
        }

        var influxDbMessageBucket = GetX.find(InfluxDbMessageBucket.class);

        try {
            influxDbMessageBucket.put(message);
        } catch (Exception e) {
            System.out.println("Could not save measurement in InfluxDB!");
            e.printStackTrace();
        }
    }

    /*@SneakyThrows
    public static void main(String... args) {
        var message = NodeMessage.builder()
                .box("618b9f9b98f9b3001cd63de0")
                //.accessToken("*** redacted ***")
                .sensor("618b9f9b98f9b3001cd63de1")
                .value("600")
                .build();

        var sensorDataHandler = new SensorDataHandler();
        sensorDataHandler.handleRequest(message, null);

        System.out.println("Finished handling request.");
    }*/

}
