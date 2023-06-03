# Purpose of this module

**Note**: With Spring Boot 3.1 and Micrometer Tracing 1.1.0 the aspect oriented
approach of the Sleuth annotations is supported. See
[6.5. Aspect Oriented Programming (starting from Micrometer Tracing 1.1.0)](https://micrometer.io/docs/tracing#_aspect_oriented_programming_starting_from_micrometer_tracing_1_1_0) for
more information.

Currently, it seems that Spring Boot 3 does not provide any
autoconfiguration for Spring Cloud Sleuth's annotations. Therefore, I
wanted to attempt to provide an aspect oriented programming implementation
for those annotations. However, note that this implementation is by no means a
complete re-implementation of Sleuth's autoconfiguration and serves more as
a learning experience.

If in the future the Spring Boot 3 documentation mentions anything about the
old Sleuth annotations, feel free to open an issue, so this README can be
updated to reference the documentation.