A [Giter8](http://www.foundweekends.org/giter8/) template for creating Scala Play projects for HMRC digital frontend services

## To create a template service:

* Install g8 commandline tool (http://www.foundweekends.org/giter8/setup.html)
* Locate to the directory where you want to create the template
* Decide your service name :-) [do not add `Frontend` suffix]
* To create a generic frontend microservice run the command

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

* Run `./test-agents-frontend-template.sh` 

Backend template
==

Backend microservice template is hosted and developed on a `master` branch, please DONT merge `agents-frontend` branch into `master`.

Content
==

Assuming param `--servicename="New Shiny Service"` the template will produce following files and directories:

```
.
├── README.md
├── app
│   ├── ErrorHandler.scala
│   ├── FrontendModule.scala
│   └── uk
│       └── gov
│           └── hmrc
│               └── newshinyservicefrontend
│                   ├── connectors
│                   │   ├── FrontendAuthConnector.scala
│                   │   └── NewShinyServiceConnector.scala
│                   ├── controllers
│                   │   ├── AuthActions.scala
│                   │   ├── NewShinyServiceFrontendController.scala
│                   │   └── package.scala
│                   ├── models
│                   │   └── NewShinyServiceFrontendModel.scala
│                   ├── services
│                   │   └── AuditService.scala
│                   └── views
│                       ├── error_prefix.scala.html
│                       ├── error_template.scala.html
│                       ├── govuk_wrapper.scala.html
│                       ├── main_template.scala.html
│                       ├── newShinyServiceFrontendForm.scala.html
│                       ├── start.scala.html
│                       └── summary.scala.html
├── build.sbt
├── conf
│   ├── app.routes
│   ├── application-json-logger.xml
│   ├── application.conf
│   ├── logback.xml
│   ├── messages
│   └── prod.routes
├── it
│   └── uk
│       └── gov
│           └── hmrc
│               └── newshinyservicefrontend
│                   ├── connectors
│                   │   └── NewShinyServiceConnectorISpec.scala
│                   ├── controllers
│                   │   ├── AuthActionsISpec.scala
│                   │   └── NewShinyServiceFrontendControllerISpec.scala
│                   ├── stubs
│                   │   ├── AuthStubs.scala
│                   │   └── DataStreamStubs.scala
│                   └── support
│                       ├── BaseISpec.scala
│                       ├── MetricsTestSupport.scala
│                       └── WireMockSupport.scala
├── project
│   ├── build.properties
│   ├── plugins.sbt
│   └── project
└── test
    └── uk
        └── gov
            └── hmrc
                └── newshinyservicefrontend
                    ├── controllers
                    │   └── NewShinyServiceFrontendFormSpec.scala
                    ├── services
                    │   └── AuditServiceSpec.scala
                    └── views
                        └── ViewsSpec.scala
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
