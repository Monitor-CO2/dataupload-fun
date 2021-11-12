package br.edu.ifsc.co2monitor.dataupload.domain.bucket;

import br.edu.ifsc.co2monitor.dataupload.domain.model.NodeMessage;

public interface OsemMessageBucket {

    void put(NodeMessage message);

}
