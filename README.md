# http-patch-mockwebserver

Testing `HTTP PATCH` using [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver).

Since [the standard JDK HTTP library does not support HTTP PATCH](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html#patchForObject-java.lang.String-java.lang.Object-java.lang.Class-java.util.Map-)
by default, Spring must be able to locate a `ClientHttpRequestFactory` that does support `HTTP PATCH`.

Spring autoconfigures it's `RestTemplateBuilder`, and if either `HttpComponentsClientHttpRequestFactory`
or `OkHttp3ClientHttpRequestFactory` are found, they will be used, and they do provided the necessary support
for `HTTP PATCH`.

Since `MockWebServer` has a dependency on `OkHttp`, the tests runs fine since `OkHttp` puts
the `OkHttp3ClientHttpRequestFactory` on the test classpath.

## Usage

Run `./gradlew clean build` in order to execute the test.