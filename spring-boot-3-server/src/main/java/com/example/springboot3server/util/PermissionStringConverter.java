package com.example.springboot3server.util;

import com.example.springboot3server.observationconfig.FileCreationObservationContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionStringConverter {

    private static String convertToPermissionStringWithHyphens(final String permissionString) {
        final var allPermissionChars = new char[]{'r', 'w', 'x'};
        final var convertedPermissionString = new StringBuilder();

        for (final var permissionChar : allPermissionChars) {
            if (permissionString.chars().anyMatch(c -> c == permissionChar)) {
                convertedPermissionString.append(permissionChar);
            } else {
                convertedPermissionString.append('-');
            }
        }

        return convertedPermissionString.toString();
    }

    public static String permissionsAsOneString(final FileCreationObservationContext context) {
        return convertToPermissionStringWithHyphens(context.getUserPermissions()) +
                convertToPermissionStringWithHyphens(context.getGroupPermissions()) +
                convertToPermissionStringWithHyphens(context.getOtherPermissions());
    }

}
