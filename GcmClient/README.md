GcmClient
========

This Android App works with the Google Cloud Messaging service and the mock server
defined in [../GcmMockServer](https://github.com/AndroidCook/Android-Cookbook-Examples/tree/master/GcmMockServer). 
You really need to read the Android Cookbook recipe at
https://androidcookbook.com/Recipe.seam?recipeId=4601 to figure out all the moving pieces!

Also note that you MUST import the google-play-services\_lib project from $ANDROID\_SDK/extras/google/google\_play\_services/libproject/ in order for the client to compile
(and do check "Copy projects into workspace" to cope with updates!).
See the Android Cookbook article for how to refer to it properly.
