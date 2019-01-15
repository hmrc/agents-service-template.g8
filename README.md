A [Giter8](http://www.foundweekends.org/giter8/) template for creating Scala Play projects for HMRC digital services

To create a template service:
==

* Install g8 commandline tool (http://www.foundweekends.org/giter8/setup.html)
* Locate to the directory where you want to create the template
* Decide your service name :-)
* To create a generic microservice run the command

  `g8 hmrc/agents-service-template.g8 --servicename="My Service Name" --mongodb=false`
  
* To create a generic microservice with MongoDB run the command

  `g8 hmrc/agents-service-template.g8 --servicename="My Service Name" --mongodb=true`
  
* To create a generic *frontend* microservice run the command
  
   `g8 hmrc/agents-service-template.g8 -b agents-frontend --servicename="My Service Name"`
  
* The new project folder will be created
* Change working directory to the new one
* Init git repo and do initial commit
* Test generated service with command 

    `sbt test it:test`
    
Known issues
==

* `sbt new` command uses old version of giter8 library (0.7.2), support for conditionals has been added in version 0.10.0
  
To test template itself  
==

* Run `./test-agents-backend-template.sh` 
* and `./test-agents-backend-template-with-mongodb.sh`

Temporary services will be then created and tested in `target/sandbox/new-shiny-service/` and `target/sandbox/new-shiny-service-with-mongodb/`

Frontend template
==

Frontend microservice template is hosted and developed on a separate stable branch `agents-frontend`

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
