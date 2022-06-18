package kz.alash.naklei;

import com.haulmont.cuba.web.testsupport.TestContainer;

import java.util.ArrayList;
import java.util.Arrays;

public class NakleiWebTestContainer extends TestContainer {

    public NakleiWebTestContainer() {
        appComponents = Arrays.asList(
                "com.haulmont.cuba",
                "com.haulmont.addon.restapi",
                "com.haulmont.addon.maps",
                "com.haulmont.addon.helium",
                "com.haulmont.addon.dashboard",
                "com.haulmont.charts",
                "de.diedavids.cuba.wizard");
        appPropertiesFiles = Arrays.asList(
                // List the files defined in your web.xml
                // in appPropertiesConfig context parameter of the web module
                "kz/alash/naklei/web-app.properties",
                // Add this file which is located in CUBA and defines some properties
                // specifically for test environment. You can replace it with your own
                // or add another one in the end.
                "com/haulmont/cuba/web/testsupport/test-web-app.properties"
        );
    }

    public static class Common extends NakleiWebTestContainer {

        // A common singleton instance of the test container which is initialized once for all tests
        public static final NakleiWebTestContainer.Common INSTANCE = new NakleiWebTestContainer.Common();

        private static volatile boolean initialized;

        private Common() {
        }

        @Override
        public void before() throws Throwable {
            if (!initialized) {
                super.before();
                initialized = true;
            }
            setupContext();
        }

        @Override
        public void after() {
            cleanupContext();
            // never stops - do not call super
        }
    }
}