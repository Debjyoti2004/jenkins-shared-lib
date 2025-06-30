// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Update the Kubernetes manifests with the new image tag only.
// -----------------------------------------------------------------------------

def call(Map config = [:]) {
    def replacements = config.replacements ?: error("[ERROR] 'replacements' map is required")
    def manifestsPath = config.manifestsPath ?: 'k8s'

    echo "[INFO] Updating Kubernetes image tags in manifests: ${replacements}"

    replacements.each { imageName, newTag ->
        echo "[INFO] Replacing tag for image: ${imageName}→${newTag}"

        sh """
            find ${manifestsPath} -type f -name '*.yaml' -exec \\
                sed -i -E 's|(image:\\\\s*)(${imageName}):([^ /]+)|\\\\1\\\\2:${newTag}|g' {} +
        """
    }

    echo "[INFO] Manifest update complete. Please commit and push in pipeline script."
}
