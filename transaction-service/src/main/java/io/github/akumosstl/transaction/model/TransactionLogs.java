package io.github.akumosstl.transaction.model;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "transaction_logs", schema = "transaction")
public class TransactionLogs implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "type")
    private String type;

    @Column(name = "code")
    private Integer code;

    @Column(name = "object")
    private String object;

    @Column(name = "date")
    private LocalDateTime date;

}

