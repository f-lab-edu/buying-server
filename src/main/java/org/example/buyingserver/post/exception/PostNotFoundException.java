package org.example.buyingserver.post.exception;

import org.example.buyingserver.common.exception.BusinessException;

public class PostNotFoundException extends BusinessException {

    public PostNotFoundException() {
        super(PostErrorCode.POST_NOT_FOUND);
    }
}