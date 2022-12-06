package com.example.commoninterface.filepermission;

import java.util.Arrays;
import java.util.Comparator;

public enum FilePermission {
    R("r"),
    RW("rw"),
    RX("rx"),
    RWX("rwx"),
    W("w"),
    WX("wx"),
    X("x"),
    NONE("");

    private final String permissionString;

    FilePermission(final String permissionString) {
        this.permissionString = permissionString;
    }

    public static FilePermission from(final String permissionString) {
        if (permissionString.isBlank()) {
            return FilePermission.NONE;
        }
        return Arrays.stream(FilePermission.values())
                .filter(permission -> permission.getPermissionString()
                        .contains(permissionString.toLowerCase()))
                .min(Comparator.comparing(
                        FilePermission::getPermissionString,
                        Comparator.comparing(String::length, Integer::compareTo)
                ))
                .orElseThrow(() -> new IllegalArgumentException("Invalid permission string"));
    }

    public String getPermissionString() {
        return this.permissionString;
    }
}
