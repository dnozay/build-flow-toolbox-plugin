package org.jenkinsci.plugins.buildflow.toolbox;

import com.cloudbees.plugins.flow.BuildFlowDSLExtension;
import com.cloudbees.plugins.flow.FlowDelegate;
import hudson.Extension;

/**
 * Created by dnozay on 2014-04-29
 */
@Extension
public class BuildFlowToolbox extends BuildFlowDSLExtension {
    public static final String EXTENSION_NAME = "build-flow-toolbox";

    @Override
    public Object createExtension(String extensionName, FlowDelegate dsl) {
        if (EXTENSION_NAME.equals(extensionName)) {
            return new BuildFlowToolboxDSL(dsl);
        }
        return null;
    }
}
