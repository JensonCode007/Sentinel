package org.anomaly.anomaly_detection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransactionDto {
    @JsonProperty("Time")
    private Double time;

    @JsonProperty("V1") private Double v1;
    @JsonProperty("V2") private Double v2;
    @JsonProperty("V3") private Double v3;
    @JsonProperty("V4") private Double v4;
    @JsonProperty("V5") private Double v5;
    @JsonProperty("V6") private Double v6;
    @JsonProperty("V7") private Double v7;
    @JsonProperty("V8") private Double v8;
    @JsonProperty("V9") private Double v9;
    @JsonProperty("V10") private Double v10;
    @JsonProperty("V11") private Double v11;
    @JsonProperty("V12") private Double v12;
    @JsonProperty("V13") private Double v13;
    @JsonProperty("V14") private Double v14;
    @JsonProperty("V15") private Double v15;
    @JsonProperty("V16") private Double v16;
    @JsonProperty("V17") private Double v17;
    @JsonProperty("V18") private Double v18;
    @JsonProperty("V19") private Double v19;
    @JsonProperty("V20") private Double v20;
    @JsonProperty("V21") private Double v21;
    @JsonProperty("V22") private Double v22;
    @JsonProperty("V23") private Double v23;
    @JsonProperty("V24") private Double v24;
    @JsonProperty("V25") private Double v25;
    @JsonProperty("V26") private Double v26;
    @JsonProperty("V27") private Double v27;
    @JsonProperty("V28") private Double v28;

    @JsonProperty("Amount")
    private Double amount;

    @JsonProperty("Class")
    private Integer classLabel;
}
