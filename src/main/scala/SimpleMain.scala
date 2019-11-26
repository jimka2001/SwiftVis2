
case class TempData(day: Int, doy: Int, month: Int, year: Int,
										precip: Double, snow: Double, tave:Double, tmax: Double, tmin: Double)

object SimpleMain {
	val invalidTemp = -999.0
	def toTemperature(token:String):Double = {
		if (token == "." || token == ". ")
			invalidTemp
		else
			token.toDouble
	}
	val invalidPercip = -999.0
	def toPercip(token:String):Double = {
		if (token == "." || token == ". ")
			invalidPercip
		else
			token.toDouble
	}
	def main(args: Array[String]):Unit = {
		println("Let's do some data analytics!")
		val source = scala.io.Source.fromFile("MN212142_9392.csv")
		val lines = source.getLines()
		val header = lines.take(1)
		val allTempData = lines.drop(1).map { line =>
			val parsed = line.split(",").toList
			val tempData = TempData(day = parsed(0).toInt,
															doy = parsed(1).toInt,
															month = parsed(2).toInt,
															// skip State_id
															year = parsed(4).toInt,
															precip = toPercip(parsed(5)),
															snow = if (parsed(6) == ".") 0.0 else parsed(6).toDouble,
															tave = toTemperature(parsed(7)),
															tmax = toTemperature(parsed(8)),
															tmin = toTemperature(parsed(9)))
			//println(s"TempData = $tempData")
			tempData
		}.toArray
		println(s"allTempData=${allTempData.mkString}")
		val janTempData = allTempData.filter(td => td.month==1)
		println(s"janTempData=${janTempData.mkString}")


		source.close()
	}
}
