This is the Google API Maps V2 Demo.
See https://androidcookbook.com/Recipe.seam?recipeId=4285

After you obtain your Google Android Maps V2 API Key, you MUST
cp AndroidManifest.xml-SAMPLE AndroidManifest.xml
edit AndroidManifest.xml to put your API key in the <meta-data> element.

Unfortunately, you also have to edit the project.properties
to refer to where you have the Android SDK installed.
If you get ClassNotFoundException on com.google.*, try this step again!
Note there will also be an error in the Console window about being
unable to find the google-play-services_lib.apk, that is your clue...
