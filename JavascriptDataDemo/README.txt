This is a simple project that just shows getting an ARRAY of data from Java to JavaScript and vice versa.

It's built out a bit more than the version shown in the Android Cookbook, in that it has two activities,
the Main one now just has a "show web view" button; control returns here when the JavaScript calls
the finish() method. Finish() and all the data related stuff moves to a new "Model" class, which is
installed as the Android Application instance to make it a Singleton (e.g., for design reasons it
should NOT be part of the Activity, which is a View/Controller-layer component.