package br.edu.ifsc.co2monitor.dataupload.domain.model;

public class SaveMeasurementException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SaveMeasurementException(String message) {
        super(message);
    }

}
