package org.example.buyingserver.post.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PostCannotReserveDeletedException extends BusinessException {
    public PostCannotReserveDeletedException() {
        super(PostErrorCode.POST_CANNOT_RESERVE_DELETED);
    }
}