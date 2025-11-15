package org.example.buyingserver.common.dto;

public interface ErrorCode {
    int getStatus(); // HTTP 상태코드추가
    int getCode();
    String getMessage();
}
