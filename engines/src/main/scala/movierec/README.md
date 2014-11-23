MovieRec engine development
=====================================
## Authors
- Beth
- Mimi
- Scott
- Tom
- Yuki

## Available Algorithms
- randAlgorithm
    + This is for testing and should not be used
- movieRecAlgorithm
- featureBasedAlgorithm
    + Each property is deemed as independent feature of a movie

## Register engine directly. Useful for testing after engine code change.
```
$ cd $PIO_HOME/engines/
$ $PIO_HOME/bin/pio register --engine-json src/main/scala/movierec/examples/engine.json
$ $PIO_HOME/bin/pio train \
  --engine-json src/main/scala/movierec/examples/engine.json \
  --params-path src/main/scala/movierec/examples/params \
  -ap **ONE_OF_THE_ALGO**.json
$ $PIO_HOME/bin/pio deploy \
  --engine-json src/main/scala/movierec/examples/engine.json \
  --port 8000
```

## Register engine through distribution. Useful for params testing.
```
$ cd $PIO_HOME
$ ./make-distribution.sh
$ cd $PIO_HOME/engines/src/main/scala/movierec/examples/
$ $PIO_HOME/bin/pio register
$ $PIO_HOME/bin/pio train -ap **ONE_OF_THE_ALGO**.json
```

## Outputs from MovieDataSource
Movie Example:
```
>>Movie: Men With Guns (1997), ID: 1646, Year: 1998, Genre: 100000010

    Directors: WrappedArray(Adrienne Shelly),
    Writers: WrappedArray(Adrienne Shelly),
    Actors: WrappedArray(Adrienne Shelly, Tim Guinee, Roger Rees, Louise Lasser, Hynden Walch, Jon Sklaroff, Paul Cassell, Chuck Montgomery, Dave Simonds, Melinda Wade, Pamela Gray, Brian Quirk, C.C. Loveheart, Shirl Bernheim, Elizabeth Newett, Garry Goodrow, Bobby Caravella, Bill Boggs, Elaine Lang, Jan Leslie Harding, Neil Deodhar, Trish Hunter, Kevin Cahoon, Emily Cutler, Kevin Hagan, Joey Golden, Mark Blum, Gordana Rashovich, Hugh Palmer, Harry Bugin, Lynn Clayton)

    Runtimes: Australia:80, Countries: WrappedArray(USA), Languages: WrappedArray(English), Certificates: WrappedArray(Australia:M)

    Plot: Donna witnesses identical murders on the same street on different days. Is this a supernatural conspiracy or is she merely cracking up?

```
User Example:
```
UserID: 1, Age: 24, Gender: M, Occupation: technician, Zip: 85711
UserID: 2, Age: 53, Gender: F, Occupation: other, Zip: 94043
UserID: 3, Age: 23, Gender: M, Occupation: writer, Zip: 32067
UserID: 4, Age: 24, Gender: M, Occupation: technician, Zip: 43537
UserID: 5, Age: 33, Gender: F, Occupation: other, Zip: 15213
```
Rating Example:
```
User: 196 rates Movie: 242 (3.0 / 5)
User: 186 rates Movie: 302 (3.0 / 5)
User: 22 rates Movie: 377 (1.0 / 5)
User: 244 rates Movie: 51 (2.0 / 5)
User: 166 rates Movie: 346 (1.0 / 5)
```

## After deploy, you can get predictions

Show engine status:
```
curl -i -X GET http://localhost:8000
```

Get predictions
e.g.
```
// rank movies
curl -i -X POST http://localhost:8000/queries.json \
-d '{
  "uid" : "2",
  "mids" : ["290", "297", "314", "50", "251", "292"]
}'

// recommend top 5 movies for users
curl -i -X POST http://localhost:8000/queries.json \
-d '{
  "uid" : "2",
  "top" : [5]
}'

// null result
curl -i -X POST http://localhost:8000/queries.json \
-d '{
  "uid" : "2"
}'
output: {"movies":null,"isOriginal":false}
```
