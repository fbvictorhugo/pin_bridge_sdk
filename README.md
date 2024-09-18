[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg)](CODE_OF_CONDUCT.md) [![Library Version](https://img.shields.io/badge/LibraryVersion-v0.0.4-brightgreen)](https://github.com/fbvictorhugo/pin_bridge_sdk/packages/2257454)

# Pinterest Bridge SDK

**PinBridge SDK** is an Android library designed to simplify and optimize interaction with the
Pinterest REST API. It acts as a facilitator between your app and the wide range of functionality
offered by Pinterest, making it easier for developers to integrate and manage features like pins,
boards, and visual content directly in their apps.

## Table of Contents

- [Disclaimer](#disclaimer)
- [Pinterest  API Compatibility](#pinterest--api-compatibility)
- [Installation](#installation)
- [Usage](#usage)
- [License](#license)

---

# Disclaimer

**PinBridge SDK** is an open source Android library, independently created to facilitate integration
with the Pinterest REST API. We have no official affiliation
with [Pinterest](https://www.pinterest.com/).

# Pinterest  API Compatibility

[Pinterest REST API v5](https://developers.pinterest.com/docs/api/v5/introduction).

```
│
└── user_account
│   └ [GET] Get user account
│
└── boards
│   └ [GET] List boards
│   └ [GET] List Pins on Boards
...
│
└── oauth
```

# Installation

> comming

# Usage

Once you have already configured your App
on [Pinterest Developer Platform](https://developers.pinterest.com/apps/), follow:

### 1. Configure Instance

In `onCreate` configure the `PinBridge` instance:

``` kotlin
PinBridge.configureInstance(this, CLIENT_ID, REDIRECT_URI)
```

### 2. Add the scopes

Below the instance configuration, add only the necessary scopes.

``` kotlin
PinBridge.addScopes(
    PScope.UserAccounts.Read,
    PScope.Boards.Read,
    PScope.Pins.Read
)
```

### 3. Authenticate

Use in a button action for example. This method will take you to Pinterest's OAuth authentication
page to request access. After granting you will return to the app.

``` kotlin
PinBridge.authenticate()
```

### 4. Intercept authorization

For example, in `onCreate` or `ActivityResult` you must intercept the intent to retrieve the OAuth
request validation code.

``` kotlin
PinBridge.interceptAuthorizationCode(intent)
```

This way, PinBridge will now retrieve the necessary code for future requests.

### 5. Request Token

When the token is successfully received, PinBridge automatically manages it, saving it,
but if necessary you can recover it.

``` kotlin
PinBridge.requestAccessToken(CLIENT_SECRET, object : PCallback<PAccessToken> {

    override fun onSuccessful(response: PResponse<PAccessToken>) {
        toast("onSuccessful Token")
    }

    override fun onUnsuccessful(response: PResponse<PAccessToken>) {
        toast("onUnsuccessful Token ${response.getCode()} - ${response.getMessage()}")
    }
})
```

### 6. Use !

Access to available APIs is in calls from `PinBridge.Requests`.

``` kotlin
PinBridge.Requests.UserAccount.getUserAccount(object : PCallback<PUserAccount> {

    override fun onSuccessful(response: PResponse<PUserAccount>) {
        toast("Hello ${response.getObject()?.username} ! ")
    }

    override fun onUnsuccessful(response: PResponse<PUserAccount>) {
        toast("onUnsuccessful Request ${response.getCode()} - ${response.getMessage()}")
    }
})
```

## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](LICENSE) file for details.

### Third-Party Licenses

This project uses the following third-party libraries:

- [Retrofit](https://square.github.io/retrofit/) > Apache License, Version 2.0

---

## Purpose

- [x] **Study**: This project was created for learning and practice purposes.
- [ ] **Course**: This project is part of a specific course.
- [x] **Real Project**: This project is a real application to be published.
