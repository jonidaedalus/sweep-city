package kz.alash.naklei;

import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Arrays;

public class NakleiTestContainer extends TestContainer {

    public NakleiTestContainer() {
        super();
        //noinspection ArraysAsListWithZeroOrOneArgument
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
                // in appPropertiesConfig context parameter of the core module
                "kz/alash/naklei/app.properties",
                // Add this file which is located in CUBA and defines some properties
                // specifically for test environment. You can replace it with your own
                // or add another one in the end.
                "kz/alash/naklei/test-app.properties");
        autoConfigureDataSource();
    }

    

    public static class Common extends NakleiTestContainer {

        public static final NakleiTestContainer.Common INSTANCE = new NakleiTestContainer.Common();

        private static volatile boolean initialized;

        private Common() {
        }

        @Override
        public void beforeAll(ExtensionContext extensionContext) throws Exception {
            if (!initialized) {
                super.beforeAll(extensionContext);
                initialized = true;
            }
            setupContext();
        }
        

        @SuppressWarnings("RedundantThrows")
        @Override
        public void afterAll(ExtensionContext extensionContext) throws Exception {
            cleanupContext();
            // never stops - do not call super
        }
        
    }
}