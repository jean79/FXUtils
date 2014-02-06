package net.fxutils.utils;

/**
 * Created with IntelliJ IDEA.
 * User: jean
 * Date: 6.2.14
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class FxStringUtils {

    public static boolean isBlank(String string) {
        return string == null || string.trim().equals("");
    }

}
