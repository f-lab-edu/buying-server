package org.example.buyingserver.chat.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipantMeta {

    private Long memberId;
    private int unreadCount;
    private boolean isConnected;

    public ParticipantMeta(Long memberId) {
        this.memberId = memberId;
        this.unreadCount = 0;
        this.isConnected = false;
    }

    public void addUnread() {
        this.unreadCount++;
    }

    //일대일 채팅방용
    public void readAll() {
        this.unreadCount = 0;
    }

    public void connect() {
        this.isConnected = true;
    }

    public void disconnect() {
        this.isConnected = false;
    }
}