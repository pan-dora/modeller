package cool.pandora.modeller.bag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * BaggerFetch
 *
 * @author Jon Steinbach
 */
public class BaggerFetch implements Serializable {
    private static final long serialVersionUID = 1L;

    protected static final Logger log = LoggerFactory.getLogger(BaggerFetch.class);

    private String baseURL;
    private String userName;
    private String userPassword;

    /**
     *
     */
    public BaggerFetch() {
        log.debug("BaggerFetch");
    }

    /**
     * @param url String
     */
    public void setBaseURL(final String url) {
        this.baseURL = url;
    }

    /**
     * @return baseURL
     */
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * @param username String
     */
    public void setUserName(final String username) {
        this.userName = username;
    }

    /**
     * @return userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param password String
     */
    public void setUserPassword(final String password) {
        this.userPassword = password;
    }

    /**
     * @return userPassword
     */
    public String getUserPassword() {
        return this.userPassword;
    }

}
