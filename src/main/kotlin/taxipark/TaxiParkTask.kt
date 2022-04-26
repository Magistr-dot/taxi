package taxipark




fun equalMaps(mD: Map<Passenger, Int>, m: Map<Passenger, Int>): Map<Passenger, Int> {
    var mNew = mutableMapOf<Passenger, Int>()
    if (mD.isNotEmpty() && m.isNotEmpty()) {
        for (key2 in m.keys)
            for (key in mD.keys)
                if (key2 == key) {
                    if (m[key]!! - mD[key2]!! < mD[key2]!!)
                    mNew[key2] = mD[key2]!! - m[key]!!
                }
        return mNew
    }
    return mD
}

/*
 * Task #1. Найти всех водителей, которые не совершили ни
 * одной поездки
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val newList: List<Trip> = trips
    val mutableSet: MutableList<Driver> = allDrivers.toMutableList()

    return if (newList.isNotEmpty()) {
        for (i in 1..newList.size) {
            mutableSet.add(newList[i - 1].driver)
        }
        mutableSet.groupingBy { it }.eachCount().filter { it.value < 2 }.keys.toSet()
    } else allDrivers
}


/*
 * Task #2. Найти всех пассажиров, которые совершили заданное
 * или большее количество поездок
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    val newList = trips
    val mutableSet: MutableList<Passenger> = mutableListOf()

    return if (minTrips != 0) {
        for (i in 1..newList.size) {
            newList[i - 1].passengers.forEach { mutableSet.add(it) }
        }
        mutableSet.groupingBy { it }.eachCount().filter { it.value >= minTrips }.keys.toSet()
    } else allPassengers

}


/*
 * Task #3. Найти всех пассажиров, которых два и более раза
 * вез заданный водитель
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val newList = trips
    val mutableSet: MutableList<Passenger> = mutableListOf()

    for (i in 1..newList.size) {
        if (newList[i - 1].driver == driver) {
            newList[i - 1].passengers.forEach { mutableSet.add(it) }
        }
    }
    return mutableSet.groupingBy { it }.eachCount().filter { it.value >= 2 }.keys.toSet()

}


/*
 * Task #4. Найти всех пассажиров, которые большую часть своих
 * поездок ездили со скидкой (trip.discount != null)
 *
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val newList = trips
    val mutableSetD: MutableList<Passenger> = mutableListOf()
    val mutableSet: MutableList<Passenger> = mutableListOf()

    for (i in 1..newList.size) {
        if (newList[i - 1].discount != null) {
            newList[i - 1].passengers.forEach { mutableSetD.add(it) }
        }
    }
    for (i in 1..newList.size) {
        newList[i - 1].passengers.forEach { mutableSet.add(it) }

    }

    var a = mutableSetD.groupingBy { it }.eachCount().filter { it.value >= 1 }
    var b = mutableSet.groupingBy { it }.eachCount().filter { it.value >= 1 }

    return equalMaps(a, b).keys.toSet()
}


/*
 * Task #5.
 * Найти наиболее частое время поездки из минутных интервалов 0..9, 10..19, 20..29 и т.п.
 * Вернуть этот интервал или null
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val t = mutableMapOf<String, IntRange>()
    for (i in 1..100) {
        t["t$i"] = ((i * 10) - 10 until i * 10)
    }


    val newList = trips
    val mutableSet: MutableList<IntRange> = mutableListOf()


    if (newList.isNotEmpty()) {
        for (i in 1..newList.size) {
            for (c in 1 until 100) {
                when (newList[i - 1].duration) {
                    in t["t$c"]!! -> mutableSet.add(t.getValue("t$c"))
                }
            }
        }
        return mutableSet.groupingBy { it }.eachCount().maxBy { it.value }?.key
    }
    return null
}
