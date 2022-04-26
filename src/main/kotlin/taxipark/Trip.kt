package taxipark

data class Trip(
    val driver: Driver,
    val passengers: Set<Passenger>,
        // the trip duration in minutes
    val duration: Int,
        // the trip distance in km
    val distance: Double,
        // the percentage of discount (in 0.0..1.0 if not null)
    val discount: Double? = null
) {
}