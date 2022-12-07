package com.example.springboot3server.creationservice;

import com.example.commoninterface.file.FileCreationRequest;
import com.example.springboot3server.demo.observationdocumentation.ObservationDocumentFileCreationService;
import com.example.springboot3server.entity.Group;
import com.example.springboot3server.entity.User;
import com.example.springboot3server.observationconfig.FileCreationObservationPredicate;
import com.example.springboot3server.observationconfig.GlobalFileCreationObservationConvention;
import com.example.springboot3server.service.FileCreationService;
import com.example.springboot3server.service.FileService;
import com.example.springboot3server.service.GroupService;
import com.example.springboot3server.service.UserService;
import io.micrometer.observation.tck.TestObservationRegistry;
import io.micrometer.observation.tck.TestObservationRegistryAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileCreationServiceObservationTest {

    @Mock
    private UserService userService;

    @Mock
    private GroupService groupService;

    @Mock
    private FileService fileService;

    private TestObservationRegistry registry;
    private ObservationDocumentFileCreationService fileCreationService;

    @BeforeEach
    void setup() {
        registry = TestObservationRegistry.create();
        final var internalFileCreationService = new FileCreationService(userService, groupService, fileService);
        fileCreationService = new ObservationDocumentFileCreationService(internalFileCreationService, registry);
    }

    @Test
    void test_observation_should_properly_create_context() {
        registry.observationConfig()
                .observationPredicate(new FileCreationObservationPredicate())
                .observationConvention(new GlobalFileCreationObservationConvention());

        final var user = User.builder()
                .name("john")
                .build();
        final var group = Group.builder()
                .name("john")
                .build();
        when(userService.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(groupService.findByGroupName(anyString())).thenReturn(Optional.of(group));

        final var request = FileCreationRequest.builder()
                .fileName("20221129.log")
                .userName(user.getName())
                .groupName(user.getName())
                .userPermissions("rw")
                .groupPermissions("rw")
                .otherPermissions("")
                .build();
        fileCreationService.create(request);

        TestObservationRegistryAssert.assertThat(registry)
                .doesNotHaveAnyRemainingCurrentObservation()
                .hasObservationWithNameEqualTo("file.creation")
                .that()
                .hasContextualNameEqualTo("observation with global convention")
                .hasLowCardinalityKeyValue("file.user.name", request.userName())
                .hasLowCardinalityKeyValue("file.group.name", request.groupName())
                .hasHighCardinalityKeyValue("file.name", request.fileName())
                .hasHighCardinalityKeyValue("file.permission.user", request.userPermissions())
                .hasHighCardinalityKeyValue("file.permission.group", request.groupPermissions())
                .hasHighCardinalityKeyValue("file.permission.other", request.otherPermissions())
                .hasBeenStarted()
                .hasBeenStopped();
    }

    @Test
    void test_observation_should_not_be_triggered_for_logger_user() {
        registry.observationConfig()
                .observationPredicate(new FileCreationObservationPredicate())
                .observationConvention(new GlobalFileCreationObservationConvention());

        final var user = User.builder()
                .name("logger")
                .build();
        final var group = Group.builder()
                .name("logger")
                .build();
        when(userService.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(groupService.findByGroupName(anyString())).thenReturn(Optional.of(group));

        final var request = FileCreationRequest.builder()
                .fileName("20221129.log")
                .userName(user.getName())
                .groupName(group.getName())
                .userPermissions("rw")
                .groupPermissions("rw")
                .otherPermissions("r")
                .build();
        fileCreationService.create(request);

        TestObservationRegistryAssert.assertThat(registry)
                .doesNotHaveAnyObservation();
    }

}