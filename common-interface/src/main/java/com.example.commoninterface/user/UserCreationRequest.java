package com.example.commoninterface.user;

import lombok.Builder;

@Builder
public record UserCreationRequest(String userName, String groupName) {
}
