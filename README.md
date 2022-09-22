# Running

```shell
# db at localhost:55432/repro, user/pw sa/sa
docker-compose -f dev.docker-compose.yml up -d

# repro
./gradlew run
```

On jOOQ >= 3.14.0 (up to and including 3.17.4) (here 3.17.4)
```
> Task :run
jooq:
+----------+
|foos      |
+----------+
|[({null})]|
+----------+

jdbc:
{(1.23)}
```

On jOOQ < 3.14 (here 3.13.6)
```
> Task :run
jooq:
+-----------+
|foos       |
+-----------+
|[row(1.23)]|
+-----------+

jdbc:
{(1.23)}
```
