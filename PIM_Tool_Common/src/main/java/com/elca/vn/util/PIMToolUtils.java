package com.elca.vn.util;

import com.google.protobuf.Timestamp;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class PIMToolUtils {
    private PIMToolUtils() {
    }

    public static LocalDate convertToLocalDate(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        Date date = convertToDate(timestamp);
        return Objects.nonNull(date) ? date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate() : null;
    }

    public static Date convertToDate(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        return new Date(timestamp.getSeconds() * 1000);
    }

    public static String convertToText(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        Date date = convertToDate(timestamp);
        // Simple date format is not a thread-safe. Not use for static
        Format formatter = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }

    public static Timestamp convertToTimestamp(Date date) {
        if (Objects.isNull(date)) {
            return null;
        }
        return Timestamp.newBuilder().setSeconds(date.getTime() / 1000).build();
    }

}
