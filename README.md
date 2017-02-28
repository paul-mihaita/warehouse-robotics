# warehouse-managent
#Shared Classes

##Node

###Data

* location (Location)
* reserved (Optional boolean)

##Movement

###Data

* ENUMS: forward, backwards, turn left, turn right

##Location

###Data

* x co-ordinate (int)
* y co-ordinate (int)

##Route

###Data

* List of movements
* Start position (Location)
* End position (Location)

##Job

###Data

* Job (Job)
* List of Tasks (List<Task>)

###Methods

* `sumOfWeights()`

##Task

###Data

* Item (Item)
* Reward (float)
* Quantity (int)

##Item

###Data

* Item name (char)
* Location (Location)
* Physical weight (float)

##Robot

###Data

* orientation (Location)
* name (String)
* location (Location)
* job id (int)
* route (Route)
* currently on job (boolean)

###Method

* `startJob()`
* `cancelJob()`
* `wait()`

#PC

##GUI - Tom

###Job

* Selection - Narae
* Input - Jack
* Assignment

##Route Planning

* Planning - Paul

#Robot

##GUI - Maria

* Pick-ups
* Current Location
* Job ID/ Is on job
* Movement

##Movement - Alex, Jack, Maria

* Ex 2 class
* Can be interrupted

