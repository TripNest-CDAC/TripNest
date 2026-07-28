package com.tripnest.auth.dto;
import com.tripnest.auth.entity.UserStatus;
import jakarta.validation.constraints.NotNull;
public record UpdateUserStatusRequest(@NotNull UserStatus status) {}
