package converter

val lengthConverter = ConverterUnits(
    "Length",
    listOf(
        listOf<String>("1.0", "meter", "meters", "m"),
        listOf<String>("1000.0", "kilometer", "kilometers", "km"),
        listOf<String>("0.01", "centimeter", "centimeters", "cm"),
        listOf<String>("0.001", "millimeter", "millimeters", "mm"),
        listOf<String>("1609.35", "mile", "miles", "mi"),
        listOf<String>("0.9144", "yard", "yards", "yd"),
        listOf<String>("0.3048", "foot", "feet", "ft"),
        listOf<String>("0.0254", "inch", "inches", "in")
    )
)

val weightConverter = ConverterUnits(
    "Weight",
    listOf(
        listOf("1.0", "gram", "grams", "g"),
        listOf("1000", "kilogram", "kilograms", "kg"),
        listOf("0.001", "milligram", "milligrams", "mg"),
        listOf("453.592", "pound", "pounds", "lb"),
        listOf("28.3495", "ounce", "ounces", "oz")
    )
)

val temperatureConverter = ConverterTemperature(
    "Temperature",
    listOf(
        listOf("1", "degree Celsius", "degrees Celsius", "degree celsius", "degrees celsius", "celsius", "dc", "c"),
        listOf(
            "1",
            "degree Fahrenheit",
            "degrees Fahrenheit",
            "degree fahrenheit",
            "degrees fahrenheit",
            "fahrenheit",
            "df",
            "f"
        ),
        listOf("1", "kelvin", "kelvins", "k")
    )
)

fun test() {
    println("2".matches(Regex("-?\\d+([.,]\\d+)?")))
}

fun main() {
//test()
    menu()
}

fun menu() {

    println("Enter what you want to convert (or exit):")
    val input = readln().split(" ") // [0]-num, [1]-inUnit, [3]-toUnit
    if (input[0] == "exit") {
        exit()
    } else {
        checkConvertUnit(input)
    }
}

fun checkConvertUnit(input: List<String>) {

    /**
     * pattern for a list of input date - chekInput
     * [0] - number
     * [1] - inType
     * [2] - random word ("to" or "in)
     * [3] - toType
     */
    fun splitTemperature(input: List<String>): List<String> {

        val number = input[0]
        val inType = if (input[1].lowercase().contains("degree")) "${input[1]} ${input[2]}" else input[1]
        val toType =
            if (input[input.lastIndex - 1].lowercase().contains("degree")) "${input[input.lastIndex - 1]} ${input[input.lastIndex]}" else input[input.lastIndex]
        return listOf(number, inType, "or", toType)
    }

    fun checkNumber(num: String): Boolean {

        return num.matches(Regex("-?\\d+([.,]\\d+)?"))
    }

    fun convertUnit(checkInput: List<String>) {

        var inType = checkInput[1]
        var toType = checkInput[3]
        val isLengthConverterList = lengthConverter.isUnit(inType, toType)
        val isWeightConverterList = weightConverter.isUnit(inType, toType)
        val isTemperatureConverter = temperatureConverter.isUnit(inType, toType)


        if (isLengthConverterList[0] && isLengthConverterList[1]) {
            converter(lengthConverter, checkInput)

        } else if (isWeightConverterList[0] && isWeightConverterList[1]) {
            converter(weightConverter, checkInput)

        } else if (isTemperatureConverter[0] && isTemperatureConverter[1]) {
            converter(temperatureConverter, checkInput)

        } else {

            //  ConvertUnits.getUnitsString checks the correspondence of types returns List<Boolean>
            val lengthStringImpossibleList = lengthConverter.getUnitsString(inType, toType)
            val weightStringImpossibleList = weightConverter.getUnitsString(inType, toType)
            val temperatureImpossibleList = temperatureConverter.getUnitsString(inType, toType)
            var inTypeImpossible = "???"
            var toTypeImpossible = "???"

            // assign the value of the types (if the type not known then "???")
            if (lengthStringImpossibleList[0] != "???") inTypeImpossible = lengthStringImpossibleList[0]
            if (weightStringImpossibleList[0] != "???") inTypeImpossible = weightStringImpossibleList[0]
            if (temperatureImpossibleList[0] != "???") inTypeImpossible = temperatureImpossibleList[0]
            if (lengthStringImpossibleList[1] != "???") toTypeImpossible = lengthStringImpossibleList[1]
            if (weightStringImpossibleList[1] != "???") toTypeImpossible = weightStringImpossibleList[1]
            if (temperatureImpossibleList[1] != "???") toTypeImpossible = temperatureImpossibleList[1]

            printImpossibleConvert(inTypeImpossible, toTypeImpossible)
        }
    }

    val chekInput = if (input.size > 4) {
        splitTemperature(input)
    } else {
        input
    }

    if (checkNumber(chekInput[0]) && input.size in 4..6) {
        convertUnit(chekInput)
    } else {
        println("Parse error\n")
        menu()
    }
}

fun printImpossibleConvert(inType: String, toType: String) {

    println("Conversion from $inType to $toType is impossible")
    println()
    menu()
}

fun converter(converterUnits: ConverterUnits, input: List<String>) {

    fun thisConverter(converterUnits: ConverterUnits, input: List<String>) {
        val returnDouble = converterUnits.converter(input[0].toDouble(), input[1], input[3])
        val returnUnitsList = converterUnits.getUnitsString(input[1], input[3])
        val returnStringInType = if (input[0].toDouble() == 1.0) returnUnitsList[2] else returnUnitsList[0]
        val returnStringToType = if (returnDouble == 1.0) returnUnitsList[3] else returnUnitsList[1]

        println("${input[0].toDouble()} $returnStringInType is $returnDouble $returnStringToType\n")
    }

    fun numberNegative(converterUnits: ConverterUnits) {
        println("${converterUnits.name} shouldn't be negative\n")
    }
    if (converterUnits.isDigitalPositive(input[0])) {
        thisConverter(converterUnits, input)
    } else {
        numberNegative(converterUnits)
    }

    menu()
}

fun exit() {}

