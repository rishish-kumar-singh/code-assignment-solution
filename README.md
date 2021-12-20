# coding-assignment



## How to run the application from command line ?

Since this is gradle java project , we'll use gradle tasks to run the application from command line.

Step 1:  Navigate to project root folder.      **code-assignment-solution**

Step 2 :  gradle run --args "filepath"          
          For example , below is the gradle command :  
          
          gradle run --args "C:\\Users\\username\\solution\\src\\main\\resources\\logs\\logfile.txt"
         
Step 3:   To view the results which are stored in DB table, we can use below gradle task command:

          gradle readDBTable
 


## Solution description:

1. Entry point of the application is "Main.java" file at location - rootproject/src/main/java/com/company/Main.java
2. I'm reading logfile.txt and creating event objects read from log file. 
3. Calculation of Event duration is done and a HashMap is created with eventId as Key and event duration as value. 
4. Flagging any long events that take longer than 4ms using findLongEvent() method and writing the found event details to file-based HSQLDB.
5. I'm using gradle tasks for DB connection and DB operations. For this, the logic is written inside "HSQLDB.gradle" file at location  rootproject/HSQLDB.gradle . The database files can be found at location -  rootproject/dev/database.


### Output of the application on console 
<img width="757" alt="gradle_run" src="https://user-images.githubusercontent.com/43696328/146337212-801f567b-af08-4ad4-a21c-fba5f327fe4b.PNG">

Here is the output :
**Below logfile.txt is used.**
[logfile.txt](https://github.com/rishish-kumar-singh/coding-assignment/files/7725717/logfile.txt)

<img width="829" alt="gradle_run_output" src="https://user-images.githubusercontent.com/43696328/146337426-e8a1acf7-24c1-4260-a015-b91a76923cb3.PNG">

Reading data from the DB table using gradle task : gradle readDBTable
<img width="775" alt="gradle_readTable" src="https://user-images.githubusercontent.com/43696328/146337734-8109cea9-f7c9-4907-9f82-15c3c4f2a429.PNG">



