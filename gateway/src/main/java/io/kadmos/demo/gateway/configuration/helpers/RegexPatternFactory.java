package io.kadmos.demo.gateway.configuration.helpers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RegexPatternFactory {
    static String anyOfRegex(List<UUID> uuids) {
        var baseRegex = uuids.stream().map(uuid -> "(" + uuid + ")").collect(Collectors.joining("|"));
        return "(" + baseRegex + ")";
    }
}
