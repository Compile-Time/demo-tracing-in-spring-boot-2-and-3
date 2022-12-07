package com.example.springboot3server.observationconfig;

import com.example.springboot3server.util.PermissionStringConverter;
import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

public class CustomFileCreationObservationConvention implements ObservationConvention<FileCreationObservationContext> {

    @Override
    public boolean supportsContext(final Observation.Context context) {
        return context instanceof FileCreationObservationContext;
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(final FileCreationObservationContext context) {
        return KeyValues.of(
                KeyValue.of("file.user.name", context.getUserName()),
                KeyValue.of("file.group.name", context.getGroupName())
        );
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(final FileCreationObservationContext context) {
        return KeyValues.of(
                KeyValue.of("file.permissions", PermissionStringConverter.permissionsAsOneString(context))
        );
    }

    @Override
    public String getName() {
        return "file.creation";
    }
}
