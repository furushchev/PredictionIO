package io.prediction.engines.movierec

class TrainingData (
    val ratings: Seq[Rating],
    val users:   Seq[User],
    var movies:  Seq[Movie]
  ) extends Serializable {

  override def toString(): String = {
    if (ratings.length > 20) {
      "TrainingData.size=" + ratings.length
    } else {
      ratings.toString
    }
  }
}

// TODO: Determine if we need to prepare training data
// currently it's the same as training data
class PreparedData (
    val ratings: Seq[Rating],
    val users:   Seq[User],
    var movies:  Seq[Movie]
  ) extends Serializable {

  override def toString(): String = {
    if (ratings.length > 20) {
      "TrainingData.size=" + ratings.length
    } else {
      ratings.toString
    }
  }
}

class Rating(
    val uid:    Int,
    val mid:    Int,
    val rating: Float
  ) extends Serializable {
  override def toString() = "User: " + uid + " rates Movie: " + mid + " (" + rating + " / 5)"
}


class User(
    val uid: Int,
    val age: Int,
    val gender: String,
    val occupation: String,
    val zip: String //Some zipcode is T8H1N
  ) extends Serializable {
  override def toString() = "UserID: " + uid + ", Age: " + age +
                            ", Gender: " + gender + ", Occupation: " +
                            occupation + ", Zip: " + zip
}

// movie id | movie title | release date | video release date (TODO) | IMDb URL (TODO) | 
//unknown | Action | Adventure | Animation | Children's | Comedy | Crime | Documentary |
//Drama | Fantasy |Film-Noir | Horror | Musical | Mystery | Romance | Sci-Fi |
//Thriller | War | Western |

 //directors | writers | actors | runtimes (in minutes) | countries | languages | certificates | plot 

class Movie(
    val mid: Int,
    val title: String,
    val releaseDate: String, //TODO Date type
    val genre: Int,
    val directors: String,// TODO separate directors, writers and actors into list/array...
    val writers: String,
    val actors: String,
    val runtimes: String, // in minutes TODO string for now due to Canada:108
    val countries: String,
    val languages: String,
    val certificates: String,
    val plot: String
  ) extends Serializable {
  override def toString() = ">>Movie: " + title + ", ID: " + mid +
                            ", ReleaseDate: " + releaseDate + 
                            ", Genre: " + genre.toBinaryString + 
                            "\n\n Directors: " + directors +
                            ", Writers: " + writers +
                            ", Actors: " + actors +
                            "\n\n Runtimes: " + runtimes +
                            ", Countries: " + countries +
                            ", Languages: " + languages +
                            ", Certificates: " + certificates +
                            "\n\n Plot: " + plot + "\n"
}

object Genre {//extends Enumeration{}
  //type Genre = Value
  /*val unknown = Value(0)
  val Action = Value(1)
  val Adventure = Value(2)
  val Animation = Value(3)
  val Childrens = Value(4)
  val Comedy = Value(5)
  val Crime = Value(6)
  val Documentary = Value(7)
  val Drama = Value(8)
  val Fantasy = Value(9)
  val FilmNoir = Value(10)
  val Horror = Value(11)
  val Musical = Value(12)
  val Mystery = Value(13)
  val Romance = Value(14)
  val SciFi = Value(15)
  val Thriller = Value(16)
  val War = Value(17)
  val Western = Value(18)*/

  //type Genre = Value
  val Unknown = 0
  val Action = 1
  val Adventure = 2
  val Animation = 3
  val Childrens = 4
  val Comedy = 5
  val Crime = 6
  val Documentary = 7
  val Drama = 8
  val Fantasy = 9
  val FilmNoir = 10
  val Horror = 11
  val Musical = 12
  val Mystery = 13
  val Romance = 14
  val SciFi = 15
  val Thriller = 16
  val War = 17
  val Western = 18
  
  val values = Array(Unknown, Action, Adventure, Animation, Childrens, Comedy, Crime,
                Documentary, Drama, Fantasy, FilmNoir, Horror, Musical, Mystery,
                Romance, SciFi, Thriller, War, Western)
}