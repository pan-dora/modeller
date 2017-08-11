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

package cool.pandora.modeller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.command.support.ExitCommand;

/**
 * Bagger Exit.
 *
 * @author gov.loc
 */
public class BaggerExit extends ExitCommand {
    protected static final Logger log = LoggerFactory.getLogger(BaggerExit.class);

    /**
     * Closes the single {@link org.springframework.richclient.application.Application} instance.
     *
     * @see org.springframework.richclient.application.Application#close()
     */
    @Override
    public void doExecuteCommand() {
        super.doExecuteCommand();
        log.debug("BaggerExit.doExecuteCommand");
        System.exit(0);
    }
}
