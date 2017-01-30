# Fused Location API Wrapper
FusedLocationAPIWrapper is an Android library that provides simple using of Google Location API. It extends of Android Service that is why it can be used as independent background process. 
Library has very simple interface that based on static methods.

Also here you can find a sample project that can be used for two goals:
- Help two understand how to use FusedLocationAPIWrapper
- Test your GPS system via Google Location API

How to use it
---------------

### Activity

```java

public class MainActivity extends LocationTrackingActivity {

    @Override
    protected void onLastKnownLocation(Location location) {
       //work with location data
    }

    @Override
    protected void onUpdateLocationEvent(Location location) {
       //work with location data
    }

    @Override
    protected void onUpdatingStarted(int interval, int fastestInterval, String priority) {
       //change UI accoding to starting updating
    }
}
```
### Start/Stop

```java
LocationApiWrapperService.startSelf(this);
LocationApiWrapperService.stopSelf(this);
```
### Connect/disconnect Location API
```java
LocationApiWrapperService.connectApi(this);
LocationApiWrapperService.disconnectApi(this);
```
### Get last known location

```java
LocationApiWrapperService.callLastLocation(this);
```
### Start/Stop location updates

```java
LocationApiWrapperService.startLocationUpdates(this);
LocationApiWrapperService.stopLocationUpdates(this);
```
### Change update settings

```java
LocationApiWrapperService.changeUpdateInterval(this, interval);
LocationApiWrapperService.changeFastestUpdateInterval(this, fastestInterval);
LocationApiWrapperService.changeUpdatePriority(this, priority);

LocationApiWrapperService.changeUpdateSettings(this, interval, fastestInterval, priority);
```

Sample App
---------------

License
-------
Copyright (c) 2015 Grossum. All rights reserved.
