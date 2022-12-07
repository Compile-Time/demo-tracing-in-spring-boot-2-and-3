package com.example.springboot3server.observationdocumentation;

import com.example.springboot3server.observationconfig.GlobalFileCreationObservationConvention;
import io.micrometer.common.docs.KeyName;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.docs.ObservationDocumentation;

public enum FileCreationObservationDocumentation implements ObservationDocumentation {
    /**
     * Observe the creation of a new file for a specific user.
     */
    FILE_CREATION {
        @Override
        public Class<? extends ObservationConvention<? extends Observation.Context>> getDefaultConvention() {
            return GlobalFileCreationObservationConvention.class;
        }

        @Override
        public KeyName[] getLowCardinalityKeyNames() {
            return FileCreationLowCardinalityKeys.values();
        }

        @Override
        public KeyName[] getHighCardinalityKeyNames() {
            return FileCreationHighCardinalityKeys.values();
        }

        @Override
        public String getPrefix() {
            return "file";
        }
    };

    public enum FileCreationLowCardinalityKeys implements KeyName {
        /**
         * Name of the user for which a file was created.
         */
        USER_NAME {
            @Override
            public String asString() {
                return "file.user.name";
            }
        },
        /**
         * Name of the group for which a file was created.
         */
        GROUP_NAME {
            @Override
            public String asString() {
                return "file.group.name";
            }
        }
    }

    public enum FileCreationHighCardinalityKeys implements KeyName {
        /**
         * Name of the created file.
         */
        FILE_NAME {
            @Override
            public String asString() {
                return "file.name";
            }
        },
        /**
         * User permissions of the created file.
         */
        FILE_PERMISSION_USER {
            @Override
            public String asString() {
                return "file.permission.user";
            }
        },
        /**
         * Group permissions of the created file.
         */
        FILE_PERMISSION_GROUP {
            @Override
            public String asString() {
                return "file.permission.group";
            }
        },
        /**
         * Permissions for all other users and groups of the created file.
         */
        FILE_PERMISSION_OTHER {
            @Override
            public String asString() {
                return "file.permission.other";
            }
        }
    }
}
