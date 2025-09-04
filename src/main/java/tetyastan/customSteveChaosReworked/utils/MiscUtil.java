package tetyastan.customSteveChaosReworked.utils;

public class MiscUtil {
    public static String toRoman(int number) {
        String[] romanNumerals = {
                "I","II","III","IV","V","VI","VII","VIII","IX","X"
        };
        if (number <= 0) return "";
        if (number > romanNumerals.length) return String.valueOf(number);
        return romanNumerals[number - 1];
    }
}
