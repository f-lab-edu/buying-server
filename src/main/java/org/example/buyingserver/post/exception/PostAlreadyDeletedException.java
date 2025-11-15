package org.example.buyingserver.post.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PostAlreadyDeletedException extends BusinessException {
    public PostAlreadyDeletedException() {
        super(PostErrorCode.POST_ALREADY_DELETED);
    }
}