package br.edu.ifsc.co2monitor.dataupload.domain.bucket;

import br.edu.ifsc.co2monitor.dataupload.domain.model.NodeMessage;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

import java.time.Instant;

public class InfluxDbMessageBucketImpl implements InfluxDbMessageBucket {

    private InfluxDBClient influxDBClient;
    private WriteApiBlocking writeApi;

    public InfluxDbMessageBucketImpl(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
        this.writeApi = influxDBClient.getWriteApiBlocking();
    }

    @Override
    public void put(NodeMessage message) {
        var point = Point
                .measurement("co2_level")
                .addTag("sensor", message.getSensor())
                .addField("value", message.getValue())
                .time(Instant.now(), WritePrecision.US);

        writeApi.writePoint(point);
    }

}
