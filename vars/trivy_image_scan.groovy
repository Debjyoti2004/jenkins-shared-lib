// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Run Trivy vulnerability scan on a Docker image.
// -----------------------------------------------------------------------------

def call(Map config = [:]) {
    def imageName = config.imageName ?: error("[ERROR] 'imageName' is required.")
    def imageTag  = config.imageTag ?: 'latest'
    def severity  = config.severity ?: 'HIGH,CRITICAL'
    def format    = config.format ?: 'table'

    def fullImage = "${imageName}:${imageTag}"

    echo "[INFO] Running Trivy scan on Docker image: ${fullImage}"
    try {
        sh """
            trivy image \
              --exit-code 1 \
              --severity ${severity} \
              --format ${format} \
              ${fullImage}
        """
        echo "[SUCCESS] Trivy scan completed successfully for ${fullImage}"
    } catch (err) {
        echo "[ERROR] Trivy found vulnerabilities in ${fullImage}: ${err.message}"
        currentBuild.result = 'FAILURE'
        throw err
    }
}
