
# HMRC Digital Microservice Template

A [Giter8](http://www.foundweekends.org/giter8/) template for creating HMRC Digital [Playframework](https://playframework.com/) microservices.

##Code is seperated on different branches agents-api and agents-frontend

!!! DO NOT MERGE BRANCHES TO MASTER !!!

## How to generate new microservice project:

1. Install g8 commandline tool (http://www.foundweekends.org/giter8/setup.html)
1. Locate to the directory where you want to create the template.
2. Decide your service name :-) and do not add `Frontend` suffix.
3. To create a frontend microservice run the command (after tweaking param values):

  ```
  g8 hmrc/agents-service-template.g8 -b agents-frontend --servicename="Some Shiny Service" --serviceport=9999
  ```
  
4. To create a backend microservice run the command (after tweaking param values):

  ```
  g8 hmrc/agents-service-template.g8 -b agents-api --servicename="Some Shiny Service" --serviceport=9999 --mongodb=[false|true]
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
  
## Known issues

* `sbt new` command uses old version of giter8 library (0.7.2), support for conditionals has been added in version 0.10.0, use `g8` command instead
  
  
## How to develop and test template itself?

1. Checkout the right branch [agent-api|agent-frontend] as a starting point
2. Remember to use placeholders for names (see examples in existing code)
3. Escape all string interpolation tokens in Scala: `\$` instead of `$` 
3. Create new branch for diverging templates, use conditionals for small optional features 
4. Test template before pushing changes using `./test-*` scripts
5. Always use latest dependencies, upgrade if needed
6. All support and PRs kindly welcome!
  
## Example frontend microservice project layout

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

## Example backend microservice project layout

```
.
├── README.md
├── app
│   ├── MicroserviceModule.scala
│   └── uk
│       └── gov
│           └── hmrc
│               └── newshinyservice
│                   ├── binders
│                   │   ├── SimpleObjectBinder.scala
│                   │   └── UrlBinders.scala
│                   ├── connectors
│                   │   └── MicroserviceAuthConnector.scala
│                   ├── controllers
│                   │   ├── AuthActions.scala
│                   │   └── NewShinyServiceController.scala
│                   ├── models
│                   │   └── NewShinyServiceModel.scala
│                   ├── repository
│                   │   ├── NewShinyServiceRepository.scala
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
│               └── newshinyservice
│                   ├── controllers
│                   │   ├── AuthActionsISpec.scala
│                   │   └── NewShinyServiceControllerISpec.scala
│                   ├── repository
│                   │   └── NewShinyServiceRepositoryISpec.scala
│                   ├── stubs
│                   │   ├── AuthStubs.scala
│                   │   └── DataStreamStubs.scala
│                   └── support
│                       ├── AppBaseISpec.scala
│                       ├── BaseISpec.scala
│                       ├── MetricsTestSupport.scala
│                       ├── MongoApp.scala
│                       ├── ServerBaseISpec.scala
│                       └── WireMockSupport.scala
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
