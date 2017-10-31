/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller.bag;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaggerFetch.
 *
 * @author Jon Steinbach
 */
public class BaggerFetch implements Serializable {
    protected static final Logger log = LoggerFactory.getLogger(BaggerFetch.class);
    private static final long serialVersionUID = 1L;
    private String baseURL;
    private String userName;
    private String userPassword;

    /**
     * BaggerFetch.
     */
    public BaggerFetch() {
        log.debug("BaggerFetch");
    }

    /**
     * getBaseURL.
     *
     * @return baseURL
     */
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * setBaseURL.
     *
     * @param url String
     */
    public void setBaseURL(final String url) {
        this.baseURL = url;
    }

    /**
     * getUserName.
     *
     * @return userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * setUserName.
     *
     * @param username String
     */
    public void setUserName(final String username) {
        this.userName = username;
    }

    /**
     * getUserPassword.
     *
     * @return userPassword
     */
    public String getUserPassword() {
        return this.userPassword;
    }

    /**
     * setUserPassword.
     *
     * @param password String
     */
    public void setUserPassword(final String password) {
        this.userPassword = password;
    }

}
