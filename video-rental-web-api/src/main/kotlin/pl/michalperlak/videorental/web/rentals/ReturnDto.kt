package pl.michalperlak.videorental.web.rentals

data class ReturnDto(
    val rentalId: String,
    val copies: List<String>
)