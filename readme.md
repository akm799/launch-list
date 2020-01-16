## Falcon 9 Launch List

Simple Android application that displays Falcon 9 Launch Data using the [Space X REST API](https://github.com/r-spacex/SpaceX-API).

### Import

The project `LaunchListRx` is an Android Studio one. To build it you must import it into Android Studio.

### Description

The application consists of a single screen. The screen displays the Falcon 9 launches in a list. For each launch in the list the following parts are visible:

- Mission patch image
- Mission name
- Launch date
- Launch outcome

### Design

The design adopted follows the MVP design pattern. It is too complex for the simple functionality required, but it was chosen to illustrate
the design patterns and best practices followed in larger scale and more demanding applications.
  
### Configuration

There are 3 external configuration parameters contained in the top-level `build.gradle` file:

1. `launchType`: The type of launches that will be displayed (in our case “falcon9” launches)
2. `cacheExpirySecs`: The time in seconds that the launch data are cached locally (to reduce the number of remote calls)
3. `apiBaseUrl`: The base URL of the [Space X REST API](https://github.com/r-spacex/SpaceX-API) endpoints

Please note that the `cacheExpirySecs` was set to this low value for testing purposes.

### Issues

1. The required date parsing can be made more efficient (at least for API 26 or higher)
2. The layout of the items in the list can be improved to better display the mission patch with different screen sizes
3. Although the application can display its data after configuration changes, its layout has not been optimised for landscape mode
4. A cache cleaning functionality (including the cached images) should be added
5. A more appropriate fallback mission patch image should be chosen (at the moment some default Android icon is used)

More manual and unit testing can always be added as well as automation tests.


### Libraries

The [Retrofit2](https://square.github.io/retrofit/) library was used to call the [Space X REST API](https://github.com/r-spacex/SpaceX-API) endpoints whereas [RxJava2](https://github.com/ReactiveX/RxJava) was employed to orchestrate the remote server calls and local cache calls. The Android [Room](https://developer.android.com/training/data-storage/room/index.html) persistence library was utilised for data caching. The 
[Glide](https://github.com/bumptech/glide) library helped with image display. For dependency injection, the [Koin](https://insert-koin.io/) framework was used. To aid unit testing, the [Mockito](https://site.mockito.org/) mocking framework and [Hamcrest](http://hamcrest.org/JavaHamcrest/) matcher library were included.

### Contact

Please e-mail <a.k.mavroidis@gmail.com> for any questions or requests.
