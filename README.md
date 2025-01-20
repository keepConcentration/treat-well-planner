# 🌏 Treat Well Planner

## 📖 프로젝트 소개
**Treat Well Planner**는 사용자가 일정 및 계획을 생성하고, 반복 규칙을 통해 주기적인 이벤트를 관리할 수 있는 Spring Boot 기반 어플리케이션입니다.  
사용자는 시작일(`startDate`)과 종료일(`endDate`)을 설정하여 일정을 관리할 수 있으며, 무기한 일정과 특정 반복 규칙(RecurrenceRule)을 활용한 다양한 계획을 지원합니다.

---

## 🚧 프로젝트 상태
현재 **Treat Well Planner는 개발 진행 중인 프로젝트**로, 정식 서비스는 아직 오픈되지 않았습니다.  
1차 목표는 주요 기능 MVP(Minimum Viable Product) 개발을 완료하고 테스트 환경에서의 안정성을 검증하는 것입니다. 정식 배포 및 오픈 일정은 추후 공지될 예정입니다.

---

## ✨ 주요 기능
- 🗓️ **일정 관리**: 일정 생성, 수정, 삭제
- 🔁 **반복 규칙 관리**:
    - 주기적인 일정(예: 매주, 매월)
    - 특정 요일, 월별 날짜, 연간 반복 규칙 설정
- 🌟 **무기한 일정** 지원: 종료일(`endDate`) 없이 지속되는 일정 생성
- 🔍 **일정 검색 및 조회**:
    - 특정 날짜가 포함된 일정 검색
    - 무기한 일정 및 반복 일정 계산

---

## 🛠️ 기술 스택
### **📦 백엔드**
- 🖥️ **Java 17**: 안정성과 성능을 높이기 위한 최신 LTS 버전 사용
- 🌿 **Spring Boot**: 강력한 REST API 및 애플리케이션 개발 프레임워크
- 🌐 **Swagger**: API 문서화 및 테스트를 위한 UI 제공
- 🌿 **Spring Data JPA**: 데이터베이스와의 간단하고 효율적인 상호작용
- ⚙️ **Gradle**: 빌드 및 의존성 관리를 위한 도구
- 📃 **Lombok**: 코드 간소화 (Getter, Setter, Builder 등 자동 생성)
- 🗄️ **H2 Database**: 테스트 환경용 인메모리 데이터베이스
    - (실제 운영에서는 다른 데이터베이스로 변경 가능)

---

## 🚀 설치 및 실행
### **1. 시스템 요구사항**
- **☕ Java 17 이상**
- **🐘 Gradle 7.x 이상**

### **2. 프로젝트 클론**
```bash  
git clone https://github.com/keepConcentration/treat-well-planner.git  
cd treat-well-planner  
```  

### **3. 애플리케이션 빌드**
Gradle을 사용해 프로젝트를 빌드합니다:

```bash  
# 의존성 설치 및 빌드  
./gradlew build  
```  

### **4. 애플리케이션 실행**
Gradle로 애플리케이션 실행:
```bash  
./gradlew bootRun  
```  

- 기본적으로 `http://localhost:8080`에서 서버가 실행됩니다.
- Swagger API 문서를 확인하려면 다음 링크를 방문하세요:  
  👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## 🎯 Swagger 설정
이 프로젝트는 Swagger를 사용하여 일관된 API 문서를 제공합니다. Swagger는 API 문서를 자동으로 생성하며, 사용자 친화적인 UI를 통해 테스트할 수 있습니다.

### Swagger 관련 의존성 (Gradle)
`build.gradle` 파일에 아래 의존성을 추가합니다.
```gradle  
dependencies {  
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'  
}  
```  

API 문서화는 기본적으로 `/swagger-ui.html` 경로에서 액세스할 수 있습니다. 설정 값은 `application.yml`에서 수정할 수 있습니다.

---

