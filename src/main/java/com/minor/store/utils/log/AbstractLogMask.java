package com.minor.store.utils.log;

import com.minor.store.utils.MaskUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public abstract class AbstractLogMask implements ILogMask {
    private static Logger mlogger = LogManager.getLogger(AbstractLogMask.class);

    public String toLog(AbstractLogMask abstractLogMask) {
        return toLog(this);
    }

    @SuppressWarnings("unchecked")
    public static String toLogString(Object obj) {
        if (obj == null) return null;
        if (obj instanceof ILogMask) {
            return ((ILogMask) obj).toLog();
        } else if (obj instanceof Map) {
            return toLogMap((Map<Object, Object>) obj, new StringBuilder()).toString();
        } else if (obj instanceof Collection) {
            return toLogCollection((Collection<Object>) obj, new StringBuilder()).toString();
        }

        return obj.toString();
    }

    private static StringBuilder toLogMap(Map<Object, Object> obj, StringBuilder sb) {
        sb.append("{");
        boolean first = true;
        for (Map.Entry<Object, Object> entry : obj.entrySet()) {
            first = appendFirst(sb, first);
            sb.append(toLogString(entry.getKey()) + ":" + toLogString(entry.getValue()));
        }
        sb.append("}");
        return sb;
    }

    private static StringBuilder toLogCollection(Collection<Object> collection, StringBuilder sb) {
        sb.append("[");
        boolean first = true;
        for (Object entry : collection) {
            first = appendFirst(sb, first);
            sb.append(toLogString(entry));
        }
        sb.append("}");
        return sb;
    }

    @SuppressWarnings("unchecked")
    public static String toLog(ILogMask obj) {
        if (obj == null) return null;

        StringBuilder sb = new StringBuilder(obj.getClass().getSimpleName() + "{");
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean isMask = obj.getClass().getAnnotation(MaskLog.class) != null;
        boolean firstField = true;
        for (Field field : fields) {
            firstField = appendFirst(sb, firstField);
            sb.append(field.getName() + ":");
            field.setAccessible(true);

            try {
                Object value = field.get(obj);
                if (value == null) {
                    sb.append("null");
                } else if (isMask && field.getAnnotation(HideLog.class) != null) {
                    sb.append(MaskUtil.HIDE.apply(value.toString()));
                } else if (isMask && field.getAnnotation(MaskLog3.class) != null) {
                    sb.append(MaskUtil.mask3(value.toString()));
                } else if (value instanceof Map) {
                    toLogMap((Map<Object, Object>) value, sb);
                } else if (value instanceof Collection) {
                    toLogCollection((Collection<Object>) value, sb);
                } else {
                    sb.append(toLogString(value));
                }
            } catch (Exception e) { // handle exception here to keep as much log as possible
                mlogger.error(e.getMessage());
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private static boolean appendFirst(StringBuilder sb, boolean isFirst) {
        if (!isFirst) sb.append(",");
        return false;
    }
}
