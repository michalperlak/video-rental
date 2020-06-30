package pl.michalperlak.videorental.rentals.mapper

import pl.michalperlak.videorental.rentals.domain.Return
import pl.michalperlak.videorental.rentals.dto.CopiesReturn
import pl.michalperlak.videorental.rentals.dto.ReturnedCopy

internal fun Return.asDto(): CopiesReturn = CopiesReturn(
    returnId = id.toString(),
    returnDate = returnDate,
    items = items.map { ReturnedCopy(it.copyId, it.delay, it.delayCharge) }
)