// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Update the Kubernetes manifests with the new image tag.
// -----------------------------------------------------------------------------

def call(Map config = [:]) {
    def replacements = config.replacements ?: error("[ERROR] 'replacements' map is required")
    def manifestsPath = config.manifestsPath ?: 'k8s'

    echo "[INFO] Updating Kubernetes image tags: ${replacements}"

    replacements.each { imageName, newTag ->
        echo "[INFO] Replacing tag for image: ${imageName} → ${newTag}"

        // 🛠 FIX: missing `+` in the -exec grep command caused silent failure
        sh """
            echo "[DEBUG] Files containing ${imageName}:"
            find ${manifestsPath} -type f -name '*.yaml' -exec grep -l '${imageName}' {} +
        """

        sh """
            find ${manifestsPath} -type f -name '*.yaml' -exec \\
                sed -i -E 's|(image:\\s*)(${imageName}):([^ /]+)|\\1\\2:${newTag}|g' {} +
        """
    }

    sh "git diff"

    def hasChanges = sh(script: "git diff --quiet || echo changed", returnStdout: true).trim()

    if (hasChanges == "changed") {
        echo "[INFO] Changes detected. Manifests updated. Commit & push will happen in pipeline script."
    } else {
        echo "[INFO] No changes to commit. All image tags are already up-to-date."
    }
}
