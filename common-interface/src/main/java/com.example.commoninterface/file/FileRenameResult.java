package com.example.commoninterface.file;

import lombok.Builder;

@Builder
public record FileRenameResult(String currentName, String newName) {
}
