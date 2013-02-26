This repo contains a number of source code examples from the recipes published in the O'Reilly Android Cookbook (see http://androidcookbook.com/). Not all recipes have code; this repo features code that was either linked by the contributor, or in a few cases, re-constructed as a New Project by the editor, for your convenience. 

Note that not all programs used in the book appear here; some are only program fragments taken from other programs that can't be included. And of course some projects used as examples in the book are routinely hosted elsewhere.

Each Directory comprises its own Eclipse project, and has a name that is cited at the end of the corresponding Recipe, under the "Source Code Download" section. If you open the repo in Eclipse with eGit, be sure to check the
    "create all projects"
checkbox. If you want to keep these 40 or so mini-projects separate from your "regular" work, you may want to use the Git repo as an Eclipse workspace (use File->Switch Workspace->Other->Browse). The .metadata directory is already in .gitignore.

You can download this as a Zip file, but then you won't get updates, and it will be hard to collaborate with the Editor and other contributors. If you're new to Git, see the page on GitHub about how to collaborate. You can either send diff/patch files or, if you forked your own copy, send a pull request.  Remember that GitHub is a free service for public projects!

Many of the projects will give you some noise when you first open them due to bin and/or gen being missing. The Eclipse plug-in will usually create them but not notify that it's done so, meaning you just have to do Refresh (F5), failing that, close and re-open the project. There is a workaround but I haven't had time to apply it to all projects yet (creating empty directories).

There is neither a Wiki or a Tracker for this project, since bugs and suggestions about the code should be made on the Android Cookbook web site (http://androidcookbook.com/). Bugs or suggesions on the published edition of the book should go to http://shop.oreilly.com/product/0636920010241.do; you can also purchase the book there (which helps fund my continued work on this code base).

N.B. There have been a few projects added since the very first edition, and
a few renamed, so if you have the printed edition prior to December, 2012 you 
should rummage around here before re-typing anythig that looks like a full example,
and you will need to make the following adjustments:
	EmailAndroid was renamed to EmailTextView, and EmailWithAttachments is new.
