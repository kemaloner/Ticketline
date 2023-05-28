package at.ac.tuwien.inso.sepm.ticketline.client.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;

/**
 * This class can be used to access resource bundles.
 */
@Component
public class BundleManager {

    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(GERMAN, ENGLISH);

    private static final String BASENAME = "localization.ticketlineClient";
    private static final String EXCEPTION_BASENAME = "localization.ticketlineClientExceptions";

    private static final Map<String, ResourceBundle> BUNDLES = new HashMap<>();
    private static final Map<String, ResourceBundle> EXCEPTION_BUNDLES = new HashMap<>();

    private static Locale locale = Locale.getDefault();

    private static LocalizationSubject localizationSubject;

    static {
        SUPPORTED_LOCALES.forEach(locale -> {
            BUNDLES.put(locale.getLanguage(), ResourceBundle.getBundle(BASENAME, locale, new UTF8Control()));
            EXCEPTION_BUNDLES.put(locale.getLanguage(), ResourceBundle.getBundle(EXCEPTION_BASENAME, locale, new UTF8Control()));
        });
    }

    /**
     * An empty private constructor to prevent the creation of an BundleManager Instance.

     private BundleManager() {

     }
     */

    @Autowired
    private BundleManager(LocalizationSubject localizationSubject){
        BundleManager.localizationSubject = localizationSubject;
    }

    /**
     * Gets the bundle for the current locale or if not set the default locale.
     *
     * @return the bundle
     */
    public static ResourceBundle getBundle() {
        return BUNDLES.getOrDefault(locale.getLanguage(), BUNDLES.get(SUPPORTED_LOCALES.get(0).getLanguage()));
    }

    /**
     * Gets the exception bundle for the current locale or if not set the default local.
     *
     * @return the exception bundle
     */
    public static ResourceBundle getExceptionBundle() {
        return EXCEPTION_BUNDLES.getOrDefault(locale.getLanguage(), EXCEPTION_BUNDLES.get(SUPPORTED_LOCALES.get(0).getLanguage()));
    }

    /**
     * Changes the locale.
     *
     * @param locale the locale
     */
    public static void changeLocale(Locale locale) {
        if (!SUPPORTED_LOCALES.contains(locale)) {
            throw new IllegalArgumentException("Locale not supported");
        }
        BundleManager.locale = locale;
        localizationSubject.notifyAllObservers();
    }

    /**
     * Gets the supported locales.
     *
     * @return the supported locales
     */
    public static List<Locale> getSupportedLocales() {
        return SUPPORTED_LOCALES;
    }

    public static String text(String k){
        return getBundle().getString(k);
    }

    public static Locale getLocale() {
        return locale;
    }

    /**
     * UTF-8 resource bundle loader
     */
    private static class UTF8Control extends ResourceBundle.Control {

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    bundle = new PropertyResourceBundle(inputStreamReader);
                }
            }
            return bundle;
        }
    }

}
