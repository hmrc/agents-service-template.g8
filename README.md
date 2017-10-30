
# Agents Service Template

A [Giter8](http://www.foundweekends.org/giter8/) template for creating Scala Play projects for HMRC digital services

To create a template service:

1. Locate to the directory where you want to create the template
2. Decide your service name :-) and do not add `Frontend` suffix
3. To create a generic microservice run the command (after tweaking param values):

  ```
  sbt new hmrc/agents-service-template.g8 -b [agents-frontend|agents-api] --servicename="Some Shiny Service" --serviceport=9999
  ```
  
4. The new project folder will be created
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


!!! DO NOT MERGE BRANCHES TO MASTER !!!

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
