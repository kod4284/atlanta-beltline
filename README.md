# atlanta-beltline

Atlanta Beltline is an application implemented by using JavaFX and MySQL. The application stores information about the Atlanta BeltLine sites and events. Also, this application keeps track of the visitors who visit the different sites and events, users who take transits that connect various sites, and the employees who work in the Atlanta BeltLine system.

Install
-------
You should have MySQL Workbench [MySQL_Workbench](https://dev.mysql.com/downloads/workbench/) and MySQL Sever [MySQL_Sever](https://dev.mysql.com/downloads/mysql/).
Using latest version v8.0 is recommended.

Also, you should have JDK.
If you currently use JDK11 or more, you should download JavaFX library additionally.
The download links are provided here:
[JDK11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html), 
[JavaFX](https://gluonhq.com/products/javafx/)

Run
----
In order to compile type this commend into your terminal.
```shell
javac --module-path javafx-sdk-11.0.2/lib --add-modules=javafx.controls main.java
```
Run
```shell
java --module-path javafx-sdk-11.0.2/lib --add-modules=javafx.controls main.java
```

Additional information
----------------------
To setting environment in IntelliJ: [Setting](https://stackoverflow.com/questions/52682195/how-to-get-javafx-and-java-11-working-in-intellij-idea)

Authors
-------
Team Eleven (Daewoong Ko, Woongrae Cho, Hee Jun Park, Jaemo Koo, Heeseon Kim)
