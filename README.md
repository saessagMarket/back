# back
새싹마켓 백엔드 레포지토리입니다.

## ✨ Branch Naming Convention

| 머릿말  | 설명                              |
| ------- | ----------------------------------|
| main    | 서비스 브랜치                      |
| develop | 배포 전 작업 기준                  |
| feat | 기능 단위 구현                        |
| hotfix  | 서비스 중 긴급 수정 건에 대한 처리  |

ex) feat/#1-login

## ✨ Commit Convention
**유다시티 스타일 적용**
| 머릿말           | 설명                                                                      |
| ---------------- | ------------------------------------------------------------------------- |
| feat             | 새로운 기능 추가                                                           |
| fix              | 버그 수정                                                                  |
| docs             | 문서 수정                                                                  |
| style            | EOL, 세미콜론 등 코드에 대한 변경이 없는 경우(프로덕션 코드 변경 X)           |
| refactor         | 프로덕션 코드 리팩토링                                                      |
| comment          | 필요한 주석 추가 및 변경                                                    |
| test             | 테스트 추가, 테스트 리팩토링(프로덕션 코드 변경 X)                           |
| setting          | 패키지 설치, 개발 설정                                                      |
| chore            | 빌드, 릴리즈, 패키지 매니저 설정 등(프로덕션 코드 변경 X)                     |

ex) fix : dto 수정

## ✨ Message Structure
type: subject  
body           
footer(선택사항) 이슈 트래커의 ID를 참조할 때 사용 #이슈번호

ex)
feat: 로그인 기능 추가

- 사용자 인증 및 세션 관리 기능 구현
- 아이디/비밀번호 입력 폼 구현
- 로그인 상태 세션 유지 처리

Resolves: #2

