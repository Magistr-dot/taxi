@file:Suppress("UNCHECKED_CAST")

package taxipark

data class TaxiPark(
    val allDrivers: Set<Driver>,
    val allPassengers: Set<Passenger>,
    val trips: List<Trip>
) {
    private fun groupAndFilter(list: MutableList<out Any>, int: Int, little: Boolean): Collection<Any> {
        return if (little) {
            list.groupingBy { it }.eachCount().filter { it.value < int }.keys.toSet()
        } else list.groupingBy { it }.eachCount().filter { it.value >= int }.keys.toSet()
    }

    private fun mostlyDiscountMap(mD: Map<Passenger, Int>, m: Map<Passenger, Int>): Map<Passenger, Int> {
        val mNew = mutableMapOf<Passenger, Int>()
        if (mD.isNotEmpty() && m.isNotEmpty()) {
            for (key2 in m.keys) {
                for (key in mD.keys) {
                    if (key2 == key) {
                        if (m[key]!! - mD[key2]!! < mD[key2]!!)
                            mNew[key2] = mD[key2]!! - m[key]!!
                    }
                }
            }
            return mNew
        }
        return mD
    }

    /*
 * Task #1. Найти всех водителей, которые не совершили ни
 * одной поездки
 */
    fun findFakeDrivers(): Set<Driver> {
        val mutableListDriver: MutableList<Driver> = allDrivers.toMutableList()
        return if (trips.isNotEmpty()) {

            trips.forEach { mutableListDriver.add(it.driver) }
            groupAndFilter(mutableListDriver, 2, true) as Set<Driver>
        } else allDrivers
    }

    /*
 * Task #2. Найти всех пассажиров, которые совершили заданное
 * или большее количество поездок
 */
    fun findFaithfulPassengers(minTrips: Int): Set<Passenger> {
        val mutableSet: MutableList<Passenger> = mutableListOf()
        return if (minTrips != 0) {
            for (i in 1..trips.size) {
                trips[i - 1].passengers.forEach { mutableSet.add(it) }
            }
            groupAndFilter(mutableSet, minTrips, false) as Set<Passenger>
        } else allPassengers
    }

    /*
 * Task #3. Найти всех пассажиров, которых два и более раза
 * вез заданный водитель
 */
    fun findFrequentPassengers(driver: Driver): Set<Passenger> {
        val mutableSet: MutableList<Passenger> = mutableListOf()
        for (i in 1..trips.size) {
            if (trips[i - 1].driver == driver) {
                trips[i - 1].passengers.forEach { mutableSet.add(it) }
            }
        }
        return groupAndFilter(mutableSet, 2, false) as Set<Passenger>
    }

    /*
 * Task #4. Найти всех пассажиров, которые большую часть своих
 * поездок ездили со скидкой (trip.discount != null)
 *
 */
    fun findSmartPassengers(): Set<Passenger> {
        val mutableSetD: MutableList<Passenger> = mutableListOf()
        val mutableSet: MutableList<Passenger> = mutableListOf()
        for (i in 1..trips.size) {
            if (trips[i - 1].discount != null) {
                trips[i - 1].passengers.forEach { mutableSetD.add(it) }
            }
        }
        for (i in 1..trips.size) {
            trips[i - 1].passengers.forEach { mutableSet.add(it) }
        }
        val a = mutableSetD.groupingBy { it }
            .eachCount()
            .filter { it.value >= 1 }
        val b = mutableSet.groupingBy { it }
            .eachCount()
            .filter { it.value >= 1 }
        return mostlyDiscountMap(a, b).keys.toSet()
    }

    /*
 * Task #5.
 * Найти наиболее частое время поездки из минутных интервалов 0..9, 10..19, 20..29 и т.п.
 * Вернуть этот интервал или null
 */
    fun findTheMostFrequentTripDurationPeriod(): IntRange? {
        val t = mutableMapOf<String, IntRange>()
        for (i in 1..100) {
            t["t$i"] = ((i * 10) - 10 until i * 10)
        }
        val mutableSet: MutableList<IntRange> = mutableListOf()
        if (trips.isNotEmpty()) {
            for (i in 1..trips.size) {
                for (c in 1 until 100) {
                    when (trips[i - 1].duration) {
                        in t["t$c"]!! -> mutableSet.add(t.getValue("t$c"))
                    }
                }
            }
            return mutableSet
                .groupingBy { it }
                .eachCount()
                .maxBy { it.value }?.key
        }
        return null
    }
}