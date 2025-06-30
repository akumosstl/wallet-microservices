package io.github.akumosstl.transaction.controller;


import io.github.akumosstl.commons.dto.ApiResponseDto;
import io.github.akumosstl.transaction.model.TransactionLogs;
import io.github.akumosstl.transaction.service.TransactionLogsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/logs")
public class TransactionController {

    @Autowired
    TransactionLogsService logsService;

    @GetMapping
    @ApiOperation(value = "Transaction Logs",notes = "Get all Transaction Logs")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not Authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not Found!"),
            @ApiResponse(code = 201, message = "Created", response = ApiResponseDto.class) })
    public ResponseEntity<List<TransactionLogs>> getAll() {
        return ResponseEntity.ok().body(logsService.findAll());
    }

}
