# docker-lab

Docker, Compose, Dev Container를 배우려고 만든 실습 프로젝트입니다. 최소한의 Spring Boot + Postgres 앱 하나를 컨테이너로 띄웁니다. 학습용 자료이고, 서비스가 아닙니다.

- 언어와 도구: Java 25, Spring Boot 4.1.1, Gradle 9.8.1(wrapper), Postgres 18
- 앱: `GET /` 엔드포인트 하나. DB의 `memo` 테이블 첫 행을 읽어 돌려줍니다.

## 학습 기록

이 프로젝트는 [dev-learning-log](https://github.com/moonbinchoi/dev-learning-log)의 [docker-devcontainer 트랙](https://github.com/moonbinchoi/dev-learning-log/tree/main/lecture/docker-devcontainer) 실습 프로젝트입니다. 0~6회차의 레슨과 학습 기록이 회차별로 남아 있습니다.

## 구성

| 파일 | 내용 |
|---|---|
| `Dockerfile` | `eclipse-temurin:25-jre` 이미지에 `build/libs/docker-lab-0.0.1-SNAPSHOT.jar`를 복사해 실행합니다. |
| `compose.yaml` | `db`: `postgres:18`, 볼륨 `pgdata`, healthcheck 포함. `app`: `Dockerfile`로 빌드, 호스트 8081 → 컨테이너 8080. `dev`: Dev Container용 `devcontainers/java:25`, `SERVER_PORT`는 8081. |
| `.devcontainer/devcontainer.json` | `compose.yaml`의 `dev` 서비스에 접속합니다. 8081 포트를 포워딩합니다. |
| `src/main/java/com/study/dockerlab/MemoController.java` | `GET /`가 `"v3 "` 뒤에 `memo` 테이블 첫 행의 `t` 컬럼을 붙여 돌려줍니다. |

## 실행 방법

아래 명령은 실행 결과를 다시 확인한 것이 아니라, 설정 파일(`compose.yaml`, `application.properties`, `Dockerfile`)을 읽고 적었습니다.

### 1. Dev Container로 실행

1. VS Code에 Dev Containers 확장을 설치하고 이 폴더를 엽니다.
2. "Reopen in Container"를 실행합니다. `dev`와 `db` 서비스가 뜹니다.
3. 컨테이너 안 터미널에서 `./gradlew bootRun`을 실행합니다. 앱은 `SERVER_PORT=8081`로 뜹니다.

### 2. Compose로 실행

```
./gradlew bootJar
docker compose up --build
```

`Dockerfile`이 빌드된 jar를 복사하므로, `bootJar`를 먼저 실행해야 합니다. 앱은 `http://localhost:8081/`에서 응답합니다.

### 3. memo 테이블 만들기 (처음 한 번)

스키마 파일이 없어서, 새 볼륨에서는 테이블을 직접 만들어야 합니다. 만들지 않으면 `GET /`가 500을 돌려줍니다. DB가 뜬 상태에서 실행합니다.

```
docker compose exec db psql -U postgres -d docker_lab -c "CREATE TABLE memo(t text);"
docker compose exec db psql -U postgres -d docker_lab -c "INSERT INTO memo(t) VALUES ('hello');"
```

### 4. 확인

```
curl http://localhost:8081/
```

`v3 `뒤에 `memo` 테이블 첫 행의 값이 붙어 나옵니다. 위 예시대로 넣었다면 `v3 hello`입니다.

## 알아둘 점

- DB 비밀번호 `pw`는 로컬 실습용 기본값입니다. 실제 서비스에 쓰는 값이 아닙니다.
- `app`과 `dev`는 둘 다 호스트 8081 포트를 씁니다. 둘 중 하나만 실행합니다.

## 이 저장소에 없는 것

`gradle/libs.versions.toml` 맨 위 주석에 `courses/`와 `projects/shop`이 나옵니다. 둘 다 이 저장소에는 없는, 작성자의 로컬 학습 작업 공간의 폴더입니다.

- `courses/`: 유료 강의 자료입니다. 저작권 때문에 공개하지 않습니다.
- `projects/shop`: 별도 저장소 [spring-shop](https://github.com/moonbinchoi/spring-shop)에 공개되어 있습니다.
