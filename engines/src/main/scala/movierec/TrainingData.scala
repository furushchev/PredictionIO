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

class Movie(
    val mid: Int,
    val title: String,
    val releaseDate: String, //TODO Date type
    val genre: Int
  ) extends Serializable {
  override def toString() = "Movie: " + title + ", ID: " + mid +
                            ", ReleaseDate: " + releaseDate + 
                            ", Genre: " + genre.toBinaryString
}


class Genre