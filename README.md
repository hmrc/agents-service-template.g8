
# HMRC Digital Microservice Template

A [Giter8](http://www.foundweekends.org/giter8/) template for creating HMRC Digital [Playframework](https://playframework.com/) microservices.

!!! DO NOT MERGE BRANCHES TO MASTER !!!

## How to generate new microservice project:

1. Locate to the directory where you want to create the template.
2. Decide your service name :-) and do not add `Frontend` suffix.
3. To create a project run the command (after tweaking param values):

  ```
  sbt new hmrc/agents-service-template.g8 -b [agents-frontend|agents-api] --servicename="Some Shiny Service" --serviceport=9999
  ```
  
4. The new project folder will be created.
5. Change working directory to the new one:

  `cd some-shiny-service-frontend`

6. Init git repo and do initial commit or configure remote repo:

  ```
  git init
  
  git add .
  git commit -m start
  ```

7. Test generated service with command:

  ```
  sbt test it:test
  ```
  
8. Start your service locally:

  ```
  sbt run
  ```
  
## Example frontend project layout

```
.
├── README.md
├── app
│   ├── ErrorHandler.scala
│   ├── FrontendFilters.scala
│   ├── FrontendModule.scala
│   └── uk
│       └── gov
│           └── hmrc
│               └── someshinyservicefrontend
│                   ├── connectors
│                   │   ├── FrontendAuditConnector.scala
│                   │   ├── FrontendAuthConnector.scala
│                   │   └── SomeShinyServiceConnector.scala
│                   ├── controllers
│                   │   ├── AuthActions.scala
│                   │   ├── SomeShinyServiceFrontendController.scala
│                   │   └── package.scala
│                   ├── models
│                   │   └── SomeShinyServiceFrontendModel.scala
│                   ├── services
│                   │   └── AuditService.scala
│                   └── views
│                       ├── error_template.scala.html
│                       ├── govuk_wrapper.scala.html
│                       ├── main_template.scala.html
│                       ├── someShinyServiceFrontendForm.scala.html
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
│               └── someshinyservicefrontend
│                   ├── connectors
│                   │   └── SomeShinyServiceConnectorISpec.scala
│                   ├── controllers
│                   │   ├── BaseISpec.scala
│                   │   └── SomeShinyServiceFrontendControllerISpec.scala
│                   ├── stubs
│                   │   ├── AuthStubs.scala
│                   │   └── DataStreamStubs.scala
│                   └── support
│                       ├── MetricsTestSupport.scala
│                       └── WireMockSupport.scala
├── project
│   ├── build.properties
│   └── plugins.sbt
└── test
    ├── ErrorHandlerSpec.scala
    └── uk
        └── gov
            └── hmrc
                └── someshinyservicefrontend
                    ├── controllers
                    │   └── SomeShinyServiceFrontendFormSpec.scala
                    ├── services
                    │   └── AuditServiceSpec.scala
                    └── views
                        └── ViewsSpec.scala
```

## Example microservice project layout

```
.
├── README.md
├── app
│   ├── MicroserviceModule.scala
│   └── uk
│       └── gov
│           └── hmrc
│               └── newshinyservice
│                   ├── connectors
│                   │   └── MicroserviceAuthConnector.scala
│                   ├── controllers
│                   │   ├── AuthActions.scala
│                   │   └── NewShinyServiceController.scala
│                   ├── models
│                   │   └── NewShinyServiceModel.scala
│                   └── services
│                       └── AuditService.scala
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
│               └── newshinyservice
│                   ├── controllers
│                   │   ├── AuthActionsISpec.scala
│                   │   └── NewShinyServiceControllerISpec.scala
│                   ├── stubs
│                   │   ├── AuthStubs.scala
│                   │   └── DataStreamStubs.scala
│                   └── support
│                       ├── AppBaseISpec.scala
│                       ├── BaseISpec.scala
│                       ├── MetricsTestSupport.scala
│                       ├── ServerBaseISpec.scala
│                       └── WireMockSupport.scala
├── logs
│   ├── access.log
│   ├── connector.log
│   └── new-shiny-service.log
├── project
│   ├── build.properties
│   ├── plugins.sbt
│   └── project
└── test
    └── uk
        └── gov
            └── hmrc
                └── newshinyservice
                    └── services
                        └── AuditServiceSpec.scala
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
