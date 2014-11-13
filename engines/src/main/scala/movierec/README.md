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
>>Movie: Maverick (1994), ID: 73, ReleaseDate: 01-Jan-1994, Genre: 1000000000000100010

 Directors: Richard Donner, Writers: Roy Huggins,William Goldman, Actors: Mel Gibson,Jodie Foster,James Garner,Graham Greene,Alfred Molina,James Coburn,Dub Taylor,Geoffrey Lewis,Paul L. Smith,Dan Hedaya,Dennis Fimple,Denver Pyle,Clint Black,Max Perlich,Art LaFleur,Leo Gordon,Paul Tuerpe,Jean De Baer,Paul Brinegar,Hal Ketchum,Corey Feldman,John M. Woodward,Jesse Eric Carroll,Toshonnie Touchin,John Meier,Steven Chambers,Doc Duhame,Frank Orsatti,Lauren Shuler Donner,Courtney Barilla,Kimberly Cullum,Gary Richard Frank,Read Morgan,Steve Kahan,Stephen Liska,Robert Jones,J. Mills Goodloe,Vilmos Zsigmond,Waylon Jennings,Kathy Mattea,Carlene Carter,Vince Gill,Janis Oliver Gill,William Smith,Chuck Hart,Doug McClure,Henry Darrow,Michael Paul Chan,Richard Blum,Bert Remsen,Robert Fuller,Donal Gibson,William Marshall,Bill Henderson,Cal Bartlett,Sam Dolan,Linda Hunt,Charles Dierkop,James Drury,John Fogerty,Michael Forte,Patrick Fullerton,Jack Garner,Danny Glover,Will Hutchins,Bob Jennings,Rick Jensen,Margot Kidder,Reba McEntire,John Otrin,Don Stark

 Runtimes: 127, Countries: USA, Languages: English, Certificates: Argentina:Atp,Australia:PG,Australia:M::(TV rating),Canada:PG::(Manitoba/Nova Scotia/Ontario),Canada:G::(Quebec),Chile:TE,Finland:K-10,Germany:12,Iceland:L,Peru:PT,Portugal:M/12,Singapore:PG,South Korea:12,Spain:T,Sweden:11,UK:PG,USA:PG

 Plot: Maverick is recreated from the character James Garner created in the 1950s TV program. Maverick is a gambler who would rather con someone than fight them. He needs an additional three thousand dollars in order to enter a Winner Take All poker game that begins in a few days. He tries to win some, tries to collect a few debts, and recover a little loot for the reward, all with a light hearted air. He joins forces with a woman gambler with a marvelous, though fake, southern accent as the two both try and enter the game.
```
User Example:
```
UserID: 1 Age: 24 Gender: M Occupation: technician zip: 85711
UserID: 2 Age: 53 Gender: F Occupation: other zip: 94043
UserID: 3 Age: 23 Gender: M Occupation: writer zip: 32067
```

Rating Example:
```
User: 295 rates Movie: 190 (4.0 / 5)
User: 224 rates Movie: 69 (4.0 / 5)
User: 272 rates Movie: 317 (4.0 / 5)
User: 221 rates Movie: 1010 (3.0 / 5)
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
