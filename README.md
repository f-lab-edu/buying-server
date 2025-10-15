# 🛒 Buying Shop - 중고거래 서비스 (개인 프로젝트)

> 당근마켓 클린코드 버전으로 구현하는 중고거래 플랫폼  
> EC2 + Jenkins를 이용한 CI/CD 구축 예정  
> 로컬 개발환경은 Docker 기반으로 구성

> API 명세서 :
https://docs.google.com/spreadsheets/d/13ObicfiP9Z7a3lEyLpHXEgHhtcZqFQFfjllnbquRQIs/edit?usp=sharing


> Erd :
https://www.erdcloud.com/d/L4sAXicesp2GP4pKe
<img width="1511" height="847" alt="image" src="https://github.com/user-attachments/assets/4f824a34-bec7-45b5-9d06-20d9be7d91e7" />


---

## 🚀 프로젝트 개요

**Buying Shop**은 사용자가 직접 상품을 등록하고,  
다른 사용자가 상품을 구매할 수 있는 중고거래 서비스입니다.

| 주요 기능 | 설명 |
|------------|------|
| 🔸 게시글 등록 | 사용자가 제목, 내용, 이미지, 가격을 등록 |
| 🔸 상품 조회 | 메인 피드에서 상품 목록 조회 (무한 스크롤) |
| 🔸 결제 기능 | 판매자가 지정한 금액으로 Toss Pay 결제 |
| 🔸 채팅 기능 | 구매자 ↔ 판매자 1:1 실시간 채팅, 오픈 채팅방 (kafka Pub/Sub + MongoDB 저장) |
| 🔸 로그인 | Kakao / Google OAuth2 로그인 |
| 🔸 캐싱 | Redis 기반 캐싱 및 세션 관리 |
| 🔸 알람 | Kafka 기반 비동기 알림 처리 (결제 완료, 채팅 메시지, 예약 알림 등) |

---

## ⚙️ 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.2|
| Build Tool | Gradle |
| Database | MySQL 8, MongoDB, flyway |
| Cache / Message | Redis, Apache Kafka |
| Infra | Docker, AWS EC2, Jenkins (CI/CD) |
| Auth | Spring Security + OAuth2 |
| Payment | Toss Payments API |
| Chat | WebSocket + Redis Pub/Sub + Kafka 메시지 스트리밍 |
| Docs | Swagger / REST Docs |
| IDE | IntelliJ IDEA |

---

## 추가 업데이트할 사항
| 주요 기능 | 설명 |
|------------|------|
| 🔸 예약 기능 등록 | 작성자가 게시물 예약 상태로 할 수 있도록 변경가능 합니다. 시간지정 예약 기능도 추가합니다. |
| 🔸 상품 조회 | 검색창을 통해 원하는 상품을 검색할 수 있도록 합니다. |
| 🔸 결제 기능 | 카카오페이 결제 기능을 추가합니다. |
| 🔸 채팅 기능 | 생각 중 |
| 🔸 로그인 | 자체 로그인 기능도 추가합니다 |

---

## 📆 개발 일정

| 주차      | 기간                | 주요 목표                                                                     |
| ------- | ----------------- | ------------------------------------------------------------------------- |
| **1주차** | 10월 13일 ~ 10월 19일 | 📦 게시물 전체 기능 완료 (등록 / 수정 / 삭제 / 조회)<br>⚙️ EC2, Jenkins, Docker 기반 배포환경 세팅 |
| **2주차** | 10월 20일 ~ 10월 26일 | 💬 실시간 채팅 (1:1 / 오픈채팅방)<br>💳 **Toss Pay 결제 시스템 구현**                      |
| **3주차** | 10월 27일 ~ 11월 3일  | 👤 사용자 기능 고도화 (프로필 수정, OAuth2 + 자체 로그인)<br>🔔 **Kafka 알림 시스템 구축**         |
| **4주차** | 11월 4일 ~ 11월 10일  | 📊 Redis 캐싱, 통계/로그 시스템 구축<br>🧩 Kafka Consumer 분리 및 알림 서비스 안정화            |
| **5주차** | 11월 11일 ~ 11월 17일 | 🚀 실서비스 배포 및 모니터링 (Prometheus / Grafana)<br>🧹 코드 리팩토링 및 테스트 자동화          |


