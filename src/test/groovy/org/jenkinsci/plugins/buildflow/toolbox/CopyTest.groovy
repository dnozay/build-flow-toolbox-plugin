package org.jenkinsci.plugins.buildflow.toolbox

import com.cloudbees.plugins.flow.DSLTestCase
import static hudson.model.Result.SUCCESS
import hudson.model.Job
import hudson.tasks.Shell
import hudson.FilePath


/**
 * Created by dnozay on 2014-04-29
 */
class CopyTest extends DSLTestCase {
    public void testUseExtension() {
        Job job1 = createJob("job1")
        job1.getBuildersList().add(new Shell("echo hello > artifact"));

        def flow = run("""
            def toolbox = extension.'build-flow-toolbox'
            b = build("job1")
            toolbox.copyFiles(b.workspace, build.workspace)
        """)
        System.out.println flow.log
        assert SUCCESS == flow.result
        assert flow.log.contains("Copying files from")
        FilePath[] list = flow.getWorkspace().list()
        assert list == ['artifact']
    }
}