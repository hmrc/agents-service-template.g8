A [Giter8](http://www.foundweekends.org/giter8/) template for creating Scala Play projects for HMRC digital services

To create a template service:

1. Locate to the directory where you want to create the template
1. Decide your service name :-) [do not add `Frontend` suffix]
1. To create a generic microservice run the command

  `sbt new hmrc/agents-service-template.g8 -b [agents-frontend|agents-api] --servicename="Some Shiny Service" --serviceport=9999`
  
1. The new project folder will be created
1. Change working directory to the new one

  `cd some-shiny-service-frontend`

1. Init git repo and do initial commit or configure remote repo

  ```
  git init
  
  git add .
  git commit -m start
  ```

1. Test generated service with command 

  `sbt test it:test`


!!! DO NOT MERGE BRANCHES TO MASTER !!!

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
