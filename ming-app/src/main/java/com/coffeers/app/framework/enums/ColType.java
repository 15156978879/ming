package com.coffeers.app.framework.enums;

/**
 * Created by Administrator on 2017/9/22 0022.
 */
public enum ColType {
    CHAR,
    BOOLEAN,
    VARCHAR,
    TEXT,
    BINARY,
    TIMESTAMP,
    DATETIME,
    DATE,
    TIME,
    INT,
    FLOAT,
    PSQL_JSON,
    PSQL_ARRAY,
    MYSQL_JSON,
    AUTO;

    private ColType() {
    }
}