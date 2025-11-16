package org.example.buyingserver.post.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PostAlreadyReservedException extends BusinessException {
    public PostAlreadyReservedException() {
        super(PostErrorCode.POST_ALREADY_RESERVED);
    }
}