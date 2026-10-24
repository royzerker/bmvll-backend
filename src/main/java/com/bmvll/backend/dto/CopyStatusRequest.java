package com.bmvll.backend.dto;

import com.bmvll.backend.model.CopyStatus;

import jakarta.validation.constraints.NotNull;

public record CopyStatusRequest(@NotNull CopyStatus status) {
}
