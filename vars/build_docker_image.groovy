// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Build the docker image.
// -----------------------------------------------------------------------------

def call(Map config = [:]) {
    def imageName = config.imageName ?: error("[ERROR] 'imageName' is required.")
    def imageTag = config.imageTag ?: 'latest'
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context = config.context ?: '.'
    def noCache = config.noCache ?: false

    def noCacheFlag = noCache ? '--no-cache' : ''

    echo "[INFO] Building Docker image: ${imageName}:${imageTag} from ${dockerfile} (noCache: ${noCache})"
    sh """
        docker build ${noCacheFlag} -t ${imageName}:${imageTag} -f ${dockerfile} ${context}
    """
}
