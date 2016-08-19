package main.java.utils;

import java.io.File;
import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Utils {

    public static int TMP = 0;

    public static boolean isXlsExension(String path) {
        if (path.length() < 5) {
            return false;
        }

        String extension = path.substring(path.length() - 3, path.length());
        if (extension.equals("xls")) {
            return true;
        }

        return false;
    }

    public static int countTrueInTab(boolean[] tab) {
        int a = 0;
        for (boolean b : tab) {
            if (b)
                a++;
        }
        return a;
    }

    public static int getNewTmpFileName() {
        TMP++;
        return TMP;
    }

    public static int parseInt(String textContent) {
        if (!"NULL".equals(textContent)) {
            return Integer.parseInt(textContent);
        }

        return 0;
    }

    public static float parseFloat(String textContent) {
        if (!"NULL".equals(textContent)) {
            return Float.parseFloat(textContent);
        }
        return 0;
    }

    public static String getFileName(String campaignName,String extension) {

        String res = deAccent(campaignName);
        res = res.replaceAll(" ", "_");
        res = res.replaceAll("[^a-zA-Z0-9_\\-\\.]+", "");

        return getNewFileBaseName(res, extension, 0);

    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str,
                Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static String getNewFileBaseName(String fileName, String extension,
            int copy) {
        String name = fileName;
        if (copy == 0)
            name += "." + extension;
        else
            name += "_" + copy + "." + extension;

        if (new File(name).exists())
            return getNewFileBaseName(fileName, extension, copy + 1);

        return name;

    }

    public static boolean isGemiusServerMaintenanceHour() {
        LocalDateTime now = LocalDateTime
                .now(TimeZone.getTimeZone("GMT+2").toZoneId());

        DayOfWeek day = now.getDayOfWeek();
        int hour = now.getHour();

        return day == DayOfWeek.WEDNESDAY && hour < 10;

    }

}
