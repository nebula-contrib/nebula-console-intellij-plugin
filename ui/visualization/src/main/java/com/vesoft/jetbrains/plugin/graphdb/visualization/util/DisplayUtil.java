package com.vesoft.jetbrains.plugin.graphdb.visualization.util;

import com.vesoft.jetbrains.plugin.graphdb.database.api.data.GraphEntity;
import com.vesoft.jetbrains.plugin.graphdb.database.api.data.GraphNode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.unmodifiableList;

public class DisplayUtil {

    private static final int MAX_TOOLTIP_PROPERTIES = 3;
    private static final int LABEL_TEXT_WIDTH = 300;
    private static final int MAX_TITLE_LENGTH = 40;
    private static final int MAX_TEXT_LENGTH = 60;

    private static final List<String> TITLE_INDICATORS = unmodifiableList(newArrayList("name", "title", "mobile", "ip", "address"));
    private static final Predicate<Map.Entry<String, Object>> IS_STRING_VALUE = o -> String.class.isAssignableFrom(o.getValue().getClass());

    public static String getProperty(GraphNode node) {
        Optional<String> backup = Optional.empty();
        Optional<String> fuzzyMatch = Optional.empty();
        for (Map.Entry<String, Object> entry : node.getPropertyContainer().getProperties().entrySet()) {
            Object valueObj = entry.getValue();
            if (valueObj instanceof String) {
                String key = entry.getKey();
                String value = (String) valueObj;

                if (StringUtils.isBlank(value)) {
                    continue;
                }

                for (String titleIndicator : TITLE_INDICATORS) {
                    if (titleIndicator.equals(key) && filterLength(value)) {
                        return value;
                    }

                    if (key.contains(titleIndicator) && !fuzzyMatch.isPresent()) {
                        fuzzyMatch = Optional.of(value)
                                .filter(DisplayUtil::filterLength);
                    }
                }

                if (!backup.isPresent()) {
                    backup = Optional.of(value)
                            .filter(DisplayUtil::filterLength);
                }
            }
        }

        if (fuzzyMatch.isPresent()) {
            return fuzzyMatch.get();
        }
        String id = node.getId();
        if (NumberUtils.isNumber(id) && backup.isPresent()) {
            return backup.get();
        }
        return id;
    }

    public static String getType(GraphNode node) {
        return node.getTypes().size() > 0 ? node.getTypes().get(0) : "";
    }

    private static boolean filterLength(String title) {
        return title.length() < MAX_TITLE_LENGTH;
    }

    public static String getTooltipTitle(GraphEntity entity) {
        return entity.getId() + ": " + entity.getTypes();
    }

    public static String getTooltipText(GraphEntity entity) {
        Map<String, Object> properties = entity.getPropertyContainer().getProperties();
        String start = "<p width=\"" + LABEL_TEXT_WIDTH + "px\"><b>";

        String typesRepresentation = entity.isTypesSingle() ? entity.getTypes().get(0) : entity.getTypes().toString();
        StringBuilder sb = new StringBuilder(start)
                .append(entity.getTypesName())
                .append("</b>: ")
                .append(typesRepresentation)
                .append("</p>");

        Stream<Map.Entry<String, Object>> strings = properties.entrySet().stream()
                .filter(IS_STRING_VALUE)
                .limit(MAX_TOOLTIP_PROPERTIES);
        Stream<Map.Entry<String, Object>> other = properties.entrySet().stream()
                .filter(IS_STRING_VALUE.negate())
                .limit(MAX_TOOLTIP_PROPERTIES);

        Stream.concat(strings, other)
                .limit(MAX_TOOLTIP_PROPERTIES)
                .forEach(entry -> sb.append(start)
                        .append(entry.getKey())
                        .append("</b>: ")
                        .append(truncate(entry.getValue().toString(), MAX_TEXT_LENGTH))
                        .append("</p>")
                );

        if (properties.size() > MAX_TOOLTIP_PROPERTIES) {
            sb.append("<p>...</p>");
        }

        return "<html>" + sb.toString() + "</html>";
    }

    private static String truncate(String text, int length) {
        return text.length() > length ? text.substring(0, length - 1) + "..." : text;
    }
}
