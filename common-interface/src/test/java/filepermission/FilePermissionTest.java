package filepermission;

import com.example.commoninterface.filepermission.FilePermission;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FilePermissionTest {

    private static Stream<Arguments> provideFilePermissionStrings() {
        return Stream.of(
                Arguments.of("r", FilePermission.R),
                Arguments.of("rw", FilePermission.RW),
                Arguments.of("rx", FilePermission.RX),
                Arguments.of("rwx", FilePermission.RWX),
                Arguments.of("w", FilePermission.W),
                Arguments.of("wx", FilePermission.WX),
                Arguments.of("x", FilePermission.X),
                Arguments.of("", FilePermission.NONE)
        );
    }

    @ParameterizedTest(name = "Permission string {0} should result in enum value {1}")
    @MethodSource("provideFilePermissionStrings")
    void test_from_should_create_proper_permission_enums(final String permissionString,
                                                         final FilePermission expected) {
        assertThat(FilePermission.from(permissionString)).isEqualByComparingTo(expected);
    }
}