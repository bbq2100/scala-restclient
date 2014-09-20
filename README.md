scala-restclient
====================

What´s this?
-----------
Provides a convenient way to call RESTful webservices. Developed solely for learning purposes. Example is taken from "Scala in Action by Nilanjan Raychaudhuri". Not scaled for real usage !!!

References:

* https://github.com/nraychaudhuri
* http://goo.gl/nI4IUY Scala in Action

Requirements
------------
* [JDK 1.7.0](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Scala 2.10.3](http://www.scala-lang.org/downloads)
* [sbt](http://code.google.com/p/simple-build-tool/) 
* [Git](http://git-scm.com/)

How to build?
-------------
Clone repository from this [repository](https://github.com/qabbasi/scala-restclient) or fork:

    % git clone git@github.com:qabbasi/scala-restclient.git
    % cd scala-restclient

Run *sbt update* to download dependencies

    % sbt update

Run *sbt run* to run the app

    % sbt run
    
How to use it?
--------------
> sbt run (post | get | delete | options) -d <request parameters comma separated -h <headers comma separated> <url>
(at least you should specify action(post, get, delete, options) and server url)
