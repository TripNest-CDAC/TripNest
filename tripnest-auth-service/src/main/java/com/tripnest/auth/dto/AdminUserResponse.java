package com.tripnest.auth.dto;
import com.tripnest.auth.entity.*;
public record AdminUserResponse(Integer userId,String username,String email,String firstName,String lastName,RoleName role,UserStatus status,String profileImagePath) {}
