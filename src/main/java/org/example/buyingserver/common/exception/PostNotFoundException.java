package org.example.buyingserver.common.exception;

import org.example.buyingserver.common.dto.PostErrorCode;

public class PostNotFoundException extends BusinessException {
    public PostNotFoundException(PostErrorCode postErrorCode) {
        super(postErrorCode);
    }
}