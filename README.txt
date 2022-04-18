README
======

This is a Gradle project structure, so you can get started on the assignment without messing around with Gradle too much.

Basically: 
* Put all your source code in src/main/java (or in further subdirectories inside src/main/java).
* Type "./gradlew run" to run. 
* Type "./gradlew check" to verify PMD rules. (On Windows, drop the "./" from start of course; i.e., "gradlew run" or "gradlew check".)
* Gradle currently considers "edu.curtin.app.App" to be the name (and package) of the main class. If you change this, you must also edit build.gradle, and change the "mainClass = ..." line accordingly.
