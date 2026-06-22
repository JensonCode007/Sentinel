package org.anomaly.anomaly_detection.controller;

import lombok.RequiredArgsConstructor;
import org.anomaly.anomaly_detection.dto.TransactionDto;
import org.anomaly.anomaly_detection.services.TransactionProcess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TranscationController {
    private final TransactionProcess transactionProcess;
    @GetMapping("/ingest")
    public ResponseEntity<Void> ingest(@RequestBody TransactionDto dto) {
        transactionProcess.transactionProcess(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
