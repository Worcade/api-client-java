# api-client-java
Client library for the Worcade public API, in Java

## Basic usage
Obtain a `Worcade` instance using the `builder` method. Log in using the `loginUserByEmail` endpoint
and your existing Worcade account. Then use the various `getApi` methods to make requests.

For code examples, see the [examples package](example/src/main/java/net/worcade/client/example)

## Runtime requirements
To use this library, a HTTP client and a JSON provider must be available at runtime.
An implementation using Jersey Client and Jackson is available in the `jersey-client` module,
but it should be possible to substitute other implementations.
To do so, implement the `WorcadeClientBuilder` interface and return a subclass of `WorcadeClient`.

To run with the Jersey client, use these dependencies:
```
compile 'net.worcade:api-client:1.0.0'
runtime 'net.worcade:api-client-jersey:1.0.0'
```

## License and contributors
* The MIT License, see [LICENSE](https://github.com/Worcade/api-client-java/raw/master/LICENSE).
* For contributors, see [AUTHORS](https://github.com/Worcade/api-client-java/raw/master/AUTHORS).
