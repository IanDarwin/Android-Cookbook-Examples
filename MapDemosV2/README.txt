This is the Google API Maps V2 Demo.
See https://androidcookbook.com/Recipe.seam?recipeId=4285

OMG NOOOOO THE ANDRAOIDMANIFEST IS COMPLETELY MISSING, WTF?

After you obtain your Google Android Maps V2 API Key, you MUST
* copy AndroidManifest.xml-SAMPLE to AndroidManifest.xml
* edit AndroidManifest.xml to put your API key in the <meta-data> element.

I set it up this way for the benefit of people that don't read README files.

Unfortunately, you also have to edit the project.properties
to refer to where you have the Android SDK installed.
If you get ClassNotFoundException on com.google.*, try this step again!
Note that in this case there will also be an error in the Console window about 
being unable to find the google-play-services_lib.apk, that is your clue...
