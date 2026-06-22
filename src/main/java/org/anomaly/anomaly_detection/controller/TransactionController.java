package org.anomaly.anomaly_detection.controller;

import lombok.RequiredArgsConstructor;
import org.anomaly.anomaly_detection.dto.TransactionDto;
import org.anomaly.anomaly_detection.services.TransactionProcess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionProcess transactionProcess;
    @PostMapping("/ingest")
    public ResponseEntity<Void> ingest(@RequestBody TransactionDto dto) {
        transactionProcess.transactionProcess(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
