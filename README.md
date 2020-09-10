# 제품 리뷰 앱을 위한 API 서버 - 네이버 크롤링

![blog](https://postfiles.pstatic.net/MjAyMDA4MjFfMTg5/MDAxNTk3OTg3NjQwMDg5.o3eOgyjWjM1qIALWjeBugxt8LpdmUwX2zu0iQPubNiog.UbDEE9QcmX5AWFjZltqc_Ow4XOGiI9KdymsgSfITHRog.PNG.getinthere/Screenshot_46.png?type=w773)

![blog](https://postfiles.pstatic.net/MjAyMDA5MDdfMTcz/MDAxNTk5NDU4OTA5OTEz.7YOgh4Pj0UPA6osBhzEqJEUPquHNN16E6F4wre1ok60g.3flY4SVfJ2SjLnyD2JFohdI0vMYbxTPtz0P9DdXuqJUg.PNG.getinthere/Screenshot_6.png?type=w773)

![blog](https://postfiles.pstatic.net/MjAyMDA5MDdfMzMg/MDAxNTk5NDU4OTA5OTI0.fy-JsGvtgUuUnqS-m-t-zC2mtY9X0ik4dqZoXQIw5mkg.x8zx2jlvwpXAlcIyalIIiweeTdjSz5z6GQUi8athpL4g.PNG.getinthere/Screenshot_7.png?type=w773)

![blog](https://postfiles.pstatic.net/MjAyMDA5MDdfMjg4/MDAxNTk5NDU4OTA5OTgz.cqR6dxp0tdaRB1n-4Hz18SrBAMwzJS2-WROPJJjKvmUg.0b2Sr9t4NJrVQB1bRbiUGIiadPvKY1e2A13A_gTVVVwg.PNG.getinthere/Screenshot_8.png?type=w773)


## 의존성

- JSoup
- Mustache
- MySQL
- Jpa
- Web
- Devtools
- Lombok

## 자식 데이터 삽입시 제약조건 위배 문제 해결법

```java
// 키워드 삭제시 부모 데이터 함께 삭제하려면 연관관계를 걸고 orphanRemoval를 건다.
@OneToMany(mappedBy = "keyword", orphanRemoval = true)
private List<Product> products;
```