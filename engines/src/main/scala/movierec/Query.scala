object Query (
  val uid: Int // user ID
  var iid: Int // item ID
) extends Serializable {
	override def toString = "(" + uid + "," + iid + ")"
}
