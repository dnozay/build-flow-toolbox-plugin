package org.jenkinsci.plugins.buildflow.toolbox

import com.cloudbees.plugins.flow.FlowDelegate
import com.cloudbees.plugins.flow.JobInvocation
import hudson.FilePath
import hudson.matrix.MatrixBuild
import hudson.model.Run

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
        Run run = build.build

        if (run == null) {
            dsl.println("Cannot slurp artifacts from a null run")
            return;
        }

        if (run instanceof MatrixBuild) {
            run.exactRuns.each { Run r ->
                dsl.println("Copying child job artifacts from " + r + " to " + run.parent.name + ".")
                copyArtifacts(r, new FilePath(r.artifactsDir))
            }
        } else {
            dsl.println("Copying artifacts from "+build+".")
            copyArtifacts(run, new FilePath(run.artifactsDir))
        }
    }

    /**
     * Copies artifacts for a given run, it can be a run from a matrix or a single job run
     * @param build The build to copy artifacts from
     */
    def copyArtifacts(Run build, FilePath artifactsDir) {
        String[] artifacts = build.artifactManager.root().list('**')
        def artifactsMap = new HashMap<String,String>()
        // here is your chance to rename artifacts...
        //artifacts.each() { it -> dsl.println("artifact: "+it + " copied to artifacts/" + build.fullDisplayName + "/" + it) }
        artifacts.each() {  it ->
            // Copy into a new directory to avoid overriding files
            def path = build.fullDisplayName + "/" + it
            artifactsMap.put(path, it)
        }
        dsl.flowRun.artifactManager.archive(artifactsDir, null,
            dsl.listener, artifactsMap)
    }
}
