// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Build the Docker image.
// -----------------------------------------------------------------------------

def call(Map config = [:]) {
    def imageName = config.imageName ?: error("[ERROR] 'imageName' is required.")
    def imageTag = config.imageTag ?: 'latest'
    def dockerfile = config.dockerfile ?: 'Dockerfile'
    def context = config.context ?: '.'
    def noCache = config.noCache ?: false
    def pull = config.pull ?: true 

    def noCacheFlag = noCache ? '--no-cache' : ''
    def pullFlag = pull ? '--pull' : ''

    echo "[INFO] Building Docker image: ${imageName}:${imageTag} from ${dockerfile} (noCache: ${noCache}, pull: ${pull})"

    sh """
        docker build ${noCacheFlag} ${pullFlag} \
          -t ${imageName}:${imageTag} \
          -f ${dockerfile} ${context}
    """
}
