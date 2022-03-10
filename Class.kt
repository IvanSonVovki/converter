package converter


/**
 * Pattern
 * lengthList: List<List<String>>
 *     [0] - convert coefficient (if not then to 1)
 *     [1] - singular unit
 *     [2] - plural units
 *     [3...until.lastIndex] - any designation
 *     [lastIndex] - abbreviated designation
  */
open class ConverterUnits(val name: String, private val lengthList: List<List<String>>) {



    open fun isDigitalPositive(num: String): Boolean {

        return num.toDouble() >= 0
    }
    private fun converterToMeters(num: Double, tape: String): Double {
        var result = 0.0
        for (list in lengthList) {
            if (list.contains(tape)) {
                result = num * list[0].toDouble()
            }
        }
        return result
    }

    /** pattern: getUnitString  List<String>
     * [0]-inType("???" or type plural)
     * [1]-toType("???" or type plural)
     * [2]-inType(type singular)
     * [3]-toType(type singular)
     */

    fun getUnitsString(inTape: String, toTape: String): List<String> {

        var returnInType0 = "???"
        var returnToType1 = "???"
        var returnInType2 = ""
        var returnToType3 = ""

        for (list in lengthList) {

            if (list.contains(inTape.lowercase())) {
                returnInType0 = list[2] //plural
                returnInType2 = list[1] //singular
            }
            if (list.contains(toTape.lowercase())) {
                returnToType1 = list[2]
                returnToType3 = list[1]
            }
        }

        return listOf(returnInType0, returnToType1, returnInType2, returnToType3)
    }

    fun isUnit(inType: String, toTape: String): List<Boolean> {
        val unit1 = inType.lowercase()
        val unit2 = toTape.lowercase()

        var isInType = false
        var isToType = false
        for (list in lengthList) {
            if (list.contains(unit1)) isInType = true
            if (list.contains(unit2)) isToType = true
        }
        return listOf(isInType, isToType)
    }

    open fun converter(num: Double, inTape: String, toTape: String): Double {

        val inTapeLowCas = inTape.lowercase()
        val toTapeLowCase = toTape.lowercase()
        var result = 0.0
        val unit = converterToMeters(num, inTapeLowCas)
        for (list in lengthList) {
            if (list.contains(toTapeLowCase)) {
                result = unit / list[0].toDouble()
            }
        }
        return result
    }
}

class ConverterTemperature(name: String, private val lengthList: List<List<String>>): ConverterUnits(name, lengthList) {

    override fun isDigitalPositive(num: String): Boolean {
        return true
    }

     override fun converter(num: Double, inTape: String, toTape: String): Double {

         val inTapeLowCas = inTape.lowercase()
         val toTapeLowCase = toTape.lowercase()
         var result = num

         val convertType = typeConvert(inTapeLowCas, toTapeLowCase)

         when(convertType) {
             listOf("c","f") -> result = celsiusFahrenheit(num)
             listOf("f", "c") -> result = celsiusFahrenheit(num, false)
             listOf("k", "c") -> result = kelvinCelsius(num)
             listOf("c", "k") -> result = kelvinCelsius(num, false)
             listOf("f", "k") -> result = fahrenheitKelvins(num)
             listOf("k", "f") -> result = fahrenheitKelvins(num, false)

         }

         return result
     }

    private fun typeConvert(inType: String, toType: String): List<String> {

        var returnInType = ""
        var returnToTape = ""

        for (type in lengthList) {
            if (type.contains(inType)) returnInType = type[type.lastIndex]
            if (type.contains(toType)) returnToTape = type[type.lastIndex]
        }
        return listOf(returnInType, returnToTape)
    }

    private fun celsiusFahrenheit(unit: Double, returnFahrenheit: Boolean = true): Double {

        return if (returnFahrenheit) {
            unit * (9.0 / 5.0) + 32.0

        } else {
            (unit - 32.0) * (5.0 / 9.0)

        }
    }

    private fun kelvinCelsius(unit: Double, returnCelsius: Boolean = true): Double {

        return if (returnCelsius) {
             unit - 273.15
        } else {
            unit + 273.15
        }
    }

    private fun fahrenheitKelvins(unit: Double, returnKelvin: Boolean = true): Double {

        return if (returnKelvin) {
            (unit + 459.67) * (5.0 / 9.0)
        } else {
            unit * (9.0 / 5.0) - 459.67
        }
    }
}