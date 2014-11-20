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
  --port 9997
```

## Register engine through distribution. Useful for params testing.
```
$ cd $PIO_HOME
$ ./make-distribution.sh
$ cd $PIO_HOME/engines/src/main/scala/movierec/examples/
$ $PIO_HOME/bin/pio register
$ $PIO_HOME/bin/pio train -ap **ONE_OF_THE_ALGO**.json
```

## Output statement of pio train
Movie Example:
```
, 1593 -> 
Movie: Death in Brunswick (1991), ID: 1593, ReleaseDate: 16-Aug-1996, Genre: 100000, Itypes: List(Comedy)

 Directors: Steven Soderbergh, Writers: Steven Soderbergh, Actors: Scott Allen,Betsy Brantley,Marcus Lyle Brown,Silas Cooper,C.C. Courtney,Sonny Cranch,Ann Dalrymple,Darrin Dickerson,Andre Dubroc,Ann Hamilton,John Hardy,Coleman Hough,Lori Jefferson,Eddie Jemison,David Jensen,Rodger Kamenetz,Katherine LaNasa,Margaret Lawhon,Mike Malone,Cristin McAlister,John Mese,L. Christian Mixon,Linda Mixon,Liann Pattison,Steven Soderbergh,Ronnie Stutes

 Runtimes: 96, Countries: USA, Languages: English,Japanese,Italian,French, Certificates: Italy:T,UK:15,USA:Not Rated

 Plot: Fletcher Munson is a lethargic, passive worker for a Scientology-like self-help corporation called Eventualism. After the death of a colleague, he is promoted to the job of writing speeches for T. Azimuth Schwitters, the founder and head of the group. He uses this as an excuse to be emotionally and romantically distant from his wife, who, he discovers, is having an affair with his doppelganger, a dentist named Dr. Jeffrey Korchek. As Munson fumbles with the speech and Korchek becomes obsessed with a new patient, a psychotic exterminator named Elmo Oxygen goes around the town seducing lonely wives and taking photographs of his genitals.

```
User Example:
```
, User: 766 rates Movie: 419 (3.0 / 5)
, User: 379 rates Movie: 339 (3.0 / 5)
, User: 698 rates Movie: 507 (4.0 / 5)
, User: 828 rates Movie: 19 (5.0 / 5)
, User: 780 rates Movie: 508 (3.0 / 5)
, User: 294 rates Movie: 123 (4.0 / 5)
```

Rating Example:
```
, User: 752 rates Movie: 1243 (4.0 / 5)
, User: 854 rates Movie: 89 (4.0 / 5)
, User: 764 rates Movie: 318 (5.0 / 5)
, User: 733 rates Movie: 1132 (4.0 / 5
```
Currently, TrainingData is contructed by Rating(Seq), User(HashMap) and Movie(HashMap).
PreparedData is the same as TrainingData (not null any more)


```
This is the featureCounts in FeatureBasedAlgorithm.scala

Map(Animation -> 42, Thriller -> 251, War -> 71, Horror -> 92, Documentary -> 50, Comedy -> 505, Western -> 27, Fantasy -> 22, Romance -> 247, Drama -> 725, Childrens -> 122, Mystery -> 61, Musical -> 56, FilmNoir -> 24, SciFi -> 101, Adventure -> 135, Unknown -> 2, Crime -> 109, Action -> 251)

````
To run/test:
Make sure you train with "-ap featureBasedAlgorithm.json"
curl -i -X POST http://localhost:9997/queries.json -d '{"uid":"12", "mids":["46", "4", "5"]}'
Query now is a combination of a String(uid) and a Seq[String(mid)].

Output: {"movies":[{"46":0.0},{"4":0.0},{"5":0.0}],"isOriginal":true}
Since the algorithm is not working right now, maybe, not tested/adapted yet.

TODO: work on the algorithms

`````
TODO deal with String Runtimes and zipcode, use array/list to store actors, directors and writers(?), deal with multiple countries and languages in one movie...
Adding actors, directors ... to itypes

Parse Plot 
keywords searching => itypes

```



## After deploy, you can get predictions

Show engine status:
```
curl -i -X GET http://localhost:9997
```

Get predictions
e.g.
```
curl -i -X POST http://localhost:9997/queries.json -d '{"uid": 12, "mid": 4}'
```
