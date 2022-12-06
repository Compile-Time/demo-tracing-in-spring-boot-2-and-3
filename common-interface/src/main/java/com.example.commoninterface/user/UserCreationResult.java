package com.example.commoninterface.user;

import lombok.Builder;

@Builder
public record UserCreationResult(String userName, String groupName) {
}
