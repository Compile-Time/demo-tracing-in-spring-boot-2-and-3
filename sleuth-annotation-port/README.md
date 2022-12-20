# Purpose of this module

Currently, it seems that Spring Boot 3 does not provide any
autoconfiguration for Spring Cloud Sleuth's annotations. Therefore, I
wanted to attempt to provide an aspect oriented programming implementation
for those annotations. However, note that this implementation is by no means a
complete re-implementation of Sleuth's autoconfiguration and serves more as
a learning experience.

If in the future the Spring Boot 3 documentation mentions anything about the
old Sleuth annotations, feel free to open an issue, so this README can be
updated to reference the documentation.