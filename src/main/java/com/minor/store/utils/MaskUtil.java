package com.minor.store.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskUtil {
    private MaskUtil () {}

    private static final String EMPTY_STRING = "";

    public static final Function<String, String> HIDE = input -> EMPTY_STRING;

    private static Logger log = LogManager.getLogger(MaskUtil.class);


    private static List<MaskConfig> maskConfigs = Arrays.asList(
            new MaskConfig("^.*(mobile|phone)(_?number)?$", MaskUtil::mask3),
            new MaskConfig("^.*password$", HIDE),
            new MaskConfig("^.*bank_?account.*(name|number)$", MaskUtil::mask3),
            new MaskConfig("^.*birth_?(date|day)$", HIDE),
            new MaskConfig("^.*identification_?number$", MaskUtil::mask3),
            new MaskConfig("^pid$", MaskUtil::mask3),
            new MaskConfig("^.*jc_?(number|no)$", MaskUtil::mask3),
            new MaskConfig("^laser$", MaskUtil::mask3),
            new MaskConfig("^.*tmn_?key$", MaskUtil::mask3),
            new MaskConfig("^.*commercial_?registration_?number$", MaskUtil::mask3),
            new MaskConfig("^.*tax_?id$", MaskUtil::mask3),
            new MaskConfig("^.*access_?token$", MaskUtil::mask3),
            new MaskConfig("^.*client_?secret$", MaskUtil::mask3),
            new MaskConfig("^.*triple-a\\.authorization$", MaskUtil::mask3)
    );

    protected static List<String> headerKeys = Arrays.asList("authorization");

    private static boolean checkHeader(HttpHeaders header) {
        return header == null || header.entrySet() == null;
    }

    private static boolean checkKey(Entry<String, List<String>> entry,String checkingKey) {
        return entry.getKey() != null && checkingKey.equalsIgnoreCase(entry.getKey());
    }

    private static boolean isMask(Entry<String, List<String>> entry, List<String> headerKeys) {
        for (String checkingKey : headerKeys) {
            if (checkKey(entry, checkingKey)) {
                return true;
            }
        }

        return false;
    }

    private static String createHeaderBody(HttpHeaders header) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, List<String>> entry : header.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }

            sb.append(entry.getKey() + "=");
            boolean mask = isMask(entry, headerKeys);

            sb.append("[");
            boolean isFirst = true;
            for (String item : entry.getValue()) {
                if (isFirst) isFirst = false;
                else sb.append(",");

                sb.append(mask ? MaskUtil.mask3(item) : item);
            }
            sb.append("]");
        }
        sb.append("}");

        return sb.toString();
    }

    public static String maskHeader(HttpHeaders header) {
        if (checkHeader(header)) {
            return null;
        }

        return createHeaderBody(header);
    }

    public static String maskJsonBody(String body) {
        if (body == null) {
            return null;
        }

        if (body.trim().length() == 0) {
            return body;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Object data = null;
        try {  // try with json request
            data = objectMapper.readValue(body, Object.class);
        } catch (Exception e) {/*maybe try another type: application form*/}

        if (data != null) {
            try {
                traverse("", data);
                return objectMapper.writeValueAsString(data);
            } catch (Exception ex) { /* make sure no impact to the operation */
                log.error(ex);
            }
        }
        return body;
    }

    @SuppressWarnings("unchecked")
    private static Object traverse(String key, Object item) {
        if (item == null) return null;

        if (item instanceof Map) {
            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) item).entrySet()) {
                entry.setValue(traverse(key + "." + entry.getKey(), entry.getValue()));
            }

            return item;
        } else if (item instanceof Collection) {
            int index = 0;
            List<Object> result = new ArrayList<>();
            for (Object subItem : (Collection<?>) item) {
                result.add(traverse(key + "[" + index + "]", subItem));
                index++;
            }

            return result;
        } else {
            for (MaskConfig maskConfig : maskConfigs) {
                Matcher matcher = maskConfig.pattern.matcher(key);
                if (matcher != null && matcher.matches()) {
                    return maskConfig.maskFunc.apply(item.toString());
                }
            }

            return item;
        }
    }

    public static String mask3(String input) {
        return maskN(input, 3);
    }

    public static String mask2(String input) {
        return maskN(input, 2);
    }

    public static String maskN(String input, int n) {
        if (input == null) return null;

        if (input.length() <= n) return input;

        return org.apache.commons.lang3.StringUtils.repeat('x', input.length() - n) + input.substring(input.length() - n);
    }

    private static class MaskConfig {
        private Pattern pattern;
        private Function<String, String> maskFunc;

        public MaskConfig(String pattern, Function<String, String> maskFunc) {
            this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            this.maskFunc = maskFunc;
        }
    }
}

