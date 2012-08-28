Picture choice app for McClelland lab.
======================================

Calls home to the PDP lab servers, which aren't on Github,
to verify version and to store the data taken.

Versions which are not the current version will be disabled.

***

### Here is how to port this app to your own servers:

The app calls home via POST to certain urls. You must expose a RESTful API in your servers to:
- Take the data and use it in your own server. The data is sent via a perfectly normal POST request.
- Bounce back an exp_id so that the app will know which exp_id it is.
- Bounce back a version number so that the app will know that it's the correct version. If it finds that it's not the correct version, it will shut itself down.

All of the app's calling home functionality is within the com.android.PictureChoice.Posting package. 
Block, ExpEnd, MTurkId, Session and TrialChoice are data structures. They won't need to be changed.
To change which site the app calls home to, change the urlString in PostBlockTask, PostExpEndtask, PostMTurkIdTask, PostSessionTask, and PostTrialTask.
To change which place the app verifies it version number with, change VersionTask. Note the APP_VERSION constant in the MainActivity.

### Here is how to edit up the app's pictures:
Note the numTrials, numSixes and numOthers constants in the ChoiceScreenActivity. Those indicate, respectively:
- The number of trials in a block
- The number of trials with six squares in a block
- The number of other trials (ones with 4 squares) in a block

After changing those, you must change the initCategories() method in the GlobalVar class. Each of the R.drawable.####'s are integers, which basically point to a memory location where your picture is.
So what initCategories() currently does is to fill up four categories of pictures, which are labeled appropriately, with the right pictures.
To put more pictures in a block, fill up the categories with more pictures, then change the constants in ChoiceScreenActivity.