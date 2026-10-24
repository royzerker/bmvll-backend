package com.bmvll.backend.dto;

import java.time.Instant;

import com.bmvll.backend.model.Copy;
import com.bmvll.backend.model.CopyCondition;
import com.bmvll.backend.model.CopyStatus;
import com.bmvll.backend.model.Location;

public record CopyResponse(
        String id,
        String bookId,
        String inventoryCode,
        CopyStatus status,
        CopyCondition condition,
        Location location,
        Instant acquisitionDate) {

    public static CopyResponse from(Copy copy) {
        return new CopyResponse(
                copy.getId(),
                copy.getBookId(),
                copy.getInventoryCode(),
                copy.getStatus(),
                copy.getCondition(),
                copy.getLocation(),
                copy.getAcquisitionDate());
    }
}
