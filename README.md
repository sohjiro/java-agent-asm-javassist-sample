# Sample Java Agent and Bytecode manipulation

Sample maven project containing a Java agent and examples of bytecode manipulation with ASM and Javassist.

See article on my blog : http://tomsquest.com/blog/2014/01/intro-java-agent-and-bytecode-manipulation/


## Build

```
$ # From the root dir
$ mvn package
```

## Run

```
$ # From the root dir
$ java -javaagent:agent/target/agent-0.1-SNAPSHOT.jar -jar other/target/other-0.1-SNAPSHOT.jar
```

## Maven/tomcat context
In order to run the agent in a maven/tomcat context you should run the following commands

```
export MAVEN_OPTS="-javaagent:path/to/agent.jar"
mvn package
```

Then you run to the project that you want to be "intercepted" and run the tomcat.
```
mvn tomcat7:run
```

In this way you load the agent jar into the tomcat context and intercept the calls to the specified method.
