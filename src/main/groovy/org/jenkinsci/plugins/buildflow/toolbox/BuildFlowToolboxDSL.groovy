package org.jenkinsci.plugins.buildflow.toolbox

import com.cloudbees.plugins.flow.FlowDelegate
import hudson.FilePath

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

}
