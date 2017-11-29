# instgramApiTestApp

Just testing the API, the app shows the recent, and allow you to like/unlike, login/log out.

The app loads 2 images at a time, if user scroll to the bottom, 2 more will be loaded.


○ List any assumptions, known limitations, and if any, next steps.  ○ List any external libraries used with pros/cons of using them.  

Assumptions and Known limitation: only 1 specific login is allowed.
Next step: should make the UI prettier, currently the like status is just text. User can only click on the image to update likes. Very limited info.

Libs:
'com.squareup.picasso:picasso:2.5.2'     ==== Popular lib for loading image. (cache, async)
'com.squareup.retrofit2:retrofit:2.3.0'  ==== Popular lib for making http requests like java api calls
'com.jakewharton:butterknife:8.8.1'      ==== No more findviewbyId
'com.orhanobut:logger:2.1.1'             ==== Good simple logger, also shows the thread is it running on.

![Output sample](https://github.com/jeffreyliu8/instgramApiTestApp/blob/master/screenshot.gif)
