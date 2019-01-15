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

Content
==

Assuming param `--servicename="New Shiny Service With MongoDB"` the template will produce following files and directories:

```
.
├── README.md
├── app
│   ├── MicroserviceModule.scala
│   └── uk
│       └── gov
│           └── hmrc
│               └── newshinyservicewithmongodb
│                   ├── binders
│                   │   ├── SimpleObjectBinder.scala
│                   │   └── UrlBinders.scala
│                   ├── connectors
│                   │   └── MicroserviceAuthConnector.scala
│                   ├── controllers
│                   │   ├── AuthActions.scala
│                   │   └── NewShinyServiceWithMongodbController.scala
│                   ├── models
│                   │   └── NewShinyServiceWithMongodbModel.scala
│                   ├── repository
│                   │   ├── NewShinyServiceWithMongodbRepository.scala
│                   │   └── StrictlyEnsureIndexes.scala
│                   ├── services
│                   │   └── AuditService.scala
│                   └── wiring
│                       ├── MicroserviceFilters.scala
│                       └── MicroserviceMonitoringFilter.scala
├── build.sbt
├── conf
│   ├── app.routes
│   ├── application-json-logger.xml
│   ├── application.conf
│   ├── logback.xml
│   └── prod.routes
├── it
│   └── uk
│       └── gov
│           └── hmrc
│               └── newshinyservicewithmongodb
│                   ├── controllers
│                   │   ├── AuthActionsISpec.scala
│                   │   └── NewShinyServiceWithMongodbControllerISpec.scala
│                   ├── repository
│                   │   └── NewShinyServiceWithMongodbRepositoryISpec.scala
│                   ├── stubs
│                   │   ├── AuthStubs.scala
│                   │   └── DataStreamStubs.scala
│                   └── support
│                       ├── AppBaseISpec.scala
│                       ├── BaseISpec.scala
│                       ├── JsonMatchers.scala
│                       ├── MetricsTestSupport.scala
│                       ├── MongoApp.scala
│                       ├── ServerBaseISpec.scala
│                       ├── WSResponseMatchers.scala
│                       └── WireMockSupport.scala
├── project
│   ├── build.properties
│   ├── plugins.sbt
│   └── project
└── test
    └── uk
        └── gov
            └── hmrc
                └── newshinyservicewithmongodb
                    └── services
                        └── AuditServiceSpec.scala
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
