## Thymeleaf
* CDN 사용시 헤더는 버전별 속성이 다르므로 주의한다.
```html
    <!-- 아래 integrity는 버전별로 설정해야 한다. -->
    <link rel="stylesheet" 
          href="/css/bootstrap.min.css"
          integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" 
          crossorigin="anonymous">
```
* `th:field=*{ }` 는 id와 name 속성을 속성명을 한번에 정의해준다.     
  즉, 속성명을 엔티티의 필드와 동일하게 맞추면 여러모로 편해진다.