package helpers;

import managers.ConfigManager;
import net.datafaker.Faker;

import java.time.Month;
import java.util.Locale;

public class DataFakerHelper {
    private static Faker faker;

    public static Faker createFaker() {
        faker = new Faker(new Locale(ConfigManager.getLocale()));
        return faker;
    }

    public static Faker createFakerByLocate(String locateName) {
        faker = new Faker(new Locale(locateName));
        return faker;
    }

    public static Faker getFaker() {
        if (faker == null) {
            faker = createFaker();
        }
        return faker;
    }

    public static Faker getFakerByLocate(String locateName) {
        faker = createFakerByLocate(locateName);
        return faker;
    }

    public static void setFaker(Faker faker) {
        DataFakerHelper.faker = faker;
    }

    public static int getRandomNumber(int min, int max) {
        return getFaker().number().numberBetween(min, max);
    }

    public static String getRandomMonth(){
        int monthNumber = getFaker().number().numberBetween(1, 13);
        String month = Month.of(monthNumber).name();
        return month.charAt(0) + month.substring(1).toLowerCase();
    }
}
