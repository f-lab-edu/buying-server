package org.example.buyingserver.post.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PostNotReservedException extends BusinessException {
    public PostNotReservedException() {
        super(PostErrorCode.POST_NOT_RESERVED);
    }
}