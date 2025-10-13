# 🛒 Buying Shop - 중고거래 서비스 (개인 프로젝트)

> 당근마켓 클린코드 버전으로 구현하는 중고거래 플랫폼  
> EC2 + Jenkins를 이용한 CI/CD 구축 예정  
> 로컬 개발환경은 Docker 기반으로 구성

---

## 🚀 프로젝트 개요

**Buying Shop**은 사용자가 직접 상품을 등록하고,  
다른 사용자가 상품을 구매할 수 있는 중고거래 서비스입니다.

| 주요 기능 | 설명 |
|------------|------|
| 🔸 게시글 등록 | 사용자가 제목, 내용, 이미지, 가격을 등록 |
| 🔸 상품 조회 | 메인 피드에서 상품 목록 조회 (무한 스크롤) |
| 🔸 결제 기능 | 판매자가 지정한 금액으로 Toss Pay 결제 |
| 🔸 채팅 기능 | 구매자 ↔ 판매자 1:1 실시간 채팅 (Redis Pub/Sub) |
| 🔸 로그인 | Kakao / Google OAuth2 로그인 |
| 🔸 캐싱 | Redis 기반 캐싱 및 세션 관리 |

---

## ⚙️ 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Build Tool | Gradle |
| Database | MySQL 8 |
| Cache / Message | Redis |
| Infra | Docker, AWS EC2, Jenkins (CI/CD) |
| Auth | Spring Security + OAuth2 |
| Payment | Toss Payments API |
| Chat | WebSocket + Redis Pub/Sub |
| Docs | Swagger / REST Docs |
| IDE | IntelliJ IDEA |

---
