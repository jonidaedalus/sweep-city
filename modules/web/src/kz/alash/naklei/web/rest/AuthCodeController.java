package kz.alash.naklei.web.rest;

import com.haulmont.addon.restapi.api.auth.OAuthTokenIssuer;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import kz.alash.naklei.entity.ExtUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collections;

@RestController
@RequestMapping("/auth-code")
public class AuthCodeController {

    @Inject
    private TrustedClientService trustedClientService;
    @Inject
    private OAuthTokenIssuer oAuthTokenIssuer;
    @Inject
    private Configuration configuration;
    @Inject
    private DataManager dataManager;
    @Inject
    private MessageTools messageTools;

    // here we check secret code and issue token using OAuthTokenIssuer
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity get(@RequestParam("appleID") String id) {
        // obtain system session to be able to call middleware services
        WebAuthConfig webAuthConfig = configuration.getConfig(WebAuthConfig.class);
        UserSession systemSession;
        try {
            systemSession = trustedClientService.getSystemSession(webAuthConfig.getTrustedClientPassword());
        } catch (LoginException e) {
            throw new RuntimeException("Error during system auth");
        }

        // set security context
        AppContext.setSecurityContext(new SecurityContext(systemSession));
        try {
            // find user with apple id
            LoadContext<ExtUser> loadContext = LoadContext.create(ExtUser.class)
                    .setQuery(LoadContext.createQuery("select c from naklei_ExtUser c where c.appleID = :appleID")
                            .setParameter("appleID", id));
            ExtUser user = dataManager.load(loadContext);
            if (user == null) {
                // if coupon is not found - code is incorrect
                return new ResponseEntity<>(new ErrorInfo("invalid_grant", "Bad credentials"), HttpStatus.BAD_REQUEST);
            }
            // generate token for "promo-user"
            OAuthTokenIssuer.OAuth2AccessTokenResult tokenResult =
                    oAuthTokenIssuer.issueToken(user.getLogin(), messageTools.getDefaultLocale(), Collections.emptyMap());
            OAuth2AccessToken accessToken = tokenResult.getAccessToken();

            // set security HTTP headers to prevent browser caching of security token
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
            headers.set(HttpHeaders.PRAGMA, "no-cache");
            return new ResponseEntity<>(accessToken, headers, HttpStatus.OK);
        } finally {
            // clean up security context
            AppContext.setSecurityContext(null);
        }
    }

    // POJO for JSON error messages
    public static class ErrorInfo implements Serializable {
        private String error;
        private String error_description;

        public ErrorInfo(String error, String error_description) {
            this.error = error;
            this.error_description = error_description;
        }

        public String getError() {
            return error;
        }

        public String getError_description() {
            return error_description;
        }
    }
}