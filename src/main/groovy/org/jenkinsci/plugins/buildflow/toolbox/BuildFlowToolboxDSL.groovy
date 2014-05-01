package org.jenkinsci.plugins.buildflow.toolbox

import com.cloudbees.plugins.flow.FlowDelegate
import com.cloudbees.plugins.flow.JobInvocation
import hudson.FilePath
import hudson.model.Job

/**
 * Created by dnozay on 2014-04-29
 */
class BuildFlowToolboxDSL {
    def FlowDelegate dsl;

    def BuildFlowToolboxDSL(FlowDelegate dsl) {
        this.dsl = dsl;
    }

    /**
     * Copy files from one <code>FilePath</code> to another.
     * e.g. from a triggered build workspace to the upstream build's workspace.
     * @param sourcedir source <code>FilePath</code>.
     * @param targetdir destination <code>FilePath</code>.
     */
    def copyFiles(FilePath sourcedir, FilePath targetdir) {

        // note: version 0.11 has no workspace.
        // https://issues.jenkins-ci.org/browse/JENKINS-22725
        dsl.println("Copying files from "+sourcedir+" to "+targetdir+".")
        targetdir.mkdirs()
        FilePath[] list = sourcedir.list('**')
        list.each() {
            it -> it.copyToWithPermission(new FilePath(targetdir, it.name))
        }
    }

    /**
     * Copy artifacts from one <code>JobInvocation</code> to the build flow run.
     * @param build downstream build.
     */
    def slurpArtifacts(JobInvocation build) {
        dsl.println("Copying artifacts from "+build+".")
        String[] artifacts = build.artifactManager.root().list('**')
        def artifactsMap = new HashMap<String,String>()
        // here is your chance to rename artifacts...
        artifacts.each() { it -> artifactsMap.put(it, it) }
        dsl.flowRun.artifactManager.archive(build.workspace, null,
            dsl.listener, artifactsMap)
    }

}
