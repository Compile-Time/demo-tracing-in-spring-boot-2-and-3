# About this module

**Note**: With Spring Boot 3.1 and Micrometer Tracing 1.1.0 the aspect oriented 
approach of the Sleuth annotations is supported. See 
[6.5. Aspect Oriented Programming (starting from Micrometer Tracing 1.1.0)](https://micrometer.io/docs/tracing#_aspect_oriented_programming_starting_from_micrometer_tracing_1_1_0) for 
more information.

This module contains a partial copy from Spring Cloud
Sleuth's source code of the
`org/springframework/cloud/sleuth/instrument/annotation` package. The copied
code is placed into the `com/example/sleuthannotationport/instrumentation`
package. All other packages contain original source code.
Modifications to the original source files are marked with code
comments starting with "Modification:" or "Modifications:" followed by a
sentence or list of sentences explaining the performed modifications. A
copy of the license file for the original source files is provided:
[LICENSE.txt](./LICENSE.txt)

The intent of this module is to estimate how difficult and feasible a port of
Sleuth's autoconfiguration for `@NewSpan`, `@ContinueSpan`, `@SpanTag` and
`@SpanName` would be for non-reactive code. All code in this module is
purely for demonstrative purposes.
