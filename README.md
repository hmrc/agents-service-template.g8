A [Giter8](http://www.foundweekends.org/giter8/) template for creating Scala Play projects for HMRC digital services

To create a template service:

* Locate to the directory where you want to create the template
* Decide your service name :-) [do not add `Frontend` suffix]
* To create a generic microservice run the command

  `sbt new hmrc/agents-service-template.g8 -b agents-frontend|agents-api --servicename="My Service Name"`
  
* The new project folder will be created
* Change working directory to the new one
* Init git repo and do initial commit
* Test generated service with command 

    `sbt test it:test`

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
