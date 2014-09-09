package org.jenkinsci.plugins.buildflow.toolbox

import org.jenkinsci.plugins.buildflow.toolbox.BuildFlowToolbox
import com.cloudbees.plugins.flow.BuildFlow
import com.cloudbees.plugins.flow.FlowRun
import com.cloudbees.plugins.flow.BuildFlowDSLExtension
import com.cloudbees.plugins.flow.DSLTestCase
import static hudson.model.Result.SUCCESS
import hudson.model.Job
import hudson.tasks.Shell
import hudson.FilePath
import jenkins.model.Jenkins


/**
 * Created by dnozay on 2014-04-29
 */
class CopyTest extends DSLTestCase {
    public void testUseExtension() {
        // No signature of method: java.util.TreeMap.copyFiles
        // happens when TestDSLExtension is already present.
        BuildFlowDSLExtension.all().clear()
        // we need to insert ourselves here so symbols are found.
        BuildFlowDSLExtension.all().add(new BuildFlowToolbox())

        Job job1 = createJob("job1")
        job1.getBuildersList().add(new Shell("echo hello > artifact"));
        // https://github.com/jenkinsci/jenkins/blob/HEAD/test/src/test/groovy/hudson/model/AbstractProjectTest.groovy#L257

        BuildFlow flow = new BuildFlow(Jenkins.instance, getName())
        flow.buildNeedsWorkspace = true
        flow.onCreatedFromScratch()
        flow.dsl = """
            def toolbox = extension.'build-flow-toolbox'
            b = build("job1")
            toolbox.copyFiles(b.workspace, build.workspace)
        """
        FlowRun flowRun = flow.scheduleBuild2(0).get()
        assert SUCCESS == flowRun.result
        assert flowRun.log.contains("Copying files from")
        FilePath[] list = flowRun.getWorkspace().list()
        assert list.size() == 1
        assert list[0].name == 'artifact'
    }
}