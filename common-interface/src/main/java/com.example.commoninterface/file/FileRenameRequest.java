package com.example.commoninterface.file;

import lombok.Builder;

@Builder
public record FileRenameRequest(String currentName, String newName) {
}
