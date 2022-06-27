# wait
About the tasks:

* browse through the comics,
* see the comic details, including its description:

  I demostrated prefetch logic and in-memory chache.
   In case the user selects a previous comic the following activities happen:
   Hit the cache to check if the request for comic is in progress or is downloaded, if so leave rxJava & LiveData libs to update the ui. 
   Otherwise make request and save it to cache, at the same time try to download the last 10 comics (see: https://github.com/ardian4/wait/blob/main/app/data/src/main/java/com/please/data/repository/comics/IncrementalPreloadComicsServiceImpl.java) 
  

* get the comic explanation:

  This will require further research on how to render mediawiki format. One possible solution would be to search through some javascript libraries, which can render the format and display results in a web view. 

* send comics to others:

  For the first iteration, it will be enough to be able to share the link through other application that support the Intent type image/jpeg.
  
* favorite the comics, which would be available offline too:

  As for this point, I have demostrated how to use RecycleView, DiffUtil, through live data and rxJava and room persistence library (although in the next iteration we could use paging library)

get notifications when a new comic is published:
As for the current iteration this feature has not been completed, but a possible solution would be as follows:
	 1. save last comic id to persistent storage
	 2. use WorkManager to run a periodic schedule job
	 3. get latest comic id 
	 4. if those are not the same 
	 5. create a notification 

* search for comics by the comic number as well as text,
As for the current iteration this feature has not been completed. 


* support multiple form factors

  This feature is implemented.



 ## Architecture:
 
 
  Application is divided in 5 module 
  
  1. core - this module contains Result.java wich is a wrapper and contains status, data or error for every request 
  2. domain - usecases and usecase implemnations which provides to ui data throught reactivestreams 
  3. data - repositories wich will provide required data to usecases throught reactivestreams
  4. ui 

  Libraries used:

  1. Retrofit 
  2. Glide for image downloading (need more work to pre-download the images)
  3. Livedata 
  4. Rxjava 
  4. Hilt for Dependency injection
  5. Room for persistence storage 

  Testing:
  
  There is unit test for BrowseComicViewModel

  ### ** To do
  1. configure glide to pre-downlaod images 
  2. pro-guard rules
  3. create configuration for producation 
  4. Create clearer boundary for the domain module, do not use Comic model in other modules 






