# $servicename$

[ ![Download](https://api.bintray.com/packages/hmrc/releases/$servicenamehyphen$/images/download.svg) ](https://bintray.com/hmrc/releases/$servicenamehyphen$/_latestVersion)

## Running the tests

    sbt test it:test
    
## Running the tests with coverage

    sbt clean coverageOn test it:test coverageReport

## Running the app locally

    sm --start AGENT_MTD -f
    sm --stop $servicename;format="upper,snake"$
    sbt run

It should then be listening on port $serviceport$

    browse http://localhost:$serviceport$/$servicename;format="normalize"$

### License


This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
