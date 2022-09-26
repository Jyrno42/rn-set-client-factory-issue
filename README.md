# react-native setOkHttpClientFactory issue example

1. Clone the repository
2. install dependencies with `yarn`
3. `yarn start`
4. In another terminal `yarn server`
5. Then use `yarn android` to launch the application

This repository demonstrates how `setOkHttpClientFactory` does nothing when it is
called after a request is made from JS side with `fetch` (most likely with xhr too
but thats not tested here).

The project is a standard react native project built from 0.70.1 typescript template.
It includes a custom native module which exposes two methods: `setValue` and `applyConfig`.

`setValue` stores the provided string as a module internal parameter and `applyConfig` reads it
and creates an `OkHttpClientFactory` which sets the originally provided value as a HTTP 
header (`FromJava`).

The included `server.js` file reads the header value and responds with its contents.

In the JS side the application shows an UI with two buttons. Both of the buttons
use `setValue` + `applyConfig` and then do a request to `server.js`. The difference between
the buttons being that `with request` button does a simple fetch to example.com beforehand.

When one launches the app and presses on the `with request` button the server responds with 
`fromjava:undefined`. They can now press the other button and will still see the same response.
This is since our custom client factory is not activated on the okhttp3
client that was initialized by the first request to example.com. 

However when one now re-launches the app and uses the other button first then the first and 
all future requests will have `manually set from JS` header value.

## Using a static okhttp client factory

This issue does not exist when one uses `setOkHttpClientFactory` from the native side early on 
(e.g. in onCreate). This can be seen by building the application in `static` factory mode:

```
STATIC_FACTORY=1 yarn android
```

In this case the response will be `CustomClientFactoryStatic` irregardless of the order of the requests.
