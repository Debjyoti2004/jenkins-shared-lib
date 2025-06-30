// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Update the Kubernetes manifests with the new image tag.
// -----------------------------------------------------------------------------

def call(Map config = [:]) {
    def replacements = config.replacements ?: error("[ERROR] 'replacements' map is required")
    def manifestsPath = config.manifestsPath ?: 'k8s'
    def gitCredentials = config.gitCredentials ?: 'github-credentials'
    def gitUserName = config.gitUserName ?: 'Jenkins CI'
    def gitUserEmail = config.gitUserEmail ?: 'jenkins@example.com'

    echo "[INFO] Updating Kubernetes image tags: ${replacements}"

    withCredentials([usernamePassword(
        credentialsId: gitCredentials,
        usernameVariable: 'GIT_USERNAME',
        passwordVariable: 'GIT_PASSWORD'
    )]) {
        sh """
            git config --global user.name "${gitUserName}"
            git config --global user.email "${gitUserEmail}"
        """

        replacements.each { imageName, newTag ->
            echo "[INFO] Replacing tag for image: ${imageName} → ${newTag}"

            sh """
                echo "[DEBUG] Files containing ${imageName}:"
                find ${manifestsPath} -type f -name '*.yaml' -exec grep -l '${imageName}' {} \\;
            """

            sh """
                find ${manifestsPath} -type f -name '*.yaml' -exec \\
                    sed -i -E 's|(image:\\s*)(${imageName}):([^ /]+)|\\1\\2:${newTag}|g' {} +
            """
        }

        sh "git diff"

        def hasChanges = sh(script: "git diff --quiet || echo changed", returnStdout: true).trim()

        if (hasChanges == "changed") {
            echo "[INFO] Changes detected. Committing and pushing..."

            sh """
                git add ${manifestsPath}/*.yaml
                git commit -m "[CI/CD] Updated image tags: ${replacements.collect{ k,v -> "$k:$v" }.join(', ')}"
                git remote set-url origin https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/DebjyotiShit/ClearCut.git
                git pull --rebase origin master
                git push origin HEAD:master
            """
        } else {
            echo "[INFO] No changes to commit. All image tags are already up-to-date."
        }
    }
}
