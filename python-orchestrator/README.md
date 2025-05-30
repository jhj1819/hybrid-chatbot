# Python Orchestrator

본 모듈은 Spring Boot 백엔드로부터 요청을 받아, RAG(Retrieval-Augmented Generation) 및 프롬프트 엔지니어링을 적용하여 OpenAI를 호출하고, Dialogflow가 더 잘 이해할 수 있는 '정돈된 질의' 텍스트를 생성하여 반환하는 역할을 합니다.

## 폴더 구조

```
/python-orchestrator
├── src/                        # 소스 코드를 담는 폴더
│   ├── __init__.py             # Python 패키지 초기화 파일
│   ├── main.py                 # FastAPI 또는 Flask 등을 사용하여 서버를 시작하는 진입점 파일
│   ├── services/               # 비즈니스 로직을 담는 서비스 계층 폴더
│   │   ├── __init__.py
│   │   ├── query_refiner.py    # 질의 정돈 핵심 로직 (RAG, Prompt Engineering, OpenAI 호출)
│   │   └── ...                 # 필요한 경우 추가 서비스 파일
│   ├── models/                 # 데이터 모델 정의 (입력/출력 데이터 구조)
│   │   ├── __init__.py
│   │   ├── request_models.py   # Spring Boot로부터 받는 요청 데이터 모델
│   │   ├── response_models.py  # Spring Boot에게 반환할 응답 데이터 모델
│   │   └── ...
│   └── utils/                  # 유틸리티 함수 모듈 (데이터 처리, API 호출 헬퍼 등)
│       ├── __init__.py
│       ├── openai_client.py    # OpenAI API 호출 헬퍼 또는 클라이언트
│       ├── rag_utils.py        # RAG 관련 유틸리티 (문서 로딩, 임베딩, 검색 등)
│       └── ...
├── requirements.txt            # 이 프로젝트가 의존하는 Python 패키지 목록
├── .env.example                # 환경 변수 설정 예시 파일 (API 키 등)
└── README.md                   # 본 파일
```

## 필요한 파일 목록

위 폴더 구조에 따라 생성해야 할 주요 파일들은 다음과 같습니다.

*   `python-orchestrator/src/__init__.py`
*   `python-orchestrator/src/main.py`
*   `python-orchestrator/src/services/__init__.py`
*   `python-orchestrator/src/services/query_refiner.py`
*   `python-orchestrator/src/models/__init__.py`
*   `python-orchestrator/src/models/request_models.py`
*   `python-orchestrator/src/models/response_models.py`
*   `python-orchestrator/src/utils/__init__.py`
*   `python-orchestrator/src/utils/openai_client.py`
*   `python-orchestrator/src/utils/rag_utils.py` (RAG 구현 시 필요)
*   `python-orchestrator/requirements.txt`
*   `python-orchestrator/.env.example`
*   `python-orchestrator/README.md` (본 파일)

## 구현 순서

파이썬 Orchestrator 모듈을 구현하는 추천 순서는 다음과 같습니다.

1.  **환경 설정 및 의존성 관리 (`requirements.txt`, `.env.example`)**
    *   프로젝트에서 사용할 Python 패키지 목록을 `requirements.txt`에 정의합니다. (예: `fastapi`, `uvicorn`, `requests`, `openai`, `langchain` 등)
    *   API 키 등 민감 정보나 환경별 설정 값을 위한 `.env.example` 파일을 생성합니다.

2.  **데이터 모델 정의 (`models/request_models.py`, `models/response_models.py`)**
    *   Spring Boot 백엔드로부터 받을 요청 데이터의 구조를 정의합니다. (원본 질의, Dialogflow 응답 정보 등) Pydantic 라이브러리 등을 활용할 수 있습니다.
    *   Spring Boot 백엔드에게 반환할 응답 데이터의 구조를 정의합니다. (정돈된 질의 문자열 등)

3.  **OpenAI API 클라이언트 구현 (`utils/openai_client.py`)**
    *   OpenAI API와의 통신을 담당하는 헬퍼 함수 또는 클래스를 구현합니다. API 호출 로직을 캡슐화하여 다른 부분에서 쉽게 재사용할 수 있도록 합니다.

4.  **RAG 유틸리티 구현 (선택 사항, `utils/rag_utils.py`)**
    *   만약 RAG 기법을 사용한다면, 관련 문서 로딩, 분할, 임베딩 생성, 벡터 DB 검색 등의 기능을 제공하는 유틸리티 함수들을 구현합니다.

5.  **질의 정돈 서비스 핵심 로직 구현 (`services/query_refiner.py`)**
    *   이 서비스 클래스에 질의 정돈을 위한 핵심 로직을 구현합니다.
    *   **RAG 적용:** (필요시) 사용자의 원본 질의 또는 Dialogflow 정보를 바탕으로 관련 문서를 검색합니다.
    *   **프롬프트 구성:** 원본 질의, 검색된 문서 내용 (RAG 결과), 초기 Dialogflow 정보 등을 조합하여 OpenAI에게 보낼 프롬프트를 동적으로 구성합니다. Dialogflow가 이해하기 쉬운 '정돈된 질의'를 생성하도록 지시하는 내용이 포함되어야 합니다.
    *   **OpenAI 호출:** `openai_client.py`를 사용하여 구성된 프롬프트를 OpenAI API로 전송하고 응답(정돈된 질의 텍스트)을 받습니다.
    *   받은 텍스트를 필요한 형태로 가공하여 반환합니다.

6.  **API 서버 진입점 구현 (`main.py`)**
    *   FastAPI 또는 Flask와 같은 웹 프레임워크를 사용하여 HTTP API 엔드포인트를 정의합니다.
    *   Spring Boot 백엔드가 호출할 `/refine-query` 또는 유사한 이름의 엔드포인트를 생성합니다.
    *   이 엔드포인트는 요청 데이터를 받아 `query_refiner.py`의 서비스 메서드를 호출합니다.
    *   서비스 메서드의 결과를 Spring Boot 백엔드가 이해할 수 있는 형태로 응답하여 반환합니다.
    *   서버 실행 로직을 추가합니다.

7.  **테스트 코드 작성**
    *   각 유틸리티 함수 및 서비스 메서드에 대한 단위 테스트를 작성합니다.
    *   API 엔드포인트에 대한 통합 테스트를 작성합니다.

8.  **문서 업데이트 및 디버깅**
    *   구현 과정에서 발견된 내용이나 변경 사항을 README에 업데이트합니다.
    *   Spring Boot 백엔드와의 연동을 테스트하며 디버깅합니다. 

- demo폴더 위치 재수정