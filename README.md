# AstaZero ATOS drone demo app

## Introduction

This is the AstaZero ATOS drone demo app.

## Requirements

 - Android Studio 2.0+
 - Android System 4.1+
 - DJI Android SDK 4.16.1

## Building for android
These build instructions have been tested and are confirmed working on ubuntu 20.04.
### Dependencies
Install swig: 
```
sudo apt install swig
```
Download Android Studio by visiting https://developer.android.com/studio/install and downloading the zip file. 
Unzip the file and execute `./studio.sh` located in /android-studio/bin to start the IDE.

Inside the IDE, go to Tools -> SDK Manager -> SDK Tools -> Show package details -> NDK 25 and check it, this downloads NDK 25 to the location ~/Android/Sdk/ndk/##.#.######.

### Build steps

Start by building boost for Android:
#### Cross compiling boost
```
git clone git@github.com:moritz-wundke/Boost-for-Android.git
cd Boost-for-Android
```
Modify the file `build-android.sh` to only build for arm64-v8a, only with the system component of the boost library and as a dynamically linked library. This is done by finding and modifying the variables accordingly:
```
LIBRARIES=--with-system
ARCHLIST=arm64-v8a
```
And at the ./b2 command, modify `link=static` to:
```
link=shared
```

Execute the script to build Boost::System for Android (arm64-v8a):
```
./build-android.sh "/path/to/ndk-25/"
```

The resulting files end up in: 
```
Boost-for-Android/build/out/arm64-v8a/include
Boost-for-Android/build/out/arm64-v8a/lib
```

We now build the app, linking it to boost and util libraries and deploy it using AndroidStudio
#### Build and deploy with AndroidStudio
Clone the repository:
``` 
git clone git@github.com:RI-SE/Android-GSDemo-GoogleMap.git
```

Clone the util repo containing ISO22133 object implementation:
```
git clone git@github.com:RI-SE/util.git
```

Change directory to the util repo and initialize and update git submodules:
```
cd util && git submodule init && git submodule update --recursive
```

Open the project Android-GSDemo-GoogleMap in AndroidStudio.
In the build.gradle file, modify my_boost_dir such that it matches the `lib` and `include` locations of the boost dirs built following the steps of the previous subsection

The app can now be built and deployed to an Android phone. 


## Common problems
### HeabTimeout
ATOS and the ISOobject have set HEAB timeouts, depending on the network connection this might cause either to timeout and cause abort. Raising this value in both can be used as a workaround if this is the the reason for aborting. 

### Invalid MONR
The iso MONR message has separate boolean values specifying if the position is valid or not. These need to be set for the MONR message to be correctly encoded.

