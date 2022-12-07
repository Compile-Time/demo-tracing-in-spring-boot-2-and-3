package com.example.springboot3server.observationconfig;

import io.micrometer.common.KeyValues;
import io.micrometer.observation.GlobalObservationConvention;
import io.micrometer.observation.Observation;

import static com.example.springboot3server.observationdocumentation.FileCreationObservationDocumentation.FileCreationHighCardinalityKeys;
import static com.example.springboot3server.observationdocumentation.FileCreationObservationDocumentation.FileCreationLowCardinalityKeys;


public class GlobalFileCreationObservationConvention implements GlobalObservationConvention<FileCreationObservationContext> {

    @Override
    public boolean supportsContext(final Observation.Context context) {
        return context instanceof FileCreationObservationContext;
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(final FileCreationObservationContext context) {
        return KeyValues.of(
                FileCreationLowCardinalityKeys.USER_NAME.withValue(context.getUserName()),
                FileCreationLowCardinalityKeys.GROUP_NAME.withValue(context.getGroupName())
        );
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(final FileCreationObservationContext context) {
        return KeyValues.of(
                FileCreationHighCardinalityKeys.FILE_NAME.withValue(context.getFileName()),
                FileCreationHighCardinalityKeys.FILE_PERMISSION_USER.withValue(context.getUserPermissions()),
                FileCreationHighCardinalityKeys.FILE_PERMISSION_GROUP.withValue(context.getGroupPermissions()),
                FileCreationHighCardinalityKeys.FILE_PERMISSION_OTHER.withValue(context.getOtherPermissions())
        );
    }

    @Override
    public String getName() {
        return "file.creation";
    }

    @Override
    public String getContextualName(final FileCreationObservationContext ignored) {
        return "create file for user and group";
    }
}
