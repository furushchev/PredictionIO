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
Movie: Toy Story (1995), ID: 1, Year: 1995, Genre: 111000

  Directors: WrappedArray(John Lasseter),
  Writers: WrappedArray(John Lasseter, Pete Docter, Andrew Stanton, Joe Ranft, Joss Whedon, Andrew Stanton, Joel Cohen, Alec Sokolow),
  Actors: WrappedArray(Tom Hanks, Tim Allen, Don Rickles, Jim Varney, Wallace Shawn, John Ratzenberger, Annie Potts, John Morris, Erik von Detten, Laurie Metcalf, R. Lee Ermey, Sarah Freeman, Penn Jillette, Jack Angel, Spencer Aste, Greg Berg, Lisa Bradley, Kendall Cunningham, Debi Derryberry, Cody Dorkin, Bill Farmer, Craig Good, Gregory Grudt, Danielle Judovits, Sam Lasseter, Brittany Levenbrown, Sherry Lynn, Scott McAfee, Mickie McGowan, Ryan O'Donohue, Jeff Pidgeon, Patrick Pinney, Phil Proctor, Jan Rabson, Joe Ranft, Andrew Stanton, Shane Sweet)

  Runtimes: 81, Countries: WrappedArray(USA), Languages: WrappedArray(English), Certificates: WrappedArray(Argentina:Atp, Australia:G, Belgium:KT, Brazil:Livre, Canada:G::(Manitoba/Nova Scotia/Quebec), Canada:F::(Ontario), Chile:TE, Denmark:7, Finland:S, France:U, Germany:o.Al.::(w), Greece:K, Hong Kong:I, Iceland:L, Ireland:G, Malaysia:U, Mexico:AA, Netherlands:AL, New Zealand:G, Norway:7, Peru:PT, Portugal:M/6, Singapore:G, South Korea:All, Spain:T, Sweden:7, UK:PG, USA:G, USA:TV-G::(TV rating))

  Plot: A little boy named Andy loves to be in his room, playing with his toys, especially his doll named "Woody". But, what do the toys do when Andy is not with them, they come to life. Woody believes that he has life (as a toy) good. However, he must worry about Andy's family moving, and what Woody does not know is about Andy's birthday party. Woody does not realize that Andy's mother gave him an action figure known as Buzz Lightyear, who does not believe that he is a toy, and quickly becomes Andy's new favorite toy. Woody, who is now consumed with jealousy, tries to get rid of Buzz. Then, both Woody and Buzz are now lost. They must find a way to get back to Andy before he moves without them, but they will have to pass through a ruthless toy killer, Sid Phillips.,

  Tags: WrappedArray(adventure, animated, animation, cartoon, cgi, childhood, children, classic, clever, computer animation, cute, disney, disney animated feature, entertaining, friendship, fun, fun movie, good, great, great movie, heartwarming, imdb top 250, kids, kids and family, light, original, original plot, oscar (best animated feature), pixar, pixar animation, story, toys, unlikely friendships, witty)


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

featureMoviesMap: e.g.
```
 Map(Michael Apted -> List(730, 619, 729, 621, 620, 1248), Animation -> List(101, 408, 1078, 538, 404, 1409, 542, 189, 969, 1, 206, 1470, 102, 1091, 820, 625, 169, 420, 989, 1219, 71, 103, 240, 1066, 473, 1076, 426, 95, 993, 1240, 99, 1412, 588, 946, 114, 418, 432, 422, 624, 596, 501, 1367), Monika Harris -> List(1433, 1433, 1432, 1432, 1431, 1431)
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
Output:
{"movies":[{"297":-8.53295620539528},{"251":-13.326537513274323},{"292":-15.276804370241758},{"290":-32.944167483781335},{"314":-37.45527366828404},{"50":-47.794163974429495}],"isOriginal":false}

// recommend top 5 movies for users
curl -i -X POST http://localhost:8000/queries.json \
-d '{
  "uid" : "2",
  "top" : [5]
}'
Output:
{"movies":[{"303":-8.53295620539528},{"874":-8.53295620539528},{"297":-8.53295620539528},{"888":-9.202005834376164},{"285":-9.31959978049809}],"isOriginal":false}

// null result
curl -i -X POST http://localhost:8000/queries.json \
-d '{
  "uid" : "2"
}'
output: {"movies":null,"isOriginal":false}


// recommend all the movies (Union) based on the features typed in
curl -i -X POST http://localhost:8000/queries.json \
-d '{
  "uid" : "5",
  "mtypes": ["Animation", "John Lasseter"],
  "display": ["Union"] //Optional, if not specified, it will still print out union of different features
}'

Output:
{"movies":[{"596":-25.22082176821925},{"1470":-31.16715550327287},{"169":-34.57836587315185},{"426":-34.946498374209014},{"189":-36.94793412802281},{"71":-37.206118307044534},{"1367":-38.69278926929178},{"240":-40.8839816580837},{"1076":-44.979302198215194},{"114":-47.4544914326997},{"1219":-48.97366095946974},{"408":-49.68555762995061},{"1409":-51.824482553705465},{"946":-59.72603704615436},{"103":-60.668264789323956},{"1066":-62.319521425686546},{"1091":-63.51245266604218},{"1":-64.75716919294615},{"1":-64.75716919294615},{"1":-64.75716919294615},{"625":-65.10688146524663},{"102":-65.20074413707682},{"473":-66.75730966477082},{"624":-70.00620650589387},{"969":-72.4464810016611},{"820":-75.39996354889115},{"101":-75.69777872007603},{"206":-77.73368523381754},{"1240":-79.45270305167615},{"418":-92.61014852823239},{"1412":-93.4718873481002},{"538":-98.90915274240078},{"404":-110.56416821934475},{"422":-119.30433052549398},{"420":-123.80244189523556},{"989":-130.15696375717877},{"99":-131.31755763625134},{"501":-139.08405398833497},{"588":-149.51307929043355},{"95":-165.7046736529991},{"993":-198.10529468779998},{"432":-239.98016084557386},{"1078":-252.6025820317153},{"542":-320.55905302650393}],"isOriginal":false}


// recomment movies based on the features typed in (Intersection)
curl -i -X POST http://localhost:8000/queries.json -d '{
  "uid":"5",
  "mtypes": ["Animation", "John Lasseter"],
  "display": ["Intersect"]
}'
Output:
{"movies":[{"No movie found":0.0}],"isOriginal":false}


// recommend movies which have Action and Comedy as genre (Intersection)
curl -i -X POST http://localhost:8000/queries.json -d '{
  "uid":"5",
  "mtypes": ["Action","Comedy"],
  "display": ["Intersect"]
}'
Output:
{"movies":[{"1181":-18.250184543315196},{"1180":-20.412558789106466},{"80":-26.188618799226443},{"1183":-28.634306517453087},{"456":-30.629163522281583},{"186":-34.325387552579556},{"435":-34.78395130928946},{"388":-35.87453399788616},{"1188":-36.85320664534093},{"232":-37.53408373330906},{"881":-38.65964523262514},{"876":-38.65964523262514},{"1484":-39.01842849159098},{"4":-39.10917422055226},{"201":-39.47735265775776},{"257":-40.44996199467528},{"391":-40.659897621785035},{"399":-41.71981254899977},{"362":-41.734626104657025},{"173":-41.84720544563858},{"73":-41.97540633232025},{"74":-42.29383605758136},{"184":-43.94467336046435},{"231":-44.24300365397366},{"1138":-45.9945360358919},{"17":-47.892601594588385},{"110":-50.90666190377151},{"29":-52.6846187998012},{"21":-57.44445612147449},{"1110":-58.303160538505495},{"385":-60.82982447034621},{"235":-71.44838270561938}],"isOriginal":false} 

// recommend top 10 movies which have Action and Comedy as genre (Intersection)
curl -i -X POST http://localhost:8000/queries.json -d '{
  "uid":"5",
  "mtypes": ["Action","Comedy"],
  "display": ["Intersect"],
  "top": [10]
}'

Output:
{"movies":[{"1181":-18.250184543315196},{"1180":-20.412558789106466},{"80":-26.188618799226443},{"1183":-28.634306517453087},{"456":-30.629163522281583},{"186":-34.325387552579556},{"435":-34.78395130928946},{"388":-35.87453399788616},{"1188":-36.85320664534093},{"232":-37.53408373330906}],"isOriginal":false}


// recommend top 10 movies which has witty as tag
curl -i -X POST http://localhost:8000/queries.json -d '{
 "uid":"5",
 "mtypes":["witty"],
"top":[10]
}'

Output:
{"movies":[{"718":-92.89030349488802},{"83":-102.27807592150992},{"695":-131.2582669336731},{"213":-145.00798088267655},{"114":-164.6381517952579},{"485":-169.54258309028188},{"1172":-170.9910538906233},{"486":-178.47064752657508},{"792":-179.18659922920878},{"497":-188.23683165316163}],"isOriginal":false}

```

