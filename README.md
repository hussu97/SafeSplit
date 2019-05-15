# SafeSplit
![Main Page](/screenshots/main.jpg)  
**Project for - CMP354 - Mobile Apps**  
**Project Duration - October-December 2018**  
**Collaborators - [Asif Towheed](https://github.com/asiftowheed)**  
>Note: This project is a mockup of [Splitwise](https://www.splitwise.com/). This project is for educational purposes only. We do not intend on making any financial gain from this
***
A productivity-based Android application to allow users to create bills and split them in any way possible with other users.

#####The application uses Google Firebase tools for different modules of the application
* [FirebaseAuth](https://firebase.google.com/products/auth/) - For registration and authentication of users
* [Firebase Realtime Database](https://firebase.google.com/products/realtime-database/) - A realtime database to store user information and information about bills, updated within the application in real-time

## Functions
* Register and login as any user (Mobile number required)
* Create a bill with anyone in your contacts list
* Split a bill in 3 ways - exact amount based, percentage based, or ratio based
* View the amounts you owe to people, and are owed from people
* Edit your profile details
* Get an **aggregate view** of bills. Example - If A owes 20 to B for Bill X, and B owes 20 to C for Bill Y, then A simply needs to pay C 20 to settle up with B, and B will automatically settle up with C.
* View dashboard of all your transactions

## Requirements
> Note: These are for modifying the project. If you want to download the apk, click [here]()
* [Java 8](https://www.java.com/en/download/)
* [Android Studio](https://developer.android.com/studio)

## Installation
1. Clone the repo by using the following command
``` bash
$ git clone https://github.com/hussu97/SafeSplit.git
$ cd SafeSplit
```
2. Open the project in your Android Studio IDE
3. Go to Build->Build Bundle(s)/APK(s) -> Build APK(s) to generate an APK
4. You can also Build an Run the project using an emulator on Android Studio

## Additional Screenshots

### Add Contact to bill page
![Add Contact](/screenshots/searchcontact.jpg)
***
### Bills page
![Bills](/screenshots/bills.jpg)
***
### Dashboard page
![Prison Information](/screenshots/dashboard.jpg)
***
### Split Bill page
![Station Information](/screenshots/split.jpg)
***
### Split Bill 2 page
![Station Information](/screenshots/splitpercent.jpg)
***
## License
[MIT](https://github.com/hussu97/SafeSplit/blob/master/LICENSE)