## 🧩 도메인 설계
### **1. Plan (일정)**
주요 엔터티이며, 일정의 기본 정보를 관리합니다.

| 📑 필드              | 설명                          |  
|--------------------|-----------------------------|  
| `id`              | UUID 기반 고유 식별자        |  
| `title`           | 일정 제목                   |  
| `description`     | 일정 설명                   |  
| `startDate`       | 시작 날짜                   |  
| `endDate`         | 종료 날짜 (nullable)        |  
| `recurrenceRule`  | 일정의 반복 규칙 (Optional) |  

### **2. RecurrenceRule (반복 규칙)**
일정의 반복 논리를 정의합니다.

| 📑 필드              | 설명                          |  
|--------------------|-----------------------------|  
| `id`              | UUID 기반 고유 식별자        |  
| `startDate`       | 반복 시작 날짜               |  
| `endDate`         | 반복 종료 날짜 (nullable)    |  
| `interval`        | 반복 간격 (days, weeks 등)   |  
| `daysOfWeek`      | 특정 요일 지정 (Optional)    |  
| `daysOfMonth`     | 특정 날짜 지정 (Optional)    |  

---

## 🔗 API 사용 방법
`Treat Well Planner` 애플리케이션의 주요 API를 나열합니다.

### **1. 일정 생성**
**POST** `/plans`
```json  
{
  "title": "Workout Plan",
  "description": "Daily running schedule",
  "startDate": "2023-11-01",
  "endDate": null, // 무기한일 경우 null
  "recurrenceRule": {
    "startDate": "2023-11-01",
    "interval": 1
  }
}
```  

### **2. 일정 조회**
**GET** `/plans?date=2023-11-01`

### **3. 반복 규칙 추가**
**POST** `/plans/{planId}/recurrence-rule`
```json  
{
  "startDate": "2023-11-01",
  "endDate": "2023-12-31",
  "interval": 7
}
```  

더 많은 API 정보는 👉 **[Swagger UI](http://localhost:8080/swagger-ui.html)** 에서 확인하실 수 있습니다.

---

## 🧪 테스트
### **1. 단위 테스트**
`test` 디렉토리 내에서 단위 테스트를 실행합니다:
```bash  
./gradlew test  
```  

---

## 📁 주요 파일 구조
```plaintext  
src/  
├── main/  
│   ├── java/com/treat/well/planner/  
│   │   ├── global/ (글로벌 이벤트 정의)  
│   │   ├── plan/  
│   │   │   ├── domain/            # Plan, RecurrenceRule 엔티티  
│   │   │   ├── service/           # 비즈니스 로직  
│   │   │   ├── repository/        # Spring Data JPA Repository  
│   ├── resources/  
│       ├── application.yml        # Spring Boot 설정  
│       ├── data.sql               # 샘플 데이터  
├── test/  
    ├── java/com/treat/well/planner/  
        ├── plan/                  # Plan 관련 단위 테스트  
```  

---

## 🛠️ 향후 계획
- 🗓️ **추가 API 제공**:
    - 특정 주 단위 반복, 연간 반복 일정 지원
- 🔐 **사용자 인증 및 권한 관리** 구현
- 🌍 **국제화(i18n) 및 다국어 지원**
- 🎨 **프론트엔드 통합 개발** (React 및 Vue.js 고려)

---

## 🤝 기여 방법
`Treat Well Planner`는 오픈 소스 프로젝트로 협업을 환영합니다!

1. 리포지토리를 포크합니다.
2. 새로운 브랜치를 생성합니다: `git checkout -b feature/my-feature`
3. 코드를 커밋합니다: `git commit -m 'Add new feature'`
4. 브랜치를 푸시합니다: `git push origin feature/my-feature`
5. 🛠️ 풀 리퀘스트를 제출합니다.

---

## 📜 라이선스
이 프로젝트는 [MIT 라이선스](LICENSE)를 따릅니다.

---  