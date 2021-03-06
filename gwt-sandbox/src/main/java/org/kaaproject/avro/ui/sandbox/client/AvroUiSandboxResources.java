/*
 * Copyright 2014-2015 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.avro.ui.sandbox.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

public interface AvroUiSandboxResources extends ClientBundle {

    public interface KaaTheme extends CssResource {
        
        String DEFAULT_CSS = "KaaTheme.css";

    }
    
    public interface AvroUiSandboxStyle extends AvroUiSandboxTheme {

        String DEFAULT_CSS = "AvroUiSandboxTheme.css";
        
    }
    
    @NotStrict
    @Source(KaaTheme.DEFAULT_CSS)
    KaaTheme kaaTheme();

    @NotStrict
    @Source(AvroUiSandboxStyle.DEFAULT_CSS)
    AvroUiSandboxStyle avroUiSandboxStyle();

}
